package io.github.jonarzz.edu.domain.faculty;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

@Repository
public interface FacultyRepository {

    FacultiesView getByEducationalInstitutionId(UUID institutionId);

    void create(UUID institutionId, FacultyView faculty);
}
