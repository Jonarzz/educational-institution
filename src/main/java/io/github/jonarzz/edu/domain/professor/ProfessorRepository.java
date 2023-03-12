package io.github.jonarzz.edu.domain.professor;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

@Repository
public interface ProfessorRepository {

    ProfessorView getById(UUID professorId);

    Collection<ProfessorView> getByFacultyId(UUID facultyId);

    void saveNew(UUID facultyId, ProfessorView professor);

    void update(ProfessorView professor);
}
