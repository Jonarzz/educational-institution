package io.github.jonarzz.edu.domain.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class FieldsOfStudyTest {

    @ParameterizedTest
    @CsvSource(value = {"''", "null"}, nullValues = "null")
    void mainFieldOfStudyCannotBeEmpty(String value) {
        assertThatThrownBy(() -> FieldsOfStudy.from(value))
                .hasMessage("Main field of study cannot be empty");
    }
}