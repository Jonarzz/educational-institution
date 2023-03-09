package io.github.jonarzz.edu.domain.faculty;

import lombok.experimental.*;

import java.util.*;

@FieldDefaults(makeFinal = true)
public class FakeFacultyRepository implements FacultyRepository {

    Map<UUID, Collection<FacultyView>> facultiesByInstitutionId = new HashMap<>();

    @Override
    public FacultiesView getAllEducationalInstitutionFaculties(UUID institutionId) {
        return new FacultiesView(
                institutionId,
                faculties(institutionId)
        );
    }

    @Override
    public FacultyView getEducationalInstitutionFaculty(UUID institutionId, String facultyName) {
        return faculties(institutionId)
                .stream()
                .filter(faculty -> faculty.name().equals(facultyName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Not found faculty " + facultyName
                                                             + " in institution with ID " + institutionId));
    }

    @Override
    public void create(UUID institutionId, FacultyView faculty) {
        facultiesByInstitutionId.computeIfAbsent(
                institutionId,
                id -> new HashSet<>()
        ).add(faculty);
    }

    private Collection<FacultyView> faculties(UUID institutionId) {
        return facultiesByInstitutionId.getOrDefault(institutionId, Set.of());
    }
}
