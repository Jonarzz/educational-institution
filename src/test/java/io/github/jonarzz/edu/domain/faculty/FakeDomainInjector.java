package io.github.jonarzz.edu.domain.faculty;

import static lombok.AccessLevel.*;

import lombok.experimental.*;

import io.github.jonarzz.edu.domain.*;
import io.github.jonarzz.edu.domain.professor.*;

@FieldDefaults(level = PRIVATE, makeFinal = true)
class FakeDomainInjector implements DomainInjector {

    FacultyConfiguration facultyConfiguration =
            FakeFacultyConfiguration.builder()
                                    .minimumProfessorYearsOfExperience(5)
                                    .build();
    FacultyRepository repository = new FakeFacultyRepository();

    @Override
    public FacultyConfiguration facultyConfiguration() {
        return facultyConfiguration;
    }

    @Override
    public FacultyRepository facultyRepository() {
        return repository;
    }

    @Override
    public ProfessorRepository professorRepository() {
        return null; // TODO
    }
}
