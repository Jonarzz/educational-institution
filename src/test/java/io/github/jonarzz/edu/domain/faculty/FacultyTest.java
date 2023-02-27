package io.github.jonarzz.edu.domain.faculty;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.InstanceOfAssertFactories.*;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import io.github.jonarzz.edu.api.*;
import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.professor.*;

class FacultyTest {

    @ParameterizedTest(name = "years of experience = {0}")
    @ValueSource(ints = {5, 6, 7, 10, 20})
    void employCandidateAsProfessor(int yearsOfExperience) {
        var config = FakeFacultyConfiguration.builder()
                                             .minimumProfessorYearsOfExperience(5)
                                             .build();
        var faculty = new Faculty("Mathematics", new FieldsOfStudy("math"))
                .withConfiguration(config);
        var candidate = new Candidate(
                yearsOfExperience
        );

        var result = faculty.employ(candidate);

        assertThat(result)
                .as(result.toString())
                .returns(true, Result::isOk)
                .extracting(Result::getSubject, optional(ProfessorView.class))
                .get()
                .returns(null, ProfessorView::id);
    }

    @ParameterizedTest(name = "years of experience = {0}")
    @ValueSource(ints = {1, 2, 3, 4})
    void rejectCandidateForProfessorBasedOnYearsOfExperience(int yearsOfExperience) {
        var minYearsOfExperience = 5;
        var config = new FakeFacultyConfiguration(
                minYearsOfExperience
        );
        var faculty = new Faculty("Mathematics", new FieldsOfStudy("math"))
                .withConfiguration(config);
        var candidate = new Candidate(
                yearsOfExperience
        );

        var result = faculty.employ(candidate);

        assertThat(result)
                .as(result.toString())
                .returns(false, Result::isOk)
                .returns("Candidate has %d years of experience, while minimum required is %d"
                                 .formatted(yearsOfExperience, minYearsOfExperience),
                         Result::getMessage);
    }
}