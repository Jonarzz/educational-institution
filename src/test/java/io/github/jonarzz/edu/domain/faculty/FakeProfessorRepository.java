package io.github.jonarzz.edu.domain.faculty;

import java.util.*;

import io.github.jonarzz.edu.domain.professor.*;

class FakeProfessorRepository implements ProfessorRepository {

    Map<UUID, Collection<ProfessorView>> professorsByFacultyId = new HashMap<>();

    @Override
    public Collection<ProfessorView> getByFacultyId(UUID facultyId) {
        return professorsByFacultyId.getOrDefault(facultyId, Set.of());
    }

    @Override
    public void save(UUID facultyId, ProfessorView professor) {
        professorsByFacultyId.computeIfAbsent(
                facultyId,
                id -> new HashSet<>()
        ).add(professor);
    }
}
