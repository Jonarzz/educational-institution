package io.github.jonarzz.edu.domain.faculty;

import static java.util.UUID.*;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.faculty.CandidateForStudent.*;
import io.github.jonarzz.edu.domain.professor.*;
import io.github.jonarzz.edu.domain.student.*;

class FacultyTest {

    static final PersonIdentification PERSONAL_DATA = new PersonIdentification("912310012A");

    @Nested
    class ProfessorEmploymentTest {

        @ParameterizedTest(name = "years of experience = {0}")
        @ValueSource(ints = {5, 6, 7, 10, 20})
        void success_matchesMinConfigRequirements(int yearsOfExperience) {
            var config = FakeFacultyConfiguration.builder()
                                                 .minimumProfessorYearsOfExperience(5)
                                                 .minimumProfessorNumberOfMatchingFieldsOfStudy(2)
                                                 .build();
            var faculty = new Faculty(
                    randomUUID(),
                    "Mathematics",
                    FieldsOfStudy.from("math", "physics", "chemistry"),
                    Set.of(),
                    new Vacancies(1),
                    Set.<StudentView>of(),
                    new Vacancies(30),
                    config
            );
            var candidate = new CandidateForProfessor(
                    yearsOfExperience,
                    FieldsOfStudy.from("math", "physics"),
                    PERSONAL_DATA
            );

            var result = faculty.employ(candidate);

            assertSuccess(result);
        }

        @Test
        void success_matchesAllFieldsOfStudy() {
            var minYearsOfExperience = 5;
            var config = FakeFacultyConfiguration.builder()
                                                 .minimumProfessorYearsOfExperience(minYearsOfExperience)
                                                 .minimumProfessorNumberOfMatchingFieldsOfStudy(2)
                                                 .build();
            var fieldsOfStudy = FieldsOfStudy.from("math", "physics", "chemistry");
            var faculty = new Faculty(
                    randomUUID(),
                    "Mathematics",
                    fieldsOfStudy,
                    Set.of(),
                    new Vacancies(1),
                    Set.<StudentView>of(),
                    new Vacancies(30),
                    config
            );
            var candidate = new CandidateForProfessor(minYearsOfExperience, fieldsOfStudy, PERSONAL_DATA);

            var result = faculty.employ(candidate);

            assertSuccess(result);
        }

        @Test
        void success_facultyHasLessFieldsOfStudyThanConfigMinimumMatchingValue() {
            var minYearsOfExperience = 5;
            var config = FakeFacultyConfiguration.builder()
                                                 .minimumProfessorYearsOfExperience(minYearsOfExperience)
                                                 .minimumProfessorNumberOfMatchingFieldsOfStudy(3)
                                                 .build();
            var fieldsOfStudy = FieldsOfStudy.from("math");
            var faculty = new Faculty(
                    randomUUID(),
                    "Mathematics",
                    fieldsOfStudy,
                    Set.of(),
                    new Vacancies(1),
                    Set.<StudentView>of(),
                    new Vacancies(30),
                    config
            );
            var candidate = new CandidateForProfessor(minYearsOfExperience, fieldsOfStudy, PERSONAL_DATA);

            var result = faculty.employ(candidate);

            assertSuccess(result);
        }

        @ParameterizedTest(name = "years of experience = {0}")
        @ValueSource(ints = {1, 2, 3, 4})
        void rejection_basedOnYearsOfExperience(int yearsOfExperience) {
            var minYearsOfExperience = 5;
            var config = FakeFacultyConfiguration.builder()
                                                 .minimumProfessorYearsOfExperience(minYearsOfExperience)
                                                 .minimumProfessorNumberOfMatchingFieldsOfStudy(2)
                                                 .build();
            var fieldsOfStudy = FieldsOfStudy.from("math");
            var faculty = new Faculty(
                    randomUUID(),
                    "Mathematics",
                    fieldsOfStudy,
                    Set.of(),
                    new Vacancies(1),
                    Set.<StudentView>of(),
                    new Vacancies(30),
                    config
            );
            var candidate = new CandidateForProfessor(yearsOfExperience, fieldsOfStudy, PERSONAL_DATA);

            var result = faculty.employ(candidate);

            assertFailure(result, "Candidate has %d years of experience, while minimum required is %d"
                    .formatted(yearsOfExperience, minYearsOfExperience));
        }

