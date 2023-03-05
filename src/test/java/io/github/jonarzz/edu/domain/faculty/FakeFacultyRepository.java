package io.github.jonarzz.edu.domain.faculty;

import static lombok.AccessLevel.*;

import lombok.experimental.*;

import java.util.*;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FakeFacultyRepository implements FacultyRepository {

    Map<UUID, Collection<FacultyView>> facultiesByInstitutionId = new HashMap<>();

    @Override
    public FacultiesView getByEducationalInstitutionId(UUID institutionId) {
        return new FacultiesView(
                institutionId,
                facultiesByInstitutionId.getOrDefault(institutionId, Set.of())
        );
    }

    @Override
    public void create(UUID institutionId, FacultyView faculty) {
        facultiesByInstitutionId.computeIfAbsent(
                institutionId,
                id -> new HashSet<>()
        ).add(faculty);
    }
}
