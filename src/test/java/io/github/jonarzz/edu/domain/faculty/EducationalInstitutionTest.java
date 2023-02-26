package io.github.jonarzz.edu.domain.faculty;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.common.result.*;

class EducationalInstitutionTest {

    @Test
    void createFaculty() {
        var institution = new EducationalInstitution(new HashSet<>());
        var facultyName = "Mathematics";
        var fieldsOfStudy = new FieldsOfStudy("math");

        var result = institution.createFaculty(facultyName, fieldsOfStudy);

        assertThat(result.get())
                .returns(facultyName, Faculty::name)
                .returns(fieldsOfStudy, Faculty::fieldsOfStudy);
    }

    @Test
    void tryToCreateFacultyThatAlreadyExists() {
        var facultyName = "Mathematics";
        var institution = new EducationalInstitution(Set.of(
                new Faculty(facultyName, null)
        ));
        var fieldsOfStudy = new FieldsOfStudy("math");

        var result = institution.createFaculty(facultyName, fieldsOfStudy);

        assertThat(result.getLeft())
                .isEqualTo(RejectionReason.ALREADY_EXISTS);
    }
}