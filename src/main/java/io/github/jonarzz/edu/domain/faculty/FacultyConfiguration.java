package io.github.jonarzz.edu.domain.faculty;

import io.github.jonarzz.edu.domain.common.*;

public interface FacultyConfiguration {

    ProfessorCandidate professorCandidate();

    StudentCandidate studentCandidate();

    interface ProfessorCandidate {

        int minimumYearsOfExperience();

        int minimumNumberOfMatchingFieldsOfStudy();
    }

    interface StudentCandidate {

        Score minimumMainFieldOfStudyScorePercentage();

        Score minimumSecondaryFieldOfStudyScorePercentage();
    }
}
