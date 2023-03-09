package io.github.jonarzz.edu.domain.student;

import lombok.*;
import lombok.experimental.*;

import java.util.*;

@FieldDefaults(makeFinal = true)
public class FakeStudentResignationListener implements StudentResignationListener {

    @Getter
    List<Event> events = new ArrayList<>();

    @Override
    public void onStudentResignation(StudentView student, String reason) {
        events.add(new Event(student, reason));
    }

    record Event(StudentView student, String reason) {

    }
}
