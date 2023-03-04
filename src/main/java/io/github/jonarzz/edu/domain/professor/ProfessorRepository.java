package io.github.jonarzz.edu.domain.professor;

import java.util.*;

public interface ProfessorRepository {

    Collection<ProfessorView> getByFacultyId(UUID facultyId);

    void save(UUID facultyId, ProfessorView professor);
}
