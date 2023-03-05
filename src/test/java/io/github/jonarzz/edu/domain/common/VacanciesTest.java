package io.github.jonarzz.edu.domain.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class VacanciesTest {

    @ParameterizedTest
    @ValueSource(ints = {-5, -1, 0})
    void vacanciesShouldBeGreaterThan0(int value) {
        assertThatThrownBy(() -> new Vacancies(value))
                .hasMessage("Vacancies count should be greater than 0");
    }
}