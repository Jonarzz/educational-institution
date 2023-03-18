package io.github.jonarzz.edu.domain;

import lombok.experimental.*;

import io.github.jonarzz.edu.domain.course.*;
import io.github.jonarzz.edu.domain.faculty.*;
import io.github.jonarzz.edu.domain.professor.*;
import io.github.jonarzz.edu.domain.student.*;

@FieldDefaults(makeFinal = true)
public class FakeDomainInjector implements DomainInjector {

    CourseConfiguration courseConfiguration = new FakeCourseConfiguration();

    FacultyConfiguration facultyConfiguration = new FakeFacultyConfiguration();
    FacultyRepository facultyRepository = new FakeFacultyRepository();

    ProfessorRepository professorRepository = new FakeProfessorRepository();
    ProfessorConfiguration professorConfiguration = new FakeProfessorConfiguration();
    ProfessorResignationListener professorResignationListener = new FakeProfessorResignationListener();

    StudentRepository studentRepository = new FakeStudentRepository();
    StudentResignationListener studentResignationListener = new FakeStudentResignationListener();

    @Override
    public CourseConfiguration courseConfiguration() {
        return courseConfiguration;
    }

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
    public ProfessorConfiguration professorConfiguration() {
        return professorConfiguration;
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
