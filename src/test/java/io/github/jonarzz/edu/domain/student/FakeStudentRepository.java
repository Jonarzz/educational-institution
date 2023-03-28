package io.github.jonarzz.edu.domain.student;

import io.github.jonarzz.edu.domain.faculty.FacultyId;
import io.github.jonarzz.edu.domain.test.InMemoryAggregatedEntityRepository;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true)
public class FakeStudentRepository extends InMemoryAggregatedEntityRepository<FacultyId, StudentView>
        implements StudentRepository {

    @Override
    public Collection<StudentView> getByFacultyId(FacultyId facultyId) {
        return getByAggregatingId(facultyId);
    }

    @Override
    public void saveNew(FacultyId facultyId, StudentView student) {
        add(facultyId, student);
    }

    @Override
    protected Function<StudentView, UUID> aggregatedEntityIdGetter() {
        return StudentView::id;
    }
}
