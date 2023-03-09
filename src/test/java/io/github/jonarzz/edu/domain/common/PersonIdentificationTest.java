package io.github.jonarzz.edu.domain.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class PersonIdentificationTest {

    @ParameterizedTest
    @CsvSource(value = {"''", "null"}, nullValues = "null")
    void nationalIdNumberCannotBeEmpty(String value) {
        assertThatThrownBy(() -> new PersonIdentification(value))
                .hasMessage("National ID number cannot be empty");
    }
}