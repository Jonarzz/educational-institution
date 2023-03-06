package io.github.jonarzz.edu.domain.faculty;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import io.github.jonarzz.edu.domain.common.*;

@ValueObject
public record CandidateForProfessor(
        int yearsOfExperience,
        FieldsOfStudy fieldsOfStudy,
        PersonIdentification personIdentification
) implements Candidate {

}
