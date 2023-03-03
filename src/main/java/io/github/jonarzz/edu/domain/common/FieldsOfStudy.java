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

    Set<String> fieldsOfStudy;

    private FieldsOfStudy(Collection<String> fieldsOfStudy) {
        this.fieldsOfStudy = Set.copyOf(fieldsOfStudy);
    }

    public static FieldsOfStudy from(String first, String... other) {
        return from(Stream.concat(Stream.of(first),
                                  Arrays.stream(other))
                          .map(String::toLowerCase)
                          .collect(toUnmodifiableSet()));
    }

    public static FieldsOfStudy from(Collection<String> fieldsOfStudy) {
        if (fieldsOfStudy.isEmpty()) {
            throw new IllegalArgumentException("At least one field of study required");
        }
        return new FieldsOfStudy(fieldsOfStudy);
    }

    public FieldsOfStudy matching(FieldsOfStudy other) {
        return new FieldsOfStudy(
                other.fieldsOfStudy
                        .stream()
                        .filter(fieldsOfStudy::contains)
                        .collect(toSet())
        );
    }

    public int count() {
        return fieldsOfStudy.size();
    }

    @Override
    public String toString() {
        return fieldsOfStudy.stream()
                            .sorted()
                            .collect(joining(", "));
    }
}
