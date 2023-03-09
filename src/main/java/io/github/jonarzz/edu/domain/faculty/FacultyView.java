package io.github.jonarzz.edu.domain.faculty;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.professor.*;
import io.github.jonarzz.edu.domain.student.*;

@ValueObject
public record FacultyView(
        UUID id,
        String name,
        FieldsOfStudy fieldsOfStudy,
        Collection<ProfessorView> employedProfessors,
        Vacancies maxProfessorVacancies,
        Collection<StudentView> enrolledStudents,
        Vacancies maxStudentVacancies
) {

    static FacultyView newFaculty(
            String name, FieldsOfStudy fieldsOfStudy,
            Vacancies maxProfessorVacancies, Vacancies maxStudentVacancies) {
        return new FacultyView(
                null,
                name,
                fieldsOfStudy,
                Set.of(),
                maxProfessorVacancies,
                Set.of(),
                maxStudentVacancies
        );
    }

    FacultyProfessors professorsDomainObject(FacultyConfiguration config) {
        return new FacultyProfessors(
                fieldsOfStudy,
                employedProfessors,
                maxProfessorVacancies,
                config
        );
    }

    FacultyStudents studentsDomainObject(FacultyConfiguration config) {
        return new FacultyStudents(
                fieldsOfStudy,
                enrolledStudents,
                maxStudentVacancies,
                config
        );
    }

}
