package io.github.jonarzz.edu.domain.faculty;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

@ValueObject
public record FacultyId(
        UUID institutionId,
        String facultyName
) {

}
