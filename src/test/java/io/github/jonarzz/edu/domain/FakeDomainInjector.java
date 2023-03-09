package io.github.jonarzz.edu.domain;

import lombok.experimental.*;

import io.github.jonarzz.edu.domain.faculty.*;
import io.github.jonarzz.edu.domain.professor.*;
import io.github.jonarzz.edu.domain.student.*;

@FieldDefaults(makeFinal = true)
public class FakeDomainInjector implements DomainInjector {

    FacultyConfiguration facultyConfiguration = new FakeFacultyConfiguration();
    FacultyRepository facultyRepository = new FakeFacultyRepository();
    ProfessorRepository professorRepository = new FakeProfessorRepository();
    StudentRepository studentRepository = new FakeStudentRepository();
    ProfessorResignationListener professorResignationListener = new FakeProfessorResignationListener();
    StudentResignationListener studentResignationListener = new FakeStudentResignationListener();

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
    public StudentRepository studentRepository() {
        return studentRepository;
    }

    @Override
    public ProfessorResignationListener professorResignationListener() {
        return professorResignationListener;
    }

    @Override
    public StudentResignationListener studentResignationListener() {
        return studentResignationListener;
    }
}
