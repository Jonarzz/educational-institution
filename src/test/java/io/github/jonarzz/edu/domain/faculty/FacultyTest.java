package io.github.jonarzz.edu.domain.faculty;

import static java.util.UUID.*;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.InstanceOfAssertFactories.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.professor.*;

class FacultyTest {

    static final PersonIdentification PERSONAL_DATA = new PersonIdentification("912310012A");

    @ParameterizedTest(name = "years of experience = {0}")
    @ValueSource(ints = {5, 6, 7, 10, 20})
    void employCandidateAsProfessor(int yearsOfExperience) {
        var minYearsOfExperience = 5;
        var minNumberOfMatchingFieldsOfStudy = 2;
        var config = new FakeFacultyConfiguration(
                minYearsOfExperience,
                minNumberOfMatchingFieldsOfStudy
        );
        var faculty = new Faculty(
                randomUUID(),
                "Mathematics",
                FieldsOfStudy.from("math", "physics", "chemistry"),
                Set.of(),
                new Vacancies(1),
                config
        );
        var candidate = new Candidate(
                yearsOfExperience,
                FieldsOfStudy.from("math", "physics"),
                PERSONAL_DATA
        );

        var result = faculty.employ(candidate);

        assertSuccess(result);
    }

    @Test
    void employCandidateAsProfessor_matchesAllFieldsOfStudy() {
        var minYearsOfExperience = 5;
        var minNumberOfMatchingFieldsOfStudy = 2;
        var config = new FakeFacultyConfiguration(
                minYearsOfExperience,
                minNumberOfMatchingFieldsOfStudy
        );
        var fieldsOfStudy = FieldsOfStudy.from("math", "physics", "chemistry");
        var faculty = new Faculty(
                randomUUID(),
                "Mathematics",
                fieldsOfStudy,
                Set.of(),
                new Vacancies(1),
                config
        );
        var candidate = new Candidate(minYearsOfExperience, fieldsOfStudy, PERSONAL_DATA);

        var result = faculty.employ(candidate);

        assertSuccess(result);
    }

    @Test
    void employCandidateAsProfessor_lessThanMinMatchingFieldsOfStudyTotal() {
        var minYearsOfExperience = 5;
        var minNumberOfMatchingFieldsOfStudy = 3;
        var config = new FakeFacultyConfiguration(
                minYearsOfExperience,
                minNumberOfMatchingFieldsOfStudy
        );
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var faculty = new Faculty(
                randomUUID(),
                "Mathematics",
                fieldsOfStudy,
                Set.of(),
                new Vacancies(1),
                config
        );
        var candidate = new Candidate(minYearsOfExperience, fieldsOfStudy, PERSONAL_DATA);

        var result = faculty.employ(candidate);

        assertSuccess(result);
    }

    @ParameterizedTest(name = "years of experience = {0}")
    @ValueSource(ints = {1, 2, 3, 4})
    void rejectCandidateForProfessor_basedOnYearsOfExperience(int yearsOfExperience) {
        var minYearsOfExperience = 5;
        var minNumberOfMatchingFieldsOfStudy = 2;
        var config = new FakeFacultyConfiguration(
                minYearsOfExperience,
                minNumberOfMatchingFieldsOfStudy
        );
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var faculty = new Faculty(
                randomUUID(),
                "Mathematics",
                fieldsOfStudy,
                Set.of(),
                new Vacancies(1),
                config
        );
        var candidate = new Candidate(yearsOfExperience, fieldsOfStudy, PERSONAL_DATA);

        var result = faculty.employ(candidate);

        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns("Candidate has %d years of experience, while minimum required is %d"
                                 .formatted(yearsOfExperience, minYearsOfExperience),
                         Result::getMessage);
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
    void rejectCandidateForProfessor_basedOnMatchingFieldsOfStudy(String facultyValues,
                                                                  String candidateValues,
                                                                  int matchingFieldsOfStudy) {
        var minYearsOfExperience = 5;
        var minNumberOfMatchingFieldsOfStudy = 3;
        var config = new FakeFacultyConfiguration(
                minYearsOfExperience,
                minNumberOfMatchingFieldsOfStudy
        );
        var faculty = new Faculty(
                randomUUID(),
                "Mathematics",
                fieldsOfStudyFrom(facultyValues),
                Set.of(),
                new Vacancies(1),
                config
        );
        var candidate = new Candidate(
                minYearsOfExperience,
                fieldsOfStudyFrom(candidateValues),
                PERSONAL_DATA
        );

        var result = faculty.employ(candidate);

        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns("Candidate fields of study (%s) match %d of %d required"
                                 .formatted(candidateValues,
                                            matchingFieldsOfStudy,
                                            minNumberOfMatchingFieldsOfStudy),
                         Result::getMessage);
    }

    @Test
    void rejectCandidateForProfessor_noVacancy() {
        var minYearsOfExperience = 5;
        var minNumberOfMatchingFieldsOfStudy = 2;
        var config = new FakeFacultyConfiguration(
                minYearsOfExperience,
                minNumberOfMatchingFieldsOfStudy
        );
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var faculty = new Faculty(
                randomUUID(),
                "Mathematics",
                fieldsOfStudy,
                Set.of(new ProfessorView(new PersonIdentification("1234"))),
                new Vacancies(1),
                config
        );
        int yearsOfExperience = 10;
        var candidate = new Candidate(yearsOfExperience, fieldsOfStudy,
                                      new PersonIdentification("9120"));

        var result = faculty.employ(candidate);

        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns("There is no vacancy", Result::getMessage);
    }

    @Test
    void rejectCandidateForProfessor_alreadyEmployed() {
        var minYearsOfExperience = 5;
        var minNumberOfMatchingFieldsOfStudy = 2;
        var config = new FakeFacultyConfiguration(
                minYearsOfExperience,
                minNumberOfMatchingFieldsOfStudy
        );
        var fieldsOfStudy = FieldsOfStudy.from("math");
        var faculty = new Faculty(
                randomUUID(),
                "Mathematics",
                fieldsOfStudy,
                Set.of(new ProfessorView(PERSONAL_DATA)),
                new Vacancies(2),
                config
        );
        int yearsOfExperience = 10;
        var candidate = new Candidate(yearsOfExperience, fieldsOfStudy, PERSONAL_DATA);

        var result = faculty.employ(candidate);

        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns("The professor is already employed", Result::getMessage);
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
                .extracting(Result::getSubject, optional(ProfessorView.class))
                .get()
                .returns(null, ProfessorView::id);
    }
}