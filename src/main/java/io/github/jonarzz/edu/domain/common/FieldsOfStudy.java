package io.github.jonarzz.edu.domain.common;

import static java.util.stream.Collectors.*;
import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;
import java.util.stream.*;

@ValueObject
@EqualsAndHashCode
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FieldsOfStudy {

    Set<String> names;

    private FieldsOfStudy(Collection<String> names) {
        this.names = Set.copyOf(names);
    }

    public static FieldsOfStudy from(String firstName, String... otherNames) {
        return from(Stream.concat(Stream.of(firstName),
                                  Arrays.stream(otherNames))
                          .map(String::toLowerCase)
                          .collect(toUnmodifiableSet()));
    }

    public static FieldsOfStudy from(Collection<String> names) {
        if (names.isEmpty()) {
            throw new IllegalArgumentException("At least one field of study required");
        }
        return new FieldsOfStudy(names);
    }

    public FieldsOfStudy matching(FieldsOfStudy other) {
        return new FieldsOfStudy(
                other.names.stream()
                           .filter(names::contains)
                           .collect(toSet())
        );
    }

    public int count() {
        return names.size();
    }

    @Override
    public String toString() {
        return names.stream()
                    .sorted()
                    .collect(joining(", "));
    }
}
