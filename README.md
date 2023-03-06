# Educational institution
![GitHub actions](https://github.com/Jonarzz/educational-institution/workflows/Java%20CI%20with%20Maven/badge.svg)
![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Jonarzz_educational-institution&metric=sqale_rating)
![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=Jonarzz_educational-institution&metric=sqale_index)
![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Jonarzz_educational-institution&metric=coverage)

## Preface
- `N`, `M` etc. in the description below means a value that is based on configuration
  (varies between sentences - `N` means "a value", not the same value in multiple places)

## Domain
### Educational institution
The whole application is run per single educational institution. 
Institution creation is not part of the domain, but part of the app configuration.

### Faculty
Faculties may be **created** within the educational institution.
Each faculty includes specific **fields of study** that the **candidates**
for professors and students have to match.

### Professor
Professors may be **employed** if specific rules are met:
- the faculty they apply for matches their **fields of study** - at least N fields of study have to match,
  or all if faculty has less than N fields of study
- there is a vacancy in the faculty
- they have at least N years of experience

Professors may **resign**.
A notification to an external system has to be sent upon that event.

### Student
Students may be **enrolled** if specific rules are met:
- there is a vacancy in the faculty
- student scored at least N% of points in tests for the **main fields of study**
- student scored at least M% of points in tests for the **secondary fields of study**

Students may **resign**.
A notification to an external system has to be sent upon that event.

### Course
A course is part of the faculty. Each course can include from N to M **fields of study**.

Professors may **create** courses within a faculty if specific rules are met:
- all fields of study of the course match those of the professor's
- the professor is leading no more than N courses already
- there are at most M courses within the faculty
- there are at most N courses with M or more fields of study matching the new course's

Professors may **resign** from leading the courses.
A notification to an external system has to be sent upon that event.

The course becomes free to be **overtaken** by another professor if:
- all fields of study of the course match those of the professor's
- the professor is leading no more than N courses already

Maximum of N students may **enroll** for a course.
That number may be restricted by the course-leading professor,
but it cannot be less than M students.

A course may be **closed** if less than N% of maximum number of students are enrolled.
A notification to an external system has to be sent upon that event.
