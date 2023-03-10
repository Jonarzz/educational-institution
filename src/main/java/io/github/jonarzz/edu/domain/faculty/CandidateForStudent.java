package io.github.jonarzz.edu.domain.faculty;

import static java.util.stream.Collectors.*;
import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import java.util.*;

import io.github.jonarzz.edu.domain.common.*;

@ValueObject
public record CandidateForStudent(
        Collection<TestResult> testResults,
        PersonIdentification personIdentification
) {

    public CandidateForStudent {
        var countByFieldOfStudy = testResults.stream()
                                             .collect(groupingBy(TestResult::fieldOfStudy,
                                                                 counting()));
        var duplicatedFieldsOfStudy = countByFieldOfStudy.entrySet()
                                                         .stream()
                                                         .filter(entry -> entry.getValue() > 1)
                                                         .map(Map.Entry::getKey)
                                                         .sorted()
                                                         .collect(joining(", "));
        if (!duplicatedFieldsOfStudy.isEmpty()) {
            throw new IllegalArgumentException("Found more than one test result for: "
                                               + duplicatedFieldsOfStudy);
        }
    }

    public Score getScoreFor(String fieldOfStudy) {
        return testResults.stream()
                          .filter(result -> result.fieldOfStudy.equals(fieldOfStudy))
                          .findFirst()
                          .map(TestResult::score)
                          .orElse(Score.ZERO);
    }

    @Getter(PRIVATE)
    @RequiredArgsConstructor
    @FieldDefaults(makeFinal = true)
    public static class TestResult {

        String fieldOfStudy;
        Score score;
    }
}
