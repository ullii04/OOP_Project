package university.classes;

import university.model.Student;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Enrollment implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String enrollmentId;
    private Student student;
    private Course course;
    private LocalDate enrolledAt;
    private String status;

    public Enrollment(Student student, Course course) {
        this.enrollmentId = "ENR-" + System.currentTimeMillis();
        this.student = student;
        this.course = course;
        this.enrolledAt = LocalDate.now();
        this.status = "PENDING";
    }

    public void approve() {
        status = "APPROVED";
        course.enrollStudent(student);
    }

    public void reject() {
        status = "REJECTED";
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getEnrolledAt() {
        return enrolledAt;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Enrollment)) {
            return false;
        }

        Enrollment enrollment = (Enrollment) object;
        return Objects.equals(enrollmentId, enrollment.enrollmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrollmentId);
    }

    @Override
    public String toString() {
        return "Enrollment{student='" +
                student.getFullName() +
                "', course='" +
                course.getName() +
                "', status=" +
                status + "'}";
    }
}