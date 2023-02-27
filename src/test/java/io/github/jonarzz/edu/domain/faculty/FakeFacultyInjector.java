package io.github.jonarzz.edu.domain.faculty;

import static lombok.AccessLevel.*;

import lombok.experimental.*;

@FieldDefaults(level = PRIVATE, makeFinal = true)
class FakeFacultyInjector implements FacultyInjector {

    FacultyRepository repository = new FakeFacultyRepository();

    @Override
    public FacultyConfiguration facultiesConfiguration() {
        return null; // TODO
    }

    @Override
    public FacultyRepository educationalInstitutionRepository() {
        return repository;
    }
}
