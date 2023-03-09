package io.github.jonarzz.edu.domain.faculty;

import static java.util.stream.Collectors.*;

import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;
import java.util.stream.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.student.*;

@AggregateRoot
@FieldDefaults(makeFinal = true)
final class FacultyStudents {

    FieldsOfStudy fieldsOfStudy;
    Collection<StudentView> enrolledStudents;
    Vacancies maxStudentVacancies;

    FacultyConfiguration config;

    FacultyStudents(
            FieldsOfStudy fieldsOfStudy,
            Collection<StudentView> enrolledStudents,
            Vacancies maxStudentVacancies,
            FacultyConfiguration config
    ) {
        this.fieldsOfStudy = fieldsOfStudy;
        this.enrolledStudents = new HashSet<>(enrolledStudents);
        this.maxStudentVacancies = maxStudentVacancies;
        this.config = config;
    }

    Result<StudentView> enroll(CandidateForStudent candidate) {
        return enrollmentRules()
                .map(rule -> rule.validate(candidate))
                .<Result<StudentView>>flatMap(Optional::stream)
                .findFirst()
                .orElseGet(() -> {
                    var newStudent = new StudentView(candidate.personIdentification());
                    enrolledStudents.add(newStudent);
                    return new Created<>(newStudent);
                });
    }

    private Stream<StudentEnrollmentRule> enrollmentRules() {
        return Stream.of(
                new DuplicatePreventingRule(),
                new VacancyRule(),
                new MainFieldOfStudyScoreRule(),
                new SecondaryFieldOfStudyScoreRule()
        );
    }

    private interface StudentEnrollmentRule {

        Optional<RuleViolated<StudentView>> validate(CandidateForStudent candidate);
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

    private class DuplicatePreventingRule implements StudentEnrollmentRule {

        @Override
        public Optional<RuleViolated<StudentView>> validate(CandidateForStudent candidate) {
            if (alreadyEmployed(candidate)) {
                return Optional.of(new RuleViolated<>("The student is already enrolled"));
            }
            return Optional.empty();
        }

        private boolean alreadyEmployed(Candidate candidate) {
            var candidateNationalId = candidate.personIdentification()
                                               .nationalIdNumber();
            return enrolledStudents.stream()
                                   .map(StudentView::personIdentification)
                                   .map(PersonIdentification::nationalIdNumber)
                                   .anyMatch(candidateNationalId::equals);
        }
    }

    private class VacancyRule implements StudentEnrollmentRule {

        @Override
        public Optional<RuleViolated<StudentView>> validate(CandidateForStudent candidate) {
            if (enrolledStudents.size() == maxStudentVacancies.count()) {
                return Optional.of(
                        new RuleViolated<>("There is no vacancy")
                );
            }
            return Optional.empty();
        }
    }
}
