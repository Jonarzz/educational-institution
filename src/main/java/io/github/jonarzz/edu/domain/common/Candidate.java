package io.github.jonarzz.edu.domain.common;

import lombok.*;

@Builder
public record Candidate(
        int yearsOfExperience,
        FieldsOfStudy fieldsOfStudy,
        PersonalData personalData
) {

}
