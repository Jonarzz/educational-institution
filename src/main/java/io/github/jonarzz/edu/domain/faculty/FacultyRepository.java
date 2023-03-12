package io.github.jonarzz.edu.domain.faculty;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.domain.faculty.Views.*;

@Repository
public interface FacultyRepository {

    Set<String> getAllFacultyNames(UUID institutionId);

    FacultyProfessorsView getFacultyProfessors(UUID institutionId, String facultyName);

    FacultyStudentsView getFacultyStudents(UUID institutionId, String facultyName);

    UUID saveNew(UUID institutionId, NewFacultyView faculty);
}
