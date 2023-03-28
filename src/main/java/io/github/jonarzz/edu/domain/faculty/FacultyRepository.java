package io.github.jonarzz.edu.domain.faculty;

import io.github.jonarzz.edu.domain.faculty.Views.FacultyProfessorsView;
import io.github.jonarzz.edu.domain.faculty.Views.FacultyStudentsView;
import io.github.jonarzz.edu.domain.faculty.Views.NewFacultyView;
import java.util.Set;
import java.util.UUID;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.Repository;

@Repository
public interface FacultyRepository {

    Set<String> getAllFacultyNames(UUID institutionId);

    FacultyProfessorsView getFacultyProfessors(FacultyId facultyId);

    FacultyStudentsView getFacultyStudents(FacultyId facultyId);

    FacultyId saveNew(UUID institutionId, NewFacultyView faculty);
}
