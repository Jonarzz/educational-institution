package io.github.jonarzz.edu.domain.course;

import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.*;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.*;

import io.github.jonarzz.edu.domain.common.*;

@ValueObject
@Getter
@RequiredArgsConstructor(access = PACKAGE)
@FieldDefaults(makeFinal = true)
public final class ValidCourseData {

    String name;
    FieldsOfStudy fieldsOfStudy;

}
