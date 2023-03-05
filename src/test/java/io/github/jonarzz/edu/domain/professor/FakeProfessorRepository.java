package io.github.jonarzz.edu.domain.professor;

import java.util.*;

public class FakeProfessorRepository implements ProfessorRepository {

    Map<UUID, Collection<ProfessorView>> professorsByFacultyId = new HashMap<>();

    @Override
    public ProfessorView getById(UUID professorId) {
        return professorsByFacultyId.values()
                                    .stream()
                                    .flatMap(Collection::stream)
                                    .filter(professor -> professorId.equals(professor.id()))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalStateException("Not found professor with ID "
                                                                                 + professorId));
    }

    @Override
    public Collection<ProfessorView> getByFacultyId(UUID facultyId) {
        return professorsByFacultyId.getOrDefault(facultyId, Set.of());
    }

    @Override
    public void update(ProfessorView professor) {
        for (var profs : professorsByFacultyId.values()) {
            for (var prof : profs) {
                if (prof.equals(professor)) {
                    profs.remove(prof);
                    profs.add(professor);
                    return;
                }
            }
        }
    }

    @Override
    public void create(UUID facultyId, ProfessorView professor) {
        professorsByFacultyId.computeIfAbsent(
                facultyId,
                id -> new HashSet<>()
        ).add(professor);
    }
}
