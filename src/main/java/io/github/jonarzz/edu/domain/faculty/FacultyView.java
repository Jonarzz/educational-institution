package io.github.jonarzz.edu.domain.faculty;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

public record FacultyView(
        UUID id,
        String name,
        FieldsOfStudy fieldsOfStudy
) {

}
