package io.github.jonarzz.edu.domain.common;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.*;

public class TestFieldsOfStudyFactory {

    public static FieldsOfStudy fieldsOfStudyFrom(String csv) {
        return fieldsOfStudyFrom(Arrays.stream(csv.split(",")));
    }

    public static FieldsOfStudy fieldsOfStudyFrom(Stream<String> namesStream) {
        var fieldsList = namesStream.map(String::trim)
                                    .collect(toList());
        return FieldsOfStudy.from(
                fieldsList.get(0),
                fieldsList.subList(1, fieldsList.size())
        );
    }
}
