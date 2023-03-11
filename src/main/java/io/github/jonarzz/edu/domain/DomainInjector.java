package io.github.jonarzz.edu.domain;

import io.github.jonarzz.edu.domain.faculty.*;
import io.github.jonarzz.edu.domain.professor.*;
import io.github.jonarzz.edu.domain.student.*;

public interface DomainInjector {

    FacultyConfiguration facultyConfiguration();

    FacultyRepository facultyRepository();

    ProfessorRepository professorRepository();

    StudentRepository studentRepository();

    ProfessorConfiguration professorConfiguration();

    ProfessorResignationListener professorResignationListener();

    StudentResignationListener studentResignationListener();
}
