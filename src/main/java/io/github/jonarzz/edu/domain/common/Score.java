package io.github.jonarzz.edu.domain.common;

import static java.util.Locale.ENGLISH;
import static lombok.AccessLevel.PRIVATE;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.ValueObject;

@ValueObject
@RequiredArgsConstructor(access = PRIVATE)
@FieldDefaults(makeFinal = true)
public class Score {

    private static final int MIN_VALUE = 0;
    private static final int MAX_PERCENTAGE = 100;

    private static final int SCALING_FACTOR = 100;

    public static final Score ZERO = new Score(MIN_VALUE);
    public static final Score MAX = fromPercentage(MAX_PERCENTAGE);

    int value;

    public static Score fromPercentage(float percentage) {
        if (percentage < MIN_VALUE) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        if (percentage > MAX_PERCENTAGE) {
            throw new IllegalArgumentException("Score cannot be greater than " + MAX_PERCENTAGE + "%");
        }
        return new Score((int) (percentage * SCALING_FACTOR));
    }

    public static Score fromPoints(int points, int maxPoints) {
        if (points > maxPoints) {
            throw new IllegalArgumentException("%s points is more than max (%s)"
                                                       .formatted(points, maxPoints));
        }
        return fromPercentage((float) points / maxPoints);
    }

    public boolean isLowerThan(Score other) {
        return value < other.value;
    }

    public String asPercentage() {
        var percentage = (float) value / SCALING_FACTOR;
        int intOnly = (int) percentage;
        return percentage == intOnly
               ? intOnly + "%"
                // TODO should depend on SCALING_FACTOR
                : String.format(ENGLISH, "%.1f%%", percentage);
    }
}
