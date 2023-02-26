package io.github.jonarzz.edu.domain.faculty;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import io.github.jonarzz.edu.domain.common.*;

@Entity
record Faculty(
        String name,
        FieldsOfStudy fieldsOfStudy
) {

}
