package university.classes;
import university.model.*;
import university.exceptions.InvalidGradeException;
import university.exceptions.GradeRequirementException;

import java.io.Serializable;
import java.util.Objects;

public class Mark implements Serializable {
    private static final long serialVersionUID = 1L;

    private Double attestation1;
    private Double attestation2;
    private Double finalExam;

    private final Student student;
    private final Course course;

    public Mark(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public Mark(double attestation1, double attestation2, double finalExam,
                Student student, Course course) throws InvalidGradeException {
        this.student = student;
        this.course = course;

        setAttestation1(attestation1);
        setAttestation2(attestation2);
        setFinalExam(finalExam);
    }

    public void setAttestation1(double attestation1) throws InvalidGradeException {
        validateScore(attestation1, 0, 30, "Attestation 1");
        this.attestation1 = attestation1;
    }

    public void setAttestation2(double attestation2) throws InvalidGradeException {
        validateScore(attestation2, 0, 30, "Attestation 2");
        this.attestation2 = attestation2;
    }

    public void setFinalExam(double finalExam) throws InvalidGradeException {
        validateScore(finalExam, 0, 40, "Final exam");
        this.finalExam = finalExam;
    }

    private void validateScore(double score, double min, double max, String fieldName)
            throws InvalidGradeException {
        if (score < min || score > max) {
            throw new InvalidGradeException(fieldName, score, min, max);
        }
    }

    public void validatePassingRequirements() throws GradeRequirementException {
        if (!hasAllMarks()) {
            throw new GradeRequirementException("Cannot validate grade: not all marks are entered.");
        }

        if (getAttestationsTotal() < 30) {
            throw new GradeRequirementException(
                    "Student failed: attestation total must be at least 30 out of 60. Current: "
                            + getAttestationsTotal()
            );
        }

        if (getFinalExam() < 20) {
            throw new GradeRequirementException(
                    "Student failed: final exam must be at least 20 out of 40. Current: "
                            + getFinalExam()
            );
        }

        if (getTotal() < 50) {
            throw new GradeRequirementException(
                    "Student failed: total mark must be at least 50 out of 100. Current: "
                            + getTotal()
            );
        }
    }

    public double getTotal() {
        return getAttestation1() + getAttestation2() + getFinalExam();
    }

    public double getAttestationsTotal() {
        return getAttestation1() + getAttestation2();
    }

    public boolean hasAllMarks() {
        return attestation1 != null && attestation2 != null && finalExam != null;
    }

    public boolean isPassed() {
        return hasAllMarks()
                && getAttestationsTotal() >= 30
                && getFinalExam() >= 20
                && getTotal() >= 50;
    }

    public double getGpa() {
        if (!isPassed()) {
            return 0.0;
        }

        double total = getTotal();

        if (total >= 95) return 4.0;
        if (total >= 90) return 3.67;
        if (total >= 85) return 3.33;
        if (total >= 80) return 3.0;
        if (total >= 75) return 2.67;
        if (total >= 70) return 2.33;
        if (total >= 65) return 2.0;
        if (total >= 60) return 1.67;
        if (total >= 55) return 1.33;
        if (total >= 50) return 1.0;

        return 0.0;
    }

    public String getLetterGrade() {
        if (!isPassed()) {
            return "F";
        }

        double total = getTotal();

        if (total >= 95) return "A";
        if (total >= 90) return "A-";
        if (total >= 85) return "B+";
        if (total >= 80) return "B";
        if (total >= 75) return "B-";
        if (total >= 70) return "C+";
        if (total >= 65) return "C";
        if (total >= 60) return "C-";
        if (total >= 55) return "D+";
        if (total >= 50) return "D";

        return "F";
    }

    public double getAttestation1() {
        return attestation1 == null ? 0.0 : attestation1;
    }

    public double getAttestation2() {
        return attestation2 == null ? 0.0 : attestation2;
    }

    public double getFinalExam() {
        return finalExam == null ? 0.0 : finalExam;
    }

    public boolean hasAttestation1() {
        return attestation1 != null;
    }

    public boolean hasAttestation2() {
        return attestation2 != null;
    }

    public boolean hasFinalExam() {
        return finalExam != null;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mark)) return false;
        Mark mark = (Mark) o;
        return Objects.equals(student, mark.student)
                && Objects.equals(course, mark.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }

    @Override
    public String toString() {
        return String.format(
                "Mark{course='%s', att1=%.1f/30, att2=%.1f/30, final=%.1f/40, total=%.1f/100, grade=%s, gpa=%.2f, passed=%s}",
                course.getName(),
                getAttestation1(),
                getAttestation2(),
                getFinalExam(),
                getTotal(),
                getLetterGrade(),
                getGpa(),
                isPassed()
        );
    }
}
