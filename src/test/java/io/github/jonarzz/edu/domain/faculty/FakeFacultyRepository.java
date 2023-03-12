package io.github.jonarzz.edu.domain.faculty;

import static io.github.jonarzz.edu.domain.faculty.FakeFacultyRepository.*;
import static io.github.jonarzz.edu.domain.faculty.Views.*;
import static java.util.stream.Collectors.*;

import lombok.experimental.*;

import java.util.*;
import java.util.function.*;

import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.test.*;

@FieldDefaults(makeFinal = true)
public class FakeFacultyRepository extends InMemoryAggregatedEntityRepository<FacultyEntity>
        implements FacultyRepository {

    @Override
    public Set<String> getAllFacultyNames(UUID institutionId) {
        return getByAggregatingId(institutionId)
                .stream()
                .map(FacultyEntity::name)
                .collect(toSet());
    }

    @Override
    public FacultyProfessorsView getFacultyProfessors(UUID institutionId, String facultyName) {
        var faculty = firstMatching(institutionId, facultyName);
        return new FacultyProfessorsView(
                faculty.id(),
                faculty.fieldsOfStudy(),
                Set.of(),
                faculty.maxProfessorVacancies()
        );
    }

    @Override
    public FacultyStudentsView getFacultyStudents(UUID institutionId, String facultyName) {
        var faculty = firstMatching(institutionId, facultyName);
        return new FacultyStudentsView(
                faculty.id(),
                faculty.fieldsOfStudy(),
                Set.of(),
                faculty.maxStudentVacancies()
        );
    }

    @Override
    public UUID saveNew(UUID institutionId, NewFacultyView faculty) {
        var facultyId = UUID.randomUUID();
        add(institutionId, new FacultyEntity(
                facultyId,
                faculty.name(),
                faculty.fieldsOfStudy(),
                faculty.maxProfessorVacancies(),
                faculty.maxStudentVacancies()
        ));
        return facultyId;
    }

    @Override
    protected Function<FacultyEntity, UUID> aggregatedEntityIdGetter() {
        return FacultyEntity::id;
    }

    private FacultyEntity firstMatching(UUID institutionId, String facultyName) {
        return firstMatching(institutionId, faculty -> faculty.name()
                                                              .equals(facultyName));
    }

    record FacultyEntity(
            UUID id,
            String name,
            FieldsOfStudy fieldsOfStudy,
            Vacancies maxProfessorVacancies,
            Vacancies maxStudentVacancies
    ) {

    }
}
