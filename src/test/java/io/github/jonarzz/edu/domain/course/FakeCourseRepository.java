package io.github.jonarzz.edu.domain.course;

import io.github.jonarzz.edu.domain.course.Views.CourseView;
import io.github.jonarzz.edu.domain.course.Views.FacultyCoursesView;
import io.github.jonarzz.edu.domain.faculty.FacultyId;
import io.github.jonarzz.edu.domain.test.InMemoryAggregatedEntityRepository;
import java.util.UUID;
import java.util.function.Function;

public class FakeCourseRepository extends InMemoryAggregatedEntityRepository<FacultyId, CourseView>
        implements CourseRepository {

    @Override
    public FacultyCoursesView getFacultyCourses(FacultyId facultyId) {
        return new FacultyCoursesView(getByAggregatingId(facultyId));
    }

    @Override
    public void saveNew(FacultyId facultyId, CourseView course) {
        add(facultyId, course);
    }

    @Override
    protected Function<CourseView, UUID> aggregatedEntityIdGetter() {
        return CourseView::id;
    }
}
