package io.github.jonarzz.edu.domain.faculty;

import io.github.jonarzz.edu.api.*;

public interface FacultyInjector extends Injector {

    FacultyConfiguration facultiesConfiguration();

    FacultyRepository educationalInstitutionRepository();

}
