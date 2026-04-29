package university.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Transcript implements Serializable {
    private static final long serialVersionUID = 1L;

    private Student student;
    private List<Mark> marks;
    private double gpa;
    private LocalDate generatedAt;

    public Transcript(Student student, List<Mark> marks) {
        this.student = student;
        this.marks = marks;
        this.gpa = calculateGPA();
        this.generatedAt = LocalDate.now();
    }

    private double calculateGPA() {
        if (marks.isEmpty()) return 0.0;
        return marks.stream().mapToDouble(Mark::getGpa).average().orElse(0.0);
    }

    public void generate() {
        System.out.println("====== TRANSCRIPT ======");
        System.out.println("Student: " + student.getFullName());
        System.out.println("Year: " + student.getYear());
        System.out.println("Generated: " + generatedAt);
        System.out.println("------------------------");
        marks.forEach(m -> System.out.println("  " + m));
        System.out.println("------------------------");
        System.out.printf("Overall GPA: %.2f%n", gpa);
        System.out.println("========================");
    }

    public double getGpa() { return gpa; }
    public Student getStudent() { return student; }
    public List<Mark> getMarks() { return marks; }

    @Override
    public String toString() {
        return "Transcript{student='" + student.getFullName() + "', gpa=" + gpa + "}";
    }
}
