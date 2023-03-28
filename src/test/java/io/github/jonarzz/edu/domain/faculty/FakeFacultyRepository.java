package io.github.jonarzz.edu.domain.faculty;

import static io.github.jonarzz.edu.domain.faculty.FakeFacultyRepository.FacultyEntity;
import static io.github.jonarzz.edu.domain.faculty.Views.FacultyProfessorsView;
import static io.github.jonarzz.edu.domain.faculty.Views.FacultyStudentsView;
import static io.github.jonarzz.edu.domain.faculty.Views.NewFacultyView;
import static java.util.stream.Collectors.toSet;

import io.github.jonarzz.edu.domain.common.FieldsOfStudy;
import io.github.jonarzz.edu.domain.common.Vacancies;
import io.github.jonarzz.edu.domain.test.InMemoryAggregatedEntityRepository;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true)
public class FakeFacultyRepository extends InMemoryAggregatedEntityRepository<UUID, FacultyEntity>
        implements FacultyRepository {

    @Override
    public Set<String> getAllFacultyNames(UUID institutionId) {
        return getByAggregatingId(institutionId)
                .stream()
                .map(FacultyEntity::name)
                .collect(toSet());
    }

    @Override
    public FacultyProfessorsView getFacultyProfessors(FacultyId facultyId) {
        var faculty = firstMatching(facultyId);
        return new FacultyProfessorsView(
                facultyId,
                faculty.fieldsOfStudy(),
                Set.of(),
                faculty.maxProfessorVacancies()
        );
    }

    @Override
    public FacultyStudentsView getFacultyStudents(FacultyId facultyId) {
        var faculty = firstMatching(facultyId);
        return new FacultyStudentsView(
                facultyId,
                faculty.fieldsOfStudy(),
                Set.of(),
                faculty.maxStudentVacancies()
        );
    }

    @Override
    public FacultyId saveNew(UUID institutionId, NewFacultyView faculty) {
        add(institutionId, new FacultyEntity(
                UUID.randomUUID(),
                faculty.name(),
                faculty.fieldsOfStudy(),
                faculty.maxProfessorVacancies(),
                faculty.maxStudentVacancies()
        ));
        return new FacultyId(institutionId, faculty.name());
    }

    @Override
    protected Function<FacultyEntity, UUID> aggregatedEntityIdGetter() {
        return FacultyEntity::id;
    }

    private FacultyEntity firstMatching(FacultyId facultyId) {
        return firstMatching(facultyId.institutionId(),
                             faculty -> faculty.name()
                                               .equals(facultyId.facultyName()));
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
