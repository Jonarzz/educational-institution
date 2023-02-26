package io.github.jonarzz.edu.domain.faculty;

import java.util.*;

public interface EducationalInstitutionRepository {

    Optional<EducationalInstitution> find(UUID educationalInstitutionId);

    void save(EducationalInstitution institution);
}
