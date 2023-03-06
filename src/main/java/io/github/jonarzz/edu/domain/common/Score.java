package io.github.jonarzz.edu.domain.common;

import static java.util.Locale.*;
import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

@ValueObject
@RequiredArgsConstructor(access = PRIVATE)
@FieldDefaults(makeFinal = true)
public class Score {

    private static final int MIN_VALUE = 0;
    private static final int MAX_PERCENTAGE = 100;

    public static final Score ZERO = new Score(MIN_VALUE);
    public static final Score MAX = fromPercentage(MAX_PERCENTAGE);

    int value;

    public static Score fromPercentage(float percentage) {
        if (percentage < MIN_VALUE) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        if (percentage > MAX_PERCENTAGE) {
            throw new IllegalArgumentException("Score cannot be greater than 100%");
        }
        return new Score((int) (percentage * 100));
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
        var percentage = (float) value / 100;
        int intOnly = (int) percentage;
        return percentage == intOnly
               ? intOnly + "%"
               : String.format(ENGLISH, "%.1f%%", percentage);
    }
}
