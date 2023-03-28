package io.github.jonarzz.edu.domain.course;

import io.github.jonarzz.edu.domain.course.Views.*;
import io.github.jonarzz.edu.domain.faculty.*;

public interface CourseRepository {

    FacultyCoursesView getFacultyCourses(FacultyId facultyId);

    void saveNew(FacultyId facultyId, CourseView course);
}
