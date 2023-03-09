package io.github.jonarzz.edu.domain.faculty;

import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.professor.*;
import io.github.jonarzz.edu.domain.student.*;

@ValueObject
public record FacultyView( // TODO move to interface?
        UUID id,
        String name,
        FieldsOfStudy fieldsOfStudy,
        Collection<ProfessorView> employedProfessors,
        Vacancies maxProfessorVacancies,
        Collection<StudentView> enrolledStudents,
        Vacancies maxStudentVacancies
) {

    Faculty toDomainObject(FacultyConfiguration config) {
        return new Faculty(
                id,
                name,
                fieldsOfStudy,
                employedProfessors,
                maxProfessorVacancies,
                enrolledStudents,
                maxStudentVacancies,
                config
        );
    }

}
