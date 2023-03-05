package io.github.jonarzz.edu.domain.professor;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

@Repository
public interface ProfessorRepository {

    Collection<ProfessorView> getByFacultyId(UUID facultyId);

    void save(UUID facultyId, ProfessorView professor);
}
