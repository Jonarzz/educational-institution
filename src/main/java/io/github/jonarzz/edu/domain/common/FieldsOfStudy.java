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

    public FieldsOfStudy(String first, String... other) {
        this(Stream.concat(Stream.of(first),
                           Arrays.stream(other))
                   .collect(toUnmodifiableSet()));
    }

    public FieldsOfStudy(Collection<String> fieldsOfStudy) {
        if (fieldsOfStudy.isEmpty()) {
            throw new IllegalArgumentException("At least one field of study required");
        }
        this.fieldsOfStudy = Set.copyOf(fieldsOfStudy);
    }

    public Collection<String> names() {
        return fieldsOfStudy;
    }
}
