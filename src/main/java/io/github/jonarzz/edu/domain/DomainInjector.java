package io.github.jonarzz.edu.domain;

import io.github.jonarzz.edu.domain.faculty.*;
import io.github.jonarzz.edu.domain.professor.*;

public interface DomainInjector {

    FacultyConfiguration facultyConfiguration();

    FacultyRepository facultyRepository();

    ProfessorRepository professorRepository();

}
