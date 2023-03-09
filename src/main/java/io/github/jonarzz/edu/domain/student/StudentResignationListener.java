package io.github.jonarzz.edu.domain.student;

public interface StudentResignationListener {

    void onStudentResignation(StudentView student, String reason);

}
