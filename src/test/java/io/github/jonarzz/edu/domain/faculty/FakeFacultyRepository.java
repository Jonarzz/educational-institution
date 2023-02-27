package io.github.jonarzz.edu.domain.faculty;

import static lombok.AccessLevel.*;

import lombok.experimental.*;

import java.util.*;

@FieldDefaults(level = PRIVATE, makeFinal = true)
class FakeFacultyRepository implements FacultyRepository {

    Map<UUID, FacultiesView> institutionsById = new HashMap<>();

    @Override
    public Optional<FacultiesView> findByEducationalInstitutionId(UUID institutionId) {
        return Optional.ofNullable(institutionsById.get(institutionId));
    }

    @Override
    public void save(FacultiesView faculties) {
        institutionsById.put(
                faculties.educationalInstitutionId(),
                faculties
        );
    }
}
