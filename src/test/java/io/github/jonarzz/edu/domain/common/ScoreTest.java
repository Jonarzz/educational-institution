package io.github.jonarzz.edu.domain.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ScoreTest {

    @ParameterizedTest
    @CsvSource({
            "0, 0.1",
            "1, 2",
            "1, 1.1",
            "0, 100",
            "10, 20",
            "40, 60",
            "99.9, 100"
    })
    void isLowerComparison(float lower, float greater) {
        var lowerScore = Score.fromPercentage(lower);
        var greaterScore = Score.fromPercentage(greater);

        var result = lowerScore.isLowerThan(greaterScore);

        assertThat(result)
                .isTrue();
    }

    @Test
    void isLowerComparison_equalValues() {
        var points = 33;
        var maxPoints = 100;
        var score = Score.fromPoints(points, maxPoints);
        var otherScore = Score.fromPoints(points, maxPoints);

        var result = score.isLowerThan(otherScore);

        assertThat(result)
                .isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "0,     0%",
            "0.1,   0.1%",
            "1.5,   1.5%",
            "1.333, 1.3%",
            "27.31, 27.3%",
            "59.94, 59.9%",
            "99.9,  99.9%"
    })
    void asPercentageString(float percentage, String expectedResult) {
        var score = Score.fromPercentage(percentage);

        var result = score.asPercentage();

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    @Test
    void percentageShouldNotBeNegative() {
        assertThatThrownBy(() -> Score.fromPercentage(-0.1f))
                .hasMessage("Score cannot be negative");
    }

    @Test
    void percentageMaxValueIs100() {
        assertThatThrownBy(() -> Score.fromPercentage(100.1f))
                .hasMessage("Score cannot be greater than 100%");
    }

    @Test
    void scoreCannotBeGreaterThanMaxPoints() {
        assertThatThrownBy(() -> Score.fromPoints(11, 10))
                .hasMessage("11 points is more than max (10)");
    }
}