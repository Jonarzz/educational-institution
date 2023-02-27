package io.github.jonarzz.edu.domain.faculty;

import lombok.*;

@Builder
record FakeFacultyConfiguration(
        int minimumProfessorYearsOfExperience
) implements FacultyConfiguration {

}
