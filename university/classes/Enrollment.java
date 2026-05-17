package university.model;
import university.enums.EnrollmentStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Enrollment implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String enrollmentId;
    private final Student student;
    private final Course course;
    private final LocalDate enrolledAt;
    private EnrollmentStatus status;

    public Enrollment(Student student, Course course) {
        this.enrollmentId = "ENR-" + System.currentTimeMillis();
        this.student = student;
        this.course = course;
        this.enrolledAt = LocalDate.now();
        this.status = EnrollmentStatus.PENDING;
    }

    public void approve() {
        if (status == EnrollmentStatus.REJECTED) {
            System.out.println("Cannot approve rejected enrollment.");
            return;
        }

        if (status == EnrollmentStatus.APPROVED) {
            System.out.println("Enrollment is already approved.");
            return;
        }

        boolean enrolled = course.enrollStudent(student);

        if (enrolled) {
            status = EnrollmentStatus.APPROVED; }
        else {
            System.out.println("Cannot approve enrollment: course is full or unavailable.");
        }
    }

    public void reject() {
        if (status == EnrollmentStatus.APPROVED) {
            System.out.println("Cannot reject already approved enrollment.");
            return;
        }

        status = EnrollmentStatus.REJECTED;
    }

    public void drop() {
        if (status == EnrollmentStatus.APPROVED) {
            course.removeStudent(student);
        }
        status = EnrollmentStatus.DROPPED;
    }

    public boolean isApproved() {
        return status == EnrollmentStatus.APPROVED;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public LocalDate getEnrolledAt() {
        return enrolledAt;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment)) return false;
        Enrollment e = (Enrollment) o;
        return Objects.equals(enrollmentId, e.enrollmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrollmentId);
    }

    @Override
    public String toString() {
        return "Enrollment{student='" + student.getFullName()
                + "', course='" + course.getName()
                + "', status=" + status
                + ", enrolledAt=" + enrolledAt
                + "}";
    }
}
