package io.github.jonarzz.edu.domain.faculty;

import java.util.*;

public record FacultiesView(
        UUID educationalInstitutionId,
        Collection<FacultyView> faculties
) {

}
