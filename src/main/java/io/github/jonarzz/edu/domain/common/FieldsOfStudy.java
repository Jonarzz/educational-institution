package io.github.jonarzz.edu.domain.common;
// TODO split into appropriate packages when the boundaries are clearer

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.ValueObject;

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
        return (int) other.asStream()
                          .filter(this::matchesAny)
                          .count();
    }

    public List<String> diff(FieldsOfStudy other) {
        return asStream()
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

    private Stream<String> asStream() {
        return Stream.concat(
                Stream.of(main),
                secondary.stream()
        );
    }
}
