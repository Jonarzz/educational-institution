package io.github.jonarzz.edu.domain.faculty;

import static java.util.stream.Collectors.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;
import java.util.stream.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.professor.*;
import io.github.jonarzz.edu.domain.student.*;

@AggregateRoot
@FieldDefaults(makeFinal = true)
final class Faculty extends NewFaculty {

    UUID id;
    // TODO split into 2 separate aggregates (employment + enrollment)
    Collection<ProfessorView> employedProfessors;
    Collection<StudentView> enrolledStudents;

    FacultyConfiguration config;

    Faculty(UUID id, String name, FieldsOfStudy fieldsOfStudy,
            Collection<ProfessorView> employedProfessors, Vacancies maxProfessorVacancies,
            Collection<StudentView> enrolledStudents, Vacancies maxStudentVacancies,
            FacultyConfiguration config) {
        super(name, fieldsOfStudy, maxProfessorVacancies, maxStudentVacancies);
        this.id = id;
        this.employedProfessors = new HashSet<>(employedProfessors);
        this.enrolledStudents = new HashSet<>(enrolledStudents);
        this.config = config;
    }

    Result<ProfessorView> employ(CandidateForProfessor candidate) {
        return professorEmploymentRules()
                .map(rule -> rule.validate(candidate))
                .<Result<ProfessorView>>flatMap(Optional::stream)
                .findFirst()
                .orElseGet(() -> {
                    var newProfessor = new ProfessorView(candidate.personIdentification());
                    employedProfessors.add(newProfessor);
                    return new Created<>(newProfessor);
                });
    }

    // TODO command + handler
    Result<StudentView> enroll(CandidateForStudent candidate) {
        return studentEnrollmentRules()
                .map(rule -> rule.validate(candidate))
                .<Result<StudentView>>flatMap(Optional::stream)
                .findFirst()
                .orElseGet(() -> {
                    var newStudent = new StudentView(candidate.personIdentification());
                    enrolledStudents.add(newStudent);
                    return new Created<>(newStudent);
                });
    }

    private Stream<ProfessorEmploymentRule> professorEmploymentRules() {
        return Stream.of(
                new DuplicatePreventingRule(),
                new VacancyRule(employedProfessors, maxProfessorVacancies),
                new YearsOfExperienceRule(config),
                new FieldsOfStudyRule(config)
        );
    }

    private Stream<StudentEnrollmentRule> studentEnrollmentRules() {
        return Stream.of(
                new DuplicatePreventingRule(),
                new VacancyRule(enrolledStudents, maxStudentVacancies),
                new MainFieldOfStudyScoreRule(),
                new SecondaryFieldOfStudyScoreRule()
        );
    }

    @Override
    FacultyView toView() {
        return new FacultyView(
                id,
                name,
                fieldsOfStudy,
                employedProfessors,
                maxProfessorVacancies,
                enrolledStudents,
                maxStudentVacancies
        );
    }

    private interface ProfessorEmploymentRule {

        Optional<RuleViolated<ProfessorView>> validate(CandidateForProfessor candidate);
    }

    private interface StudentEnrollmentRule {

        Optional<RuleViolated<StudentView>> validate(CandidateForStudent candidate);
    }

    private record YearsOfExperienceRule(FacultyConfiguration config) implements ProfessorEmploymentRule {

        @Override
        public Optional<RuleViolated<ProfessorView>> validate(CandidateForProfessor candidate) {
            var candidateExperience = candidate.yearsOfExperience();
            var minExperience = config.professorCandidate()
                                      .minimumYearsOfExperience();
            if (candidateExperience < minExperience) {
                return Optional.of(
                        new RuleViolated<>("Candidate has %d years of experience, while minimum required is %d"
                                                   .formatted(candidateExperience, minExperience))
                );
            }
            return Optional.empty();
        }
    }

    @RequiredArgsConstructor
    @FieldDefaults(makeFinal = true)
    private class FieldsOfStudyRule implements ProfessorEmploymentRule {

        FacultyConfiguration config;

