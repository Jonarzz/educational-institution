package io.github.jonarzz.edu.domain.common;
// TODO split into appropriate packages when the boundaries are clearer

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

@ValueObject
public record Candidate(
        int yearsOfExperience,
        FieldsOfStudy fieldsOfStudy,
        PersonIdentification personIdentification
) {

}
