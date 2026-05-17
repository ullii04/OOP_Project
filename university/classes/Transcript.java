package university.classes;
import university.model.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

public class Transcript implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Student student;
    private final Collection<Mark> marks;
    private final double gpa;
    private final LocalDate generatedAt;

    public Transcript(Student student, Map<Course, Mark> marks) {
        this.student = student;
        this.marks = marks.values();
        this.gpa = calculateGPA();
        this.generatedAt = LocalDate.now();
    }

    public Transcript(Student student, Collection<Mark> marks) {
        this.student = student;
        this.marks = marks;
        this.gpa = calculateGPA();
        this.generatedAt = LocalDate.now();
    }

    private double calculateGPA() {
        if (marks.isEmpty()) {
            return 0.0;
        }

        double weightedSum = 0.0;
        int totalCredits = 0;

        for (Mark mark : marks) {
            int credits = mark.getCourse().getCredits();
            weightedSum += mark.getGpa() * credits;
            totalCredits += credits;
        }

        return totalCredits == 0 ? 0.0 : weightedSum / totalCredits;
    }

    public void generate() {
        System.out.println("====== TRANSCRIPT ======");
        System.out.println("Student: " + student.getFullName());
        System.out.println("Year: " + student.getYear());
        System.out.println("Generated: " + generatedAt);
        System.out.println("------------------------");

        if (marks.isEmpty()) {
            System.out.println("No marks yet.");
        } else {
            for (Mark mark : marks) {
                System.out.println("  " + mark);
            }
        }

        System.out.println("------------------------");
        System.out.printf("Overall GPA: %.2f%n", gpa);
        System.out.println("========================");
    }

    public double getGpa() {
        return gpa;
    }

    public Student getStudent() {
        return student;
    }

    public Collection<Mark> getMarks() {
        return marks;
    }

    public LocalDate getGeneratedAt() {
        return generatedAt;
    }

    @Override
    public String toString() {
        return "Transcript{student='" + student.getFullName()
                + "', gpa=" + String.format("%.2f", gpa)
                + ", generatedAt=" + generatedAt
                + "}";
    }
}
