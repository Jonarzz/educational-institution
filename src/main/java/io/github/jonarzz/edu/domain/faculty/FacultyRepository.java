package io.github.jonarzz.edu.domain.faculty;

import java.util.*;

public interface FacultyRepository {

    FacultiesView getByEducationalInstitutionId(UUID institutionId);

    void save(UUID institutionId, FacultyView faculty);
}
