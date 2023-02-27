package io.github.jonarzz.edu.domain.faculty;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

@Entity
record Faculty(
        UUID id,
        String name,
        FieldsOfStudy fieldsOfStudy
) {

    Faculty(String name, FieldsOfStudy fieldsOfStudy) {
        this(null, name, fieldsOfStudy);
    }

    static Faculty fromView(FacultyView view) {
        return new Faculty(
                view.id(),
                view.name(),
                view.fieldsOfStudy()
        );
    }

    FacultyView toView() {
        return new FacultyView(
                id,
                name,
                fieldsOfStudy
        );
    }
}
