package io.github.jonarzz.edu.domain.faculty;

import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;
import java.util.stream.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.common.result.*;
import io.github.jonarzz.edu.domain.professor.*;

@AggregateRoot
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class Faculty extends NewFaculty {

    UUID id;
    Collection<ProfessorView> employedProfessors;

    FacultyConfiguration config;

    Faculty(UUID id, String name, FieldsOfStudy fieldsOfStudy,
            Collection<ProfessorView> employedProfessors, Vacancies maxProfessorVacancies,
            FacultyConfiguration config) {
        super(name, fieldsOfStudy, maxProfessorVacancies);
        this.id = id;
        this.employedProfessors = new HashSet<>(employedProfessors);
        this.config = config;
    }

    Result<ProfessorView> employ(Candidate candidate) {
        return professorEmploymentRules()
                .map(rule -> rule.validate(candidate))
                .<Result<ProfessorView>>flatMap(Optional::stream)
                .findFirst()
                .orElseGet(() -> {
                    var newProfessor = new ProfessorView();
                    employedProfessors.add(newProfessor);
                    return new Created<>(newProfessor);
                });
    }

    private Stream<ProfessorEmploymentRule> professorEmploymentRules() {
        return Stream.of(
                new ProfessorYearsOfExperienceRule(config),
                new ProfessorFieldsOfStudyRule(config),
                new ProfessorEmploymentVacancyRule()
        );
    }

    @Override
    FacultyView toView() {
        return new FacultyView(
                id,
                name,
                fieldsOfStudy,
                employedProfessors,
                maxProfessorVacancies
        );
    }

    private interface ProfessorEmploymentRule {

        Optional<RuleViolated<ProfessorView>> validate(Candidate candidate);
    }

    private record ProfessorYearsOfExperienceRule(FacultyConfiguration config) implements ProfessorEmploymentRule {

        @Override
        public Optional<RuleViolated<ProfessorView>> validate(Candidate candidate) {
            var candidateExperience = candidate.yearsOfExperience();
            var minExperience = config.minimumProfessorYearsOfExperience();
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
    @FieldDefaults(level = PRIVATE, makeFinal = true)
    private class ProfessorFieldsOfStudyRule implements ProfessorEmploymentRule {

        FacultyConfiguration config;

        @Override
        public Optional<RuleViolated<ProfessorView>> validate(Candidate candidate) {
            var candidateFieldsOfStudy = candidate.fieldsOfStudy();
            var matchingFieldsOfStudy = fieldsOfStudy.matching(candidateFieldsOfStudy);
            var matchingCount = matchingFieldsOfStudy.count();
            if (minimumRequiredMatch(matchingCount)) {
                return Optional.empty();
            }
            if (allMatch(matchingCount)) {
                return Optional.empty();
            }
            return Optional.of(
                    new RuleViolated<>("Candidate fields of study (%s) match %d of %d required"
                                               .formatted(candidateFieldsOfStudy,
                                                          matchingCount,
                                                          config.minimumNumberOfMatchingFieldsOfStudy()))
            );
        }

        private boolean minimumRequiredMatch(int matchingCount) {
            return matchingCount >= config.minimumNumberOfMatchingFieldsOfStudy();
        }

        private boolean allMatch(int matchingCount) {
            return matchingCount == fieldsOfStudy.count();
        }
    }

    private class ProfessorEmploymentVacancyRule implements ProfessorEmploymentRule {

        @Override
        public Optional<RuleViolated<ProfessorView>> validate(Candidate candidate) {
            if (employedProfessors.size() == maxProfessorVacancies.count()) {
                return Optional.of(
                        new RuleViolated<>("There is no vacancy")
                );
            }
            return Optional.empty();
        }
    }
}
