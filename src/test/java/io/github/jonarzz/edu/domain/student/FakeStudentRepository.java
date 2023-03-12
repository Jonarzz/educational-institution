package io.github.jonarzz.edu.domain.student;

import lombok.experimental.*;

import java.util.*;
import java.util.function.*;

import io.github.jonarzz.edu.domain.test.*;

@FieldDefaults(makeFinal = true)
public class FakeStudentRepository  extends InMemoryAggregatedEntityRepository<StudentView>
        implements StudentRepository {

    @Override
    public Collection<StudentView> getByFacultyId(UUID facultyId) {
        return getByAggregatingId(facultyId);
    }

    @Override
    public void saveNew(UUID facultyId, StudentView student) {
        add(facultyId, student);
    }

    @Override
    protected Function<StudentView, UUID> aggregatedEntityIdGetter() {
        return StudentView::id;
    }
}
