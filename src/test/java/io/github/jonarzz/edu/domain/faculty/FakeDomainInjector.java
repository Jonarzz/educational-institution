package io.github.jonarzz.edu.domain.faculty;

import static lombok.AccessLevel.*;

import lombok.experimental.*;

import io.github.jonarzz.edu.domain.*;
import io.github.jonarzz.edu.domain.professor.*;

@FieldDefaults(level = PRIVATE, makeFinal = true)
class FakeDomainInjector implements DomainInjector {

    FacultyConfiguration facultyConfiguration = new FakeFacultyConfiguration();
    FacultyRepository facultyRepository = new FakeFacultyRepository();
    FakeProfessorRepository professorRepository = new FakeProfessorRepository();

    @Override
    public FacultyConfiguration facultyConfiguration() {
        return facultyConfiguration;
    }

    @Override
    public FacultyRepository facultyRepository() {
        return facultyRepository;
    }

    @Override
    public ProfessorRepository professorRepository() {
        return professorRepository;
    }
}