        @ParameterizedTest(name = "faculty fields of study = {0}, candidate fields of study = {1}")
        @CsvSource(value = {
                "math;                                    english;             0",
                "math;                                    english, physics;    0",
                "math, physics, chemistry;                math;                1",
                "math, physics, chemistry;                math, physics;       2",
                "chemistry, biology, psychology, english; biology, psychology; 2",
                "chemistry, biology, psychology, english; chemistry, english;  2",
        }, delimiter = ';')
        void rejection_basedOnMatchingFieldsOfStudy(String facultyValues,
                                                    String candidateValues,
                                                    int matchingFieldsOfStudy) {
            var minYearsOfExperience = 5;
            var minNumberOfMatchingFieldsOfStudy = 3;
            var config = FakeFacultyConfiguration.builder()
                                                 .minimumProfessorYearsOfExperience(minYearsOfExperience)
                                                 .minimumProfessorNumberOfMatchingFieldsOfStudy(minNumberOfMatchingFieldsOfStudy)
                                                 .build();
            var faculty = new Faculty(
                    randomUUID(),
                    "Mathematics",
                    fieldsOfStudyFrom(facultyValues),
                    Set.of(),
                    new Vacancies(1),
                    Set.<StudentView>of(),
                    new Vacancies(30),
                    config
            );
            var candidate = new CandidateForProfessor(
                    minYearsOfExperience,
                    fieldsOfStudyFrom(candidateValues),
                    PERSONAL_DATA
            );

            var result = faculty.employ(candidate);

            assertFailure(result, "Candidate fields of study (%s) match %d of %d required"
                    .formatted(candidateValues,
                               matchingFieldsOfStudy,
                               minNumberOfMatchingFieldsOfStudy));
        }

        @Test
        void rejection_noVacancy() {
            var fieldsOfStudy = FieldsOfStudy.from("math");
            var faculty = new Faculty(
                    randomUUID(),
                    "Mathematics",
                    fieldsOfStudy,
                    Set.of(new ProfessorView(new PersonIdentification("1234"))),
                    new Vacancies(1),
                    Set.<StudentView>of(),
                    new Vacancies(30),
                    new FakeFacultyConfiguration()
            );
            int yearsOfExperience = 1;
            var candidate = new CandidateForProfessor(yearsOfExperience, fieldsOfStudy,
                                                      new PersonIdentification("9120"));

            var result = faculty.employ(candidate);

            assertFailure(result, "There is no vacancy");
        }

        @Test
        void rejection_alreadyEmployed() {
            var fieldsOfStudy = FieldsOfStudy.from("math");
            var faculty = new Faculty(
                    randomUUID(),
                    "Mathematics",
                    fieldsOfStudy,
                    Set.of(new ProfessorView(PERSONAL_DATA)),
                    new Vacancies(2),
                    Set.<StudentView>of(),
                    new Vacancies(30),
                    new FakeFacultyConfiguration()
            );
            int yearsOfExperience = 1;
            var candidate = new CandidateForProfessor(yearsOfExperience, fieldsOfStudy, PERSONAL_DATA);

            var result = faculty.employ(candidate);

            assertFailure(result, "The professor is already employed");
        }

        private static FieldsOfStudy fieldsOfStudyFrom(String csv) {
            var fieldsList = Arrays.stream(csv.split(","))
                                   .map(String::trim)
                                   .collect(toList());
            return FieldsOfStudy.from(
                    fieldsList.get(0),
                    fieldsList.subList(1, fieldsList.size())
            );
        }

        private static void assertSuccess(Result<ProfessorView> result) {
            assertThat(result)
                    .as(result.toString())
                    .returns(true, Result::isOk)
                    .extracting(Result::getSubject)
                    .returns(null, ProfessorView::id)
                    .returns(true, ProfessorView::active)
                    .returns(PERSONAL_DATA, ProfessorView::personIdentification);
        }
    }

    @Nested
    class StudentEnrollmentTest {

