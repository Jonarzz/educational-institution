package io.github.jonarzz.edu.domain.professor;

import io.github.jonarzz.edu.domain.faculty.FacultyId;
import java.util.Collection;
import java.util.UUID;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD.Repository;

@Repository
public interface ProfessorRepository {

    ProfessorView getById(UUID professorId);

    Collection<ProfessorView> getByFaculty(FacultyId facultyId);

    ProfessorView getBy(FacultyId facultyId, UUID professorId);

    void saveNew(FacultyId facultyId, ProfessorView professor);

    void update(ProfessorView professor);
}