        @Override
        public Optional<RuleViolated<ProfessorView>> validate(CandidateForProfessor candidate) {
            var candidateFieldsOfStudy = candidate.fieldsOfStudy();
            var matchingCount = fieldsOfStudy.countMatching(candidateFieldsOfStudy);
            if (matchingCount == fieldsOfStudy.count()) {
                return Optional.empty();
            }
            var minimumMatches = config.professorCandidate()
                                       .minimumNumberOfMatchingFieldsOfStudy();
            if (matchingCount >= minimumMatches) {
                return Optional.empty();
            }
            return Optional.of(
                    new RuleViolated<>("Candidate fields of study (%s) match %d of %d required"
                                               .formatted(candidateFieldsOfStudy,
                                                          matchingCount,
                                                          minimumMatches))
            );
        }
    }

    private class MainFieldOfStudyScoreRule implements StudentEnrollmentRule {

        @Override
        public Optional<RuleViolated<StudentView>> validate(CandidateForStudent candidate) {
            var score = candidate.getScoreFor(fieldsOfStudy.main());
            var minimumScore = config.studentCandidate()
                                     .minimumMainFieldOfStudyScorePercentage();
            if (score.isLowerThan(minimumScore)) {
                return Optional.of(
                        new RuleViolated<>("Student main faculty score %s is lower than minimum %s"
                                                   .formatted(score.asPercentage(),
                                                              minimumScore.asPercentage()))
                );
            }
            return Optional.empty();
        }
    }

    private class SecondaryFieldOfStudyScoreRule implements StudentEnrollmentRule {

        @Override
        public Optional<RuleViolated<StudentView>> validate(CandidateForStudent candidate) {
            var minimumScore = config.studentCandidate()
                                     .minimumSecondaryFieldOfStudyScorePercentage();
            var fieldsOfStudyWithNotEnoughScore =
                    fieldsOfStudy.secondary()
                                 .stream()
                                 .filter(field -> candidate.getScoreFor(field)
                                                           .isLowerThan(minimumScore))
                                 .sorted()
                                 .collect(joining(", "));
            if (fieldsOfStudyWithNotEnoughScore.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(
                    new RuleViolated<>("Student secondary fields of study: %s have score lower than minimum %s"
                                               .formatted(fieldsOfStudyWithNotEnoughScore,
                                                          minimumScore.asPercentage()))
            );
        }
    }

    @SuppressWarnings("unchecked")
    private class DuplicatePreventingRule implements ProfessorEmploymentRule, StudentEnrollmentRule {

        @Override
        public Optional<RuleViolated<ProfessorView>> validate(CandidateForProfessor candidate) {
            return doValidate(candidate, "The professor is already employed")
                    .map(result -> (RuleViolated<ProfessorView>) result);
        }

        @Override
        public Optional<RuleViolated<StudentView>> validate(CandidateForStudent candidate) {
            return doValidate(candidate, "The student is already enrolled")
                    .map(result -> (RuleViolated<StudentView>) result);
        }

        private Optional<RuleViolated<?>> doValidate(Candidate candidate, String errorMessage) {
            if (alreadyEmployed(candidate)) {
                return Optional.of(new RuleViolated<>(errorMessage));
            }
            return Optional.empty();
        }

        private boolean alreadyEmployed(Candidate candidate) {
            var candidateNationalId = candidate.personIdentification()
                                               .nationalIdNumber();
            return employedProfessors.stream()
                                     .map(ProfessorView::personIdentification)
                                     .map(PersonIdentification::nationalIdNumber)
                                     .anyMatch(candidateNationalId::equals);
        }
    }

    @SuppressWarnings("unchecked")
    private record VacancyRule(
            Collection<?> facultyPersons,
            Vacancies maxVacancies
    ) implements ProfessorEmploymentRule, StudentEnrollmentRule {

        @Override
        public Optional<RuleViolated<ProfessorView>> validate(CandidateForProfessor candidate) {
            return validate()
                    .map(result -> (RuleViolated<ProfessorView>) result);
        }

        @Override
        public Optional<RuleViolated<StudentView>> validate(CandidateForStudent candidate) {
            return validate()
                    .map(result -> (RuleViolated<StudentView>) result);
        }

        private Optional<RuleViolated<?>> validate() {
            if (facultyPersons.size() == maxVacancies.count()) {
                return Optional.of(
                        new RuleViolated<>("There is no vacancy")
                );
            }
            return Optional.empty();
        }
    }
}
