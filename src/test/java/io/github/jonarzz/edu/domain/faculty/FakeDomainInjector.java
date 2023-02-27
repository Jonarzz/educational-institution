package io.github.jonarzz.edu.domain.faculty;

import static lombok.AccessLevel.*;

import lombok.experimental.*;

import io.github.jonarzz.edu.domain.*;
import io.github.jonarzz.edu.domain.professor.*;

@FieldDefaults(level = PRIVATE, makeFinal = true)
class FakeDomainInjector implements DomainInjector {

    FacultyRepository repository = new FakeFacultyRepository();

    @Override
    public FacultyConfiguration facultiesConfiguration() {
        return null; // TODO
    }

    @Override
    public FacultyRepository facultyRepository() {
        return repository;
    }
}
