package io.github.jonarzz.edu.domain.common;
// TODO split into appropriate packages when the boundaries are clearer

import static java.util.stream.Collectors.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@ValueObject
@EqualsAndHashCode
@FieldDefaults(makeFinal = true)
public class FieldsOfStudy {

    @Getter
    String main;
    Set<String> secondary;

    private FieldsOfStudy(String main, Collection<String> secondary) {
        this.main = main;
        this.secondary = Set.copyOf(secondary);
    }

    public static FieldsOfStudy from(String main, String... secondary) {
        return from(main, Set.of(secondary));
    }

    public static FieldsOfStudy from(String main, Collection<String> secondary) {
        if (main == null || main.isEmpty()) {
            throw new IllegalArgumentException("Main field of study cannot be empty");
        }
        return new FieldsOfStudy(main, secondary);
    }

    public int count() {
        return 1 + secondary.size();
    }

    public int countMatching(FieldsOfStudy other) {
        Predicate<String> matchesMain = other.main::equals;
        Predicate<String> matchesSecondary = other.secondary::contains;
        return (int) Stream.concat(Stream.of(main),
                                   secondary.stream())
                           .filter(matchesMain.or(matchesSecondary))
                           .count();
    }

    public Collection<String> secondary() {
        return Set.copyOf(secondary);
    }

    @Override
    public String toString() {
        var delimiter = ", ";
        var secondaryString = secondary.stream()
                                       .sorted()
                                       .collect(joining(delimiter));
        if (secondaryString.isEmpty()) {
            return main;
        }
        return main + delimiter + secondaryString;
    }
}
