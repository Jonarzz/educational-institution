package io.github.jonarzz.edu.domain.common;
// TODO split into appropriate packages when the boundaries are clearer

import static java.util.function.Predicate.*;
import static java.util.stream.Collectors.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;
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

    public Collection<String> secondary() {
        return Set.copyOf(secondary);
    }

    public int count() {
        return 1 + secondary.size();
    }

    public int countMatching(FieldsOfStudy other) {
        return (int) asStream(other)
                .filter(this::matchesAny)
                .count();
    }

    public List<String> diff(FieldsOfStudy other) {
        return asStream(this)
                .filter(not(other::matchesAny))
                .sorted()
                .toList();
    }

    @Override
    public String toString() {
        if (secondary.isEmpty()) {
            return main;
        }
        var delimiter = ", ";
        var secondaryString = secondary.stream()
                                       .sorted()
                                       .collect(joining(delimiter));
        return main + delimiter + secondaryString;
    }

    private boolean matchesAny(String value) {
        return main.equals(value) || secondary.contains(value);
    }

    private static Stream<String> asStream(FieldsOfStudy other) {
        return Stream.concat(
                Stream.of(other.main),
                other.secondary.stream()
        );
    }
}
