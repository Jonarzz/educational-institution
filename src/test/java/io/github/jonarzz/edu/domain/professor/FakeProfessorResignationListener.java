package io.github.jonarzz.edu.domain.professor;

import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.*;

import java.util.*;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FakeProfessorResignationListener implements ProfessorResignationListener {

    @Getter
    List<Event> events = new ArrayList<>();

    @Override
    public void onProfessorResignation(ProfessorView professor, String reason) {
        events.add(new Event(professor, reason));
    }

    record Event(ProfessorView professor, String reason) {

    }
}
