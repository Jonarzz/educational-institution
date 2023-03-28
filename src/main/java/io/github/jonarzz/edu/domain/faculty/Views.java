package io.github.jonarzz.edu.domain.faculty;

import static java.util.stream.Collectors.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.professor.*;
import io.github.jonarzz.edu.domain.student.*;

public class Views {

    private Views() {
    }

    public record NewFacultyView(
            String name,
            FieldsOfStudy fieldsOfStudy,
            Vacancies maxProfessorVacancies,
            Vacancies maxStudentVacancies
    ) {
    }

    public record FacultyProfessorsView(
            FacultyId facultyId,
            FieldsOfStudy fieldsOfStudy,
            Collection<ProfessorView> employedProfessors,
            Vacancies maxProfessorVacancies
    ) {

        FacultyProfessors toDomainObject(FacultyConfiguration config) {
            return new FacultyProfessors(
                    fieldsOfStudy,
                    employedProfessors.stream()
                                      .map(ProfessorView::personIdentification)
                                      .collect(toSet()),
                    maxProfessorVacancies,
                    config
            );
        }
    }

    public record FacultyStudentsView(
            FacultyId facultyId,
            FieldsOfStudy fieldsOfStudy,
            Collection<StudentView> enrolledStudents,
            Vacancies maxStudentVacancies
    ) {

        FacultyStudents toDomainObject(FacultyConfiguration config) {
            return new FacultyStudents(
                    fieldsOfStudy,
                    enrolledStudents.stream()
                                    .map(StudentView::personIdentification)
                                    .collect(toSet()),
                    maxStudentVacancies,
                    config
            );
        }
    }
}
