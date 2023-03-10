package io.github.jonarzz.edu.domain.faculty;

import static io.github.jonarzz.edu.domain.professor.ProfessorView.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;
import java.util.stream.*;

import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.professor.*;

@AggregateRoot
@FieldDefaults(makeFinal = true)
class FacultyProfessors {

    FieldsOfStudy fieldsOfStudy;
    Collection<PersonIdentification> employedProfessorIds;
    Vacancies maxVacancies;

    FacultyConfiguration config;

    FacultyProfessors(
            FieldsOfStudy fieldsOfStudy,
            Collection<PersonIdentification> employedProfessorIds,
            Vacancies maxVacancies,
            FacultyConfiguration config
    ) {
        this.fieldsOfStudy = fieldsOfStudy;
        this.employedProfessorIds = Set.copyOf(employedProfessorIds);
        this.maxVacancies = maxVacancies;
        this.config = config;
    }

    Result<ProfessorView> employ(CandidateForProfessor candidate) {
        return employmentRules()
                .map(rule -> rule.calculateViolation(candidate))
                .<Result<ProfessorView>>flatMap(Optional::stream)
                .findFirst()
                .orElseGet(() -> new Created<>(newProfessor(
                        candidate.personIdentification(),
                        candidate.fieldsOfStudy()
                )));
    }

    private Stream<ProfessorEmploymentRule> employmentRules() {
        return Stream.of(
                new DuplicatePreventingRule(),
                new VacancyRule(),
                new YearsOfExperienceRule(config),
                new FieldsOfStudyRule(config)
        );
    }

    private interface ProfessorEmploymentRule {

        Optional<RuleViolated<ProfessorView>> calculateViolation(CandidateForProfessor candidate);
    }

    private class DuplicatePreventingRule implements ProfessorEmploymentRule {

        @Override
        public Optional<RuleViolated<ProfessorView>> calculateViolation(CandidateForProfessor candidate) {
            // TODO take 'active' flag into account
            if (alreadyEmployed(candidate)) {
                return Optional.of(new RuleViolated<>("The professor is already employed"));
            }
            return Optional.empty();
        }

        private boolean alreadyEmployed(CandidateForProfessor candidate) {
            var candidateNationalId = candidate.personIdentification()
                                               .nationalIdNumber();
            return employedProfessorIds.stream()
                                       .map(PersonIdentification::nationalIdNumber)
                                       .anyMatch(candidateNationalId::equals);
        }
    }

    private class VacancyRule implements ProfessorEmploymentRule {

        @Override
        public Optional<RuleViolated<ProfessorView>> calculateViolation(CandidateForProfessor candidate) {
            if (employedProfessorIds.size() == maxVacancies.count()) {
                return Optional.of(
                        new RuleViolated<>("There is no vacancy")
                );
            }
            return Optional.empty();
        }
    }

    private record YearsOfExperienceRule(FacultyConfiguration config) implements ProfessorEmploymentRule {

        @Override
        public Optional<RuleViolated<ProfessorView>> calculateViolation(CandidateForProfessor candidate) {
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
        public Optional<RuleViolated<ProfessorView>> calculateViolation(CandidateForProfessor candidate) {
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

}