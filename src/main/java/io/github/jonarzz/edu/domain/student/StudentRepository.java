package io.github.jonarzz.edu.domain.student;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.domain.faculty.*;

@Repository
public interface StudentRepository {

    Collection<StudentView> getByFacultyId(FacultyId facultyId);

    void saveNew(FacultyId facultyId, StudentView student);
}
