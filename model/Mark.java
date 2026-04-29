package university.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Mark consists of attestation1, attestation2, and finalExam.
 * Total = att1*30% + att2*30% + final*40%.
 * getGrade() returns letter grade as per UML: +getGrade(): String
 */
public class Mark implements Serializable, Comparable<Mark> {
    private static final long serialVersionUID = 1L;

    private double attestation1;
    private double attestation2;
    private double finalExam;
    private double gpa;
    private Student student;
    private Course course;

    public Mark(double attestation1, double attestation2, double finalExam,
                Student student, Course course) {
        this.attestation1 = attestation1;
        this.attestation2 = attestation2;
        this.finalExam = finalExam;
        this.student = student;
        this.course = course;
        this.gpa = calculateGPA();
    }

    private double calculateGPA() {
        double total = getTotal();
        if (total >= 90) return 4.0;
        if (total >= 80) return 3.0;
        if (total >= 70) return 2.0;
        if (total >= 60) return 1.0;
        return 0.0;
    }

    /** Weighted total score: att1(30%) + att2(30%) + final(40%) */
    public double getTotal() {
        return attestation1 * 0.3 + attestation2 * 0.3 + finalExam * 0.4;
    }

    public boolean isPassed() {
        return getTotal() >= 50;
    }

    /**
     * Returns letter grade — as specified in UML: +getGrade(): String
     */
    public String getGrade() {
        double total = getTotal();
        if (total >= 90) return "A";
        if (total >= 80) return "B";
        if (total >= 70) return "C";
        if (total >= 60) return "D";
        return "F";
    }

    @Override
    public int compareTo(Mark other) {
        return Double.compare(other.getTotal(), this.getTotal()); // descending
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mark)) return false;
        Mark mark = (Mark) o;
        return Objects.equals(student, mark.student) && Objects.equals(course, mark.course);
    }

    @Override
    public int hashCode() { return Objects.hash(student, course); }

    @Override
    public String toString() {
        return String.format("Mark{course='%s', att1=%.1f, att2=%.1f, final=%.1f, total=%.1f, grade=%s, gpa=%.1f}",
                course.getName(), attestation1, attestation2, finalExam, getTotal(), getGrade(), gpa);
    }

    // Getters
    public double getAttestation1() { return attestation1; }
    public double getAttestation2() { return attestation2; }
    public double getFinalExam() { return finalExam; }
    public double getGpa() { return gpa; }
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
}
