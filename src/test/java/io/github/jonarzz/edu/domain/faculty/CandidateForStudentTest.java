package io.github.jonarzz.edu.domain.faculty;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;
import io.github.jonarzz.edu.domain.faculty.CandidateForStudent.*;

class CandidateForStudentTest {

    @Test
    void xxx() {
        var fieldOfStudy = "test";
        var candidateTestResults = Set.of(
                new TestResult(fieldOfStudy, Score.fromPercentage(10)),
                new TestResult(fieldOfStudy, Score.fromPercentage(50))
        );
        var candidateIdentification = new PersonIdentification("1291A412C");

        assertThatThrownBy(() -> new CandidateForStudent(candidateTestResults, candidateIdentification))
                .hasMessage("Found more than one test result for: " + fieldOfStudy);
    }
}