package io.github.jonarzz.edu.domain.faculty;

import static io.github.jonarzz.edu.domain.student.StudentView.*;
import static java.util.stream.Collectors.*;

import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;
import java.util.stream.*;

import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.student.*;

@AggregateRoot
@FieldDefaults(makeFinal = true)
class FacultyStudents {

    FieldsOfStudy fieldsOfStudy;
    Collection<PersonIdentification> enrolledStudentIds;
    Vacancies maxStudentVacancies;

    FacultyConfiguration config;

    FacultyStudents(
            FieldsOfStudy fieldsOfStudy,
            Collection<PersonIdentification> enrolledStudentIds,
            Vacancies maxStudentVacancies,
            FacultyConfiguration config
    ) {
        this.fieldsOfStudy = fieldsOfStudy;
        this.enrolledStudentIds = Set.copyOf(enrolledStudentIds);
        this.maxStudentVacancies = maxStudentVacancies;
        this.config = config;
    }

    Result<StudentView> enroll(CandidateForStudent candidate) {
        return enrollmentRules()
                .map(rule -> rule.calculateViolation(candidate))
                .<Result<StudentView>>flatMap(Optional::stream)
                .findFirst()
                .orElseGet(() -> new Created<>(newStudent(
                        candidate.personIdentification()
                )));
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

        Optional<RuleViolated<StudentView>> calculateViolation(CandidateForStudent candidate);
    }

    private class MainFieldOfStudyScoreRule implements StudentEnrollmentRule {

        @Override
        public Optional<RuleViolated<StudentView>> calculateViolation(CandidateForStudent candidate) {
            var score = candidate.getScoreFor(fieldsOfStudy.main());
            var minimumScore = config.studentCandidate()
                                     .mainFieldOfStudyMinimumScorePercentage();
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
        public Optional<RuleViolated<StudentView>> calculateViolation(CandidateForStudent candidate) {
            var minimumScore = config.studentCandidate()
                                     .secondaryFieldOfStudyMinimumScorePercentage();
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
        public Optional<RuleViolated<StudentView>> calculateViolation(CandidateForStudent candidate) {
            if (alreadyEmployed(candidate)) {
                return Optional.of(new RuleViolated<>("The student is already enrolled"));
            }
            return Optional.empty();
        }

        private boolean alreadyEmployed(CandidateForStudent candidate) {
            var candidateNationalId = candidate.personIdentification()
                                               .nationalIdNumber();
            return enrolledStudentIds.stream()
                                     .map(PersonIdentification::nationalIdNumber)
                                     .anyMatch(candidateNationalId::equals);
        }
    }

    private class VacancyRule implements StudentEnrollmentRule {

        @Override
        public Optional<RuleViolated<StudentView>> calculateViolation(CandidateForStudent candidate) {
            if (enrolledStudentIds.size() == maxStudentVacancies.count()) {
                return Optional.of(
                        new RuleViolated<>("There is no vacancy")
                );
            }
            return Optional.empty();
        }
    }
}
