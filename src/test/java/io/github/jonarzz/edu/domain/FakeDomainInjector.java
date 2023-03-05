package io.github.jonarzz.edu.domain;

import lombok.experimental.*;

import io.github.jonarzz.edu.domain.faculty.*;
import io.github.jonarzz.edu.domain.professor.*;

@FieldDefaults(makeFinal = true)
public class FakeDomainInjector implements DomainInjector {

    FacultyConfiguration facultyConfiguration = new FakeFacultyConfiguration();
    FacultyRepository facultyRepository = new FakeFacultyRepository();
    ProfessorRepository professorRepository = new FakeProfessorRepository();
    ProfessorResignationListener professorResignationListener = new FakeProfessorResignationListener();

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

    @Override
    public ProfessorResignationListener professorResignationListener() {
        return professorResignationListener;
    }
}