        @Test
        void success_scoresHigherThanMinimum() {
            var math = "math";
            var physics = "physics";
            var chemistry = "chemistry";
            var config = FakeFacultyConfiguration.builder()
                                                 .minimumStudentMainScorePercent(75)
                                                 .minimumStudentSecondaryScorePercent(30)
                                                 .build();
            var faculty = new Faculty(
                    randomUUID(),
                    "Mathematics",
                    FieldsOfStudy.from(math, physics, chemistry),
                    Set.<ProfessorView>of(),
                    new Vacancies(1),
                    Set.<StudentView>of(),
                    new Vacancies(30),
                    config
            );
            var candidate = new CandidateForStudent(
                    PERSONAL_DATA,
                    Set.of(new TestResult(math, Score.fromPercentage(80)),
                           new TestResult(physics, Score.fromPercentage(50)),
                           new TestResult(chemistry, Score.fromPercentage(55)))
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
            var faculty = new Faculty(
                    randomUUID(),
                    "Mathematics",
                    FieldsOfStudy.from(math, physics, chemistry),
                    Set.<ProfessorView>of(),
                    new Vacancies(1),
                    Set.<StudentView>of(),
                    new Vacancies(30),
                    config
            );
            var candidate = new CandidateForStudent(
                    PERSONAL_DATA,
                    Set.of(new TestResult(math, Score.fromPercentage(75)),
                           new TestResult(physics, Score.fromPercentage(30)),
                           new TestResult(chemistry, Score.fromPercentage(31)))
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
            var faculty = new Faculty(
                    randomUUID(),
                    "Mathematics",
                    FieldsOfStudy.from(mainSubject),
                    Set.<ProfessorView>of(),
                    new Vacancies(1),
                    Set.<StudentView>of(),
                    new Vacancies(30),
                    config
            );
            var candidate = new CandidateForStudent(
                    PERSONAL_DATA,
                    Set.of(new TestResult(mainSubject, Score.fromPercentage(scorePercent)))
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
            var faculty = new Faculty(
                    randomUUID(),
                    "Mathematics",
                    FieldsOfStudy.from(mainSubject, physics, chemistry),
                    Set.<ProfessorView>of(),
                    new Vacancies(1),
                    Set.<StudentView>of(),
                    new Vacancies(30),
                    config
            );
            var candidate = new CandidateForStudent(
                    PERSONAL_DATA,
                    Set.of(new TestResult(mainSubject, Score.MAX),
                           new TestResult(physics, Score.fromPercentage(scorePercent)),
                           new TestResult(chemistry, Score.fromPercentage(scorePercent)))
            );

            var result = faculty.enroll(candidate);

            assertFailure(result, "Student secondary fields of study: chemistry, physics have score lower than minimum 30%");
        }

        @Test
        void rejection_noVacancy() {
            var maxVacancies = 10;
            var faculty = new Faculty(
                    randomUUID(),
                    "Mathematics",
                    FieldsOfStudy.from("math"),
                    Set.of(new ProfessorView(PERSONAL_DATA)),
                    new Vacancies(1),
                    IntStream.rangeClosed(1, maxVacancies)
                             .mapToObj(i -> new PersonIdentification(i + "A"))
                             .map(StudentView::new)
                             .toList(),
                    new Vacancies(maxVacancies),
                    new FakeFacultyConfiguration()
            );
            var candidate = new CandidateForStudent(new PersonIdentification("5811B"), Set.of());

            var result = faculty.enroll(candidate);

            assertFailure(result, "There is no vacancy");
        }

        @Test
        void rejection_alreadyEnrolled() {
            var faculty = new Faculty(
                    randomUUID(),
                    "Mathematics",
                    FieldsOfStudy.from("math"),
                    Set.of(new ProfessorView(PERSONAL_DATA)),
                    new Vacancies(1),
                    Set.of(new StudentView(PERSONAL_DATA)),
                    new Vacancies(30),
                    new FakeFacultyConfiguration()
            );
            var candidate = new CandidateForStudent(PERSONAL_DATA, Set.of());

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
    }

    private static void assertFailure(Result<?> result, String expectedMessage) {
        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns(expectedMessage, Result::getMessage);
    }
}