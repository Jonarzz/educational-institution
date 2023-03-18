package io.github.jonarzz.edu.domain.faculty;

import static io.github.jonarzz.edu.domain.common.TestFieldsOfStudyFactory.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import io.github.jonarzz.edu.api.result.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.professor.*;

@Nested
class FacultyProfessorsTest {

    static final PersonIdentification PERSONAL_DATA = new PersonIdentification("912310012A");

    @ParameterizedTest(name = "years of experience = {0}")
    @ValueSource(ints = {5, 6, 7, 10, 20})
    void success_matchesMinConfigRequirements(int yearsOfExperience) {
        var config = FakeFacultyConfiguration.builder()
                                             .minimumProfessorYearsOfExperience(5)
                                             .minimumProfessorNumberOfMatchingFieldsOfStudy(2)
                                             .build();
        var faculty = new FacultyProfessors(
                FieldsOfStudy.from("math", "physics", "chemistry"),
                Set.of(),
                new Vacancies(1),
                config
        );
        var candidateFieldsOfStudy = FieldsOfStudy.from("math", "physics");
        var candidate = new CandidateForProfessor(
                yearsOfExperience,
                candidateFieldsOfStudy,
                PERSONAL_DATA
        );

        var result = faculty.employ(candidate);

        assertSuccess(result, candidateFieldsOfStudy);
    }

    @Test
    void success_matchesAllFieldsOfStudy() {
        var minYearsOfExperience = 5;
        var config = FakeFacultyConfiguration.builder()
                                             .minimumProfessorYearsOfExperience(minYearsOfExperience)
                                             .minimumProfessorNumberOfMatchingFieldsOfStudy(2)
                                             .build();
        var fieldsOfStudy = FieldsOfStudy.from("math", "physics", "chemistry");
        var faculty = new FacultyProfessors(
                fieldsOfStudy,
                Set.of(),
                new Vacancies(1),
                config
        );
        var candidate = new CandidateForProfessor(minYearsOfExperience, fieldsOfStudy, PERSONAL_DATA);

        var result = faculty.employ(candidate);

        assertSuccess(result, fieldsOfStudy);
    }

    @Test
    void success_facultyHasLessFieldsOfStudyThanConfigMinimumMatchingValue() {
        var minYearsOfExperience = 5;
        var config = FakeFacultyConfiguration.builder()
                                             .minimumProfessorYearsOfExperience(minYearsOfExperience)
                                             .minimumProfessorNumberOfMatchingFieldsOfStudy(3)
                                             .build();
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var faculty = new FacultyProfessors(
                fieldsOfStudy,
                Set.of(),
                new Vacancies(1),
                config
        );
        var candidate = new CandidateForProfessor(minYearsOfExperience, fieldsOfStudy, PERSONAL_DATA);

        var result = faculty.employ(candidate);

        assertSuccess(result, fieldsOfStudy);
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
        var faculty = new FacultyProfessors(
                fieldsOfStudy,
                Set.of(),
                new Vacancies(1),
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
        var faculty = new FacultyProfessors(
                fieldsOfStudyFrom(facultyValues),
                Set.of(),
                new Vacancies(1),
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
        var faculty = new FacultyProfessors(
                fieldsOfStudy,
                Set.of(new PersonIdentification("1234")),
                new Vacancies(1),
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
        var faculty = new FacultyProfessors(
                fieldsOfStudy,
                Set.of(PERSONAL_DATA),
                new Vacancies(2),
                new FakeFacultyConfiguration()
        );
        int yearsOfExperience = 1;
        var candidate = new CandidateForProfessor(yearsOfExperience, fieldsOfStudy, PERSONAL_DATA);

        var result = faculty.employ(candidate);

        assertFailure(result, "The professor is already employed");
    }

    private static void assertSuccess(Result<ProfessorView> result,
                                      FieldsOfStudy expectedFieldsOfStudy) {
        assertThat(result)
                .as(result.toString())
                .returns(true, Result::isOk)
                .extracting(Result::getSubject)
                .returns(null, ProfessorView::id)
                .returns(true, ProfessorView::active)
                .returns(PERSONAL_DATA, ProfessorView::personIdentification)
                .returns(expectedFieldsOfStudy, ProfessorView::fieldsOfStudy);
    }

    private static void assertFailure(Result<?> result, String expectedMessage) {
        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns(expectedMessage, Result::getMessage);
    }
}
