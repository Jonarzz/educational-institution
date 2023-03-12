package io.github.jonarzz.edu.domain.professor;

import lombok.experimental.*;

import java.util.*;
import java.util.function.*;

import io.github.jonarzz.edu.domain.test.*;

@FieldDefaults(makeFinal = true)
public class FakeProfessorRepository extends InMemoryAggregatedEntityRepository<ProfessorView>
        implements ProfessorRepository {

    @Override
    public ProfessorView getById(UUID professorId) {
        return getByEntityId(professorId);
    }

    @Override
    public Collection<ProfessorView> getByFacultyId(UUID facultyId) {
        return getByAggregatingId(facultyId);
    }

    @Override
    public void update(ProfessorView professor) {
        replace(professor);
    }

    @Override
    public void saveNew(UUID facultyId, ProfessorView professor) {
        add(facultyId, professor);
    }

    @Override
    protected Function<ProfessorView, UUID> aggregatedEntityIdGetter() {
        return ProfessorView::id;
    }
}
