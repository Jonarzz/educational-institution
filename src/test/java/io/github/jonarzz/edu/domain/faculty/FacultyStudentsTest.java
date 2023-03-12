package io.github.jonarzz.edu.domain.faculty;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.*;

import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.faculty.CandidateForStudent.*;
import io.github.jonarzz.edu.domain.student.*;

class FacultyStudentsTest {

    static final PersonIdentification PERSONAL_DATA = new PersonIdentification("912310012A");

    @Test
    void success_scoresHigherThanMinimum() {
        var math = "math";
        var physics = "physics";
        var chemistry = "chemistry";
        var config = FakeFacultyConfiguration.builder()
                                             .minimumStudentMainScorePercent(75)
                                             .minimumStudentSecondaryScorePercent(30)
                                             .build();
        var faculty = new FacultyStudents(
                FieldsOfStudy.from(math, physics, chemistry),
                Set.of(),
                new Vacancies(30),
                config
        );
        var candidate = new CandidateForStudent(
                Set.of(new TestResult(math, Score.fromPercentage(80)),
                       new TestResult(physics, Score.fromPercentage(50)),
                       new TestResult(chemistry, Score.fromPercentage(55))),
                PERSONAL_DATA
        );

        var result = faculty.enroll(candidate);

        assertSuccess(result);
    }

    @Test
    void success_scoresEqualToMinimum() {
        var math = "math";
        var physics = "physics";
        var chemistry = "chemistry";
        var config = FakeFacultyConfiguration.builder()
                                             .minimumStudentMainScorePercent(74.9f)
                                             .minimumStudentSecondaryScorePercent(29.9f)
                                             .build();
        var faculty = new FacultyStudents(
                FieldsOfStudy.from(math, physics, chemistry),
                Set.of(),
                new Vacancies(30),
                config
        );
        var candidate = new CandidateForStudent(
                Set.of(new TestResult(math, Score.fromPercentage(75)),
                       new TestResult(physics, Score.fromPercentage(30)),
                       new TestResult(chemistry, Score.fromPercentage(31))),
                PERSONAL_DATA
        );

        var result = faculty.enroll(candidate);

        assertSuccess(result);
    }

    @ParameterizedTest(name = "{1} / 75%")
    @CsvSource({
            "74.9, 74.9%",
            "74.94, 74.9%",
            "74, 74%",
            "50.14, 50.1%",
            "50.1435, 50.1%",
            "10.9, 10.9%",
            "10.333, 10.3%",
            "0, 0%",
    })
    void failure_mainScoreLowerThanMinimum(float scorePercent, String expectedMessageValue) {
        var mainSubject = "math";
        var minimumScore = 75;
        var config = FakeFacultyConfiguration.builder()
                                             .minimumStudentMainScorePercent(minimumScore)
                                             .build();
        var faculty = new FacultyStudents(
                FieldsOfStudy.from(mainSubject),
                Set.of(),
                new Vacancies(30),
                config
        );
        var candidate = new CandidateForStudent(
                Set.of(new TestResult(mainSubject, Score.fromPercentage(scorePercent))),
                PERSONAL_DATA
        );

        var result = faculty.enroll(candidate);

        assertFailure(result, "Student main faculty score %s is lower than minimum 75%%"
                .formatted(expectedMessageValue));
    }

    @ParameterizedTest(name = "{0}% / 30%")
    @ValueSource(floats = {
            29.9f, 29.94f, 29f, 20.14f, 20.1435f, 5.9f, 5.333f, 0
    })
    void failure_secondaryScoreLowerThanMinimum(float scorePercent) {
        var mainSubject = "math";
        var physics = "physics";
        var chemistry = "chemistry";
        var minimumScore = 30;
        var config = FakeFacultyConfiguration.builder()
                                             .minimumStudentSecondaryScorePercent(minimumScore)
                                             .build();
        var faculty = new FacultyStudents(
                FieldsOfStudy.from(mainSubject, physics, chemistry),
                Set.of(),
                new Vacancies(30),
                config
        );
        var candidate = new CandidateForStudent(
                Set.of(new TestResult(mainSubject, Score.MAX),
                       new TestResult(physics, Score.fromPercentage(scorePercent)),
                       new TestResult(chemistry, Score.fromPercentage(scorePercent))),
                PERSONAL_DATA
        );

        var result = faculty.enroll(candidate);

        assertFailure(result, "Student secondary fields of study: chemistry, physics have score lower than minimum 30%");
    }

    @Test
    void rejection_noVacancy() {
        var maxVacancies = 10;
        var faculty = new FacultyStudents(
                FieldsOfStudy.from("math"),
                IntStream.rangeClosed(1, maxVacancies)
                         .mapToObj(i -> new PersonIdentification(i + "A"))
                         .toList(),
                new Vacancies(maxVacancies),
                new FakeFacultyConfiguration()
        );
        var candidate = new CandidateForStudent(Set.of(), new PersonIdentification("5811B"));

        var result = faculty.enroll(candidate);

        assertFailure(result, "There is no vacancy");
    }

    @Test
    void rejection_alreadyEnrolled() {
        var faculty = new FacultyStudents(
                FieldsOfStudy.from("math"),
                Set.of(PERSONAL_DATA),
                new Vacancies(30),
                new FakeFacultyConfiguration()
        );
        var candidate = new CandidateForStudent(Set.of(), PERSONAL_DATA);

        var result = faculty.enroll(candidate);

        assertFailure(result, "The student is already enrolled");
    }

    private static void assertSuccess(Result<StudentView> result) {
        assertThat(result)
                .as(result.toString())
                .returns(true, Result::isOk)
                .extracting(Result::getSubject)
                .returns(null, StudentView::id)
                .returns(PERSONAL_DATA, StudentView::personIdentification);
    }

    private static void assertFailure(Result<?> result, String expectedMessage) {
        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns(expectedMessage, Result::getMessage);
    }
}