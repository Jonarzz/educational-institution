package io.github.jonarzz.edu.domain.professor;

import java.util.*;

public interface ProfessorRepository {

    void save(UUID facultyId, ProfessorView professor);
}
