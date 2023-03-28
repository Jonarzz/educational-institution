package io.github.jonarzz.edu.domain.professor;

import io.github.jonarzz.edu.domain.faculty.FacultyId;
import io.github.jonarzz.edu.domain.test.InMemoryAggregatedEntityRepository;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true)
public class FakeProfessorRepository extends InMemoryAggregatedEntityRepository<FacultyId, ProfessorView>
        implements ProfessorRepository {

    @Override
    public ProfessorView getById(UUID professorId) {
        return getByEntityId(professorId);
    }

    @Override
    public Collection<ProfessorView> getByFaculty(FacultyId facultyId) {
        return getByAggregatingId(facultyId);
    }

    @Override
    public ProfessorView getBy(FacultyId facultyId, UUID professorId) {
        return getByAggregatingId(facultyId)
                .stream()
                .filter(professor -> professor.id()
                                              .equals(professorId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Not found professor with ID %s within %s"
                                                                     .formatted(professorId, facultyId)));
    }

    @Override
    public void update(ProfessorView professor) {
        replace(professor);
    }

    @Override
    public void saveNew(FacultyId facultyId, ProfessorView professor) {
        add(facultyId, professor);
    }

    @Override
    protected Function<ProfessorView, UUID> aggregatedEntityIdGetter() {
        return ProfessorView::id;
    }
}
