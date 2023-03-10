package io.github.jonarzz.edu.domain.faculty;

import lombok.*;
import lombok.experimental.*;

import io.github.jonarzz.edu.domain.common.*;

@Builder
@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public final class FakeFacultyConfiguration implements FacultyConfiguration {

    static final int DEFAULT_MIN_PROF_YEARS_OF_EXPERIENCE = 5;
    static final int DEFAULT_PROFESSOR_MIN_NUMBER_OF_MATCHING_FIELDS_OF_STUDY = 1;

    static final int DEFAULT_MIN_STUDENT_MAIN_SCORE_PERCENT = 50;
    static final int DEFAULT_MIN_STUDENT_SECONDARY_SCORE_PERCENT = 30;

    int minimumProfessorYearsOfExperience;
    int minimumProfessorNumberOfMatchingFieldsOfStudy;

    float minimumStudentMainScorePercent;
    float minimumStudentSecondaryScorePercent;

    public FakeFacultyConfiguration() {
        this(DEFAULT_MIN_PROF_YEARS_OF_EXPERIENCE,
             DEFAULT_PROFESSOR_MIN_NUMBER_OF_MATCHING_FIELDS_OF_STUDY,
             DEFAULT_MIN_STUDENT_MAIN_SCORE_PERCENT,
             DEFAULT_MIN_STUDENT_SECONDARY_SCORE_PERCENT);
    }

    @Override
    public ProfessorCandidate professorCandidate() {
        return new ProfessorCandidate() {
            @Override
            public int minimumYearsOfExperience() {
                return minimumProfessorYearsOfExperience;
            }

            @Override
            public int minimumNumberOfMatchingFieldsOfStudy() {
                return minimumProfessorNumberOfMatchingFieldsOfStudy;
            }
        };
    }

    @Override
    public StudentCandidate studentCandidate() {
        return new StudentCandidate() {
            @Override
            public Score mainFieldOfStudyMinimumScorePercentage() {
                return Score.fromPercentage(minimumStudentMainScorePercent);
            }

            @Override
            public Score secondaryFieldOfStudyMinimumScorePercentage() {
                return Score.fromPercentage(minimumStudentSecondaryScorePercent);
            }
        };
    }
}
