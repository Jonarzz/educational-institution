package io.github.jonarzz.edu.domain.student;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

@Repository
public interface StudentRepository {

    Collection<StudentView> getByFacultyId(UUID facultyId);

    void create(UUID facultyId, StudentView student);
}
