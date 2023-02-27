package io.github.jonarzz.edu.domain.faculty;

import java.util.*;

public interface FacultyRepository {

    Optional<FacultiesView> findByEducationalInstitutionId(UUID institutionId);

    void save(FacultiesView faculties);
}
