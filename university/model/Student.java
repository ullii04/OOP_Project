package university.model;
import university.classes.*;
import university.exceptions.LowResearcherException;
import university.exceptions.MaxCreditsExceededException;
import university.patterns.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int MAX_CREDITS = 21;
    private static final int MAX_FAILS = 3;

    private String studentId;

    private String firstName;
    private String lastName;

    private int year;
    private double gpa;
    private int credits;
    private int failCount;

    private List<Mark> marks;
    private List<Course> courses;
    private List<Enrollment> enrollments;

    private Researcher researchSupervisor;

    private Researcher researcherRole;

    public Student(String username,
                   String password,
                   String email,
                   String firstName,
                   String lastName,
                   int year) {

        super(username, password, email);

        this.studentId = "STU-" + System.currentTimeMillis();

        this.firstName = firstName;
        this.lastName = lastName;

        this.year = year;

        this.gpa = 0.0;
        this.credits = 0;
        this.failCount = 0;

        this.marks = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.enrollments = new ArrayList<>();
    }

    public void registerCourse(Course course)
            throws MaxCreditsExceededException {

        if (credits + course.getCredits() > MAX_CREDITS) {

            throw new MaxCreditsExceededException(
                    "Cannot register: would exceed max "
                            + MAX_CREDITS
                            + " credits. Current: "
                            + credits
            );
        }

        Enrollment enrollment =
                new Enrollment(this, course);

        enrollments.add(enrollment);

        credits += course.getCredits();

        Logger.getInstance()
                .log(
                        getUsername()
                                + " registered for "
                                + course.getName()
                );

        System.out.println(
                "Registration submitted (pending approval): "
                        + course.getName()
        );
    }

    public void viewMarks() {

        System.out.println(
                "=== Marks for "
                        + getFullName()
                        + " ==="
        );

        if (marks.isEmpty()) {

            System.out.println("  No marks yet.");

            return;
        }

        marks.forEach(
                mark -> System.out.println("  " + mark)
        );

        System.out.printf(
                "  Overall GPA: %.2f%n",
                gpa
        );
    }

    public List<Mark> viewMarks(Object dummy) {
        return marks;
    }

    public Transcript getTranscript() {
        return new Transcript(this, marks);
    }

    public void rateTeacher(Teacher teacher,
                            double rating) {

        teacher.addRating(rating);

        Logger.getInstance()
                .log(
                        getUsername()
                                + " rated teacher "
                                + teacher.getUsername()
                                + " with "
                                + rating
                );
    }

    public void addMark(Mark mark) {

        marks.removeIf(
                existing ->
                        existing.getCourse()
                                .equals(mark.getCourse())
        );

        marks.add(mark);

        if (!mark.isPassed()) {

            failCount++;

            if (failCount > MAX_FAILS) {

                System.out.println(
                        "WARNING: "
                                + getFullName()
                                + " has failed more than "
                                + MAX_FAILS
                                + " times!"
                );
            }
        }

        recalculateGPA();
    }

    private void recalculateGPA() {

        if (marks.isEmpty()) {

            gpa = 0.0;

            return;
        }

        gpa = marks.stream()
                .mapToDouble(Mark::getGpa)
                .average()
                .orElse(0.0);
    }

    public void setResearchSupervisor(
            Researcher supervisor
    ) throws LowResearcherException {

        if (year != 4) {

            System.out.println(
                    "Only 4th year students can have a research supervisor."
            );

            return;
        }

        if (supervisor.getHIndex() < 3) {

            throw new LowResearcherException(
                    "Supervisor h-index="
                            + supervisor.getHIndex()
                            + " is below required 3."
            );
        }

        this.researchSupervisor = supervisor;

        System.out.println(
                "Research supervisor assigned to "
                        + getFullName()
        );
    }

    public boolean isResearcher() {
        return researcherRole != null;
    }

    public void enableResearcherRole(Researcher role) {
        this.researcherRole = role;
    }

    public Researcher getResearcherRole() {
        return researcherRole;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getYear() {
        return year;
    }

    public double getGpa() {
        return gpa;
    }

    public int getCredits() {
        return credits;
    }

    public int getFailCount() {
        return failCount;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public Researcher getResearchSupervisor() {
        return researchSupervisor;
    }

    @Override
    public void showMenu() {

        System.out.println("=== Student Menu ===");

        System.out.println("1. View Courses");
        System.out.println("2. Register for Course");
        System.out.println("3. View Marks & GPA");
        System.out.println("4. View/Get Transcript");
        System.out.println("5. Rate Teacher");
        System.out.println("6. View Teacher Info for Course");

        if (isResearcher()) {
            System.out.println("7. Research Papers (sorted)");
        }

        System.out.println("0. Logout");
    }

    @Override
    public String toString() {

        return "Student{name='"
                + getFullName()
                + "', year="
                + year
                + ", gpa="
                + String.format("%.2f", gpa)
                + ", credits="
                + credits
                + "}";
    }
}
