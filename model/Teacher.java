package university.model;

import university.enums.TeacherTitle;
import university.exceptions.LowResearcherException;

import java.io.Serializable;
import java.util.*;

/**
 * Teacher extends Employee.
 * A Teacher MAY be a Researcher — if so, they hold a Researcher instance (composition).
 * Professors ARE always Researchers (set in constructor).
 * Non-professors CAN also be researchers (setResearcherRole).
 */
public class Teacher extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private TeacherTitle title;
    private List<Course> courses;
    private double rating;

    /**
     * Composition: Teacher "has a" Researcher role (can be null if not a researcher).
     * UML shows composition circle on Teacher->Researcher.
     */
    private Researcher researcherRole;

    public Teacher(String username, String password, String email,
                   String firstName, String lastName, String school, double salary,
                   TeacherTitle title) {
        super(username, password, email, firstName, lastName, school, salary);
        this.title = title;
        this.courses = new ArrayList<>();
        this.rating = 0.0;

        // Professors are ALWAYS researchers
        if (title == TeacherTitle.PROFESSOR || title == TeacherTitle.ASSOCIATE_PROFESSOR) {
            this.researcherRole = new Researcher(getFullName());
        }
    }

    public boolean isProfessor() {
        return title == TeacherTitle.PROFESSOR || title == TeacherTitle.ASSOCIATE_PROFESSOR;
    }

    public boolean isResearcher() { return researcherRole != null; }

    /** Assign (or enable) researcher role for non-professor teachers */
    public void setResearcherRole(Researcher r) {
        this.researcherRole = r;
    }

    public Researcher getResearcherRole() { return researcherRole; }

    public void putMark(Student student, Course course, double att1, double att2, double finalGrade) {
        Mark mark = new Mark(att1, att2, finalGrade, student, course);
        student.addMark(mark);
        university.patterns.Logger.getInstance().log(
                getUsername() + " put mark for " + student.getUsername() + " in " + course.getName());
    }

    public void manageCourse(Course course) {
        if (!courses.contains(course)) courses.add(course);
    }

    public void viewStudents(Course course) {
        System.out.println("=== Students in " + course.getName() + " ===");
        course.getStudents().forEach(s ->
                System.out.println("  " + s.getFullName() + " | GPA: " + s.getGpa()));
    }

    public double getRating() { return rating; }

    public void addRating(double r) {
        if (this.rating == 0.0) this.rating = r;
        else this.rating = (this.rating + r) / 2.0;
    }

    // Getters
    public TeacherTitle getTitle() { return title; }
    public void setTitle(TeacherTitle title) {
        this.title = title;
        // If upgraded to professor, auto-create researcher role
        if ((title == TeacherTitle.PROFESSOR || title == TeacherTitle.ASSOCIATE_PROFESSOR)
                && researcherRole == null) {
            this.researcherRole = new Researcher(getFullName());
        }
    }
    public List<Course> getCourses() { return courses; }

    @Override
    public void showMenu() {
        System.out.println("=== Teacher Menu ===");
        System.out.println("1. View Courses");
        System.out.println("2. Manage Course");
        System.out.println("3. View Students");
        System.out.println("4. Put Mark");
        System.out.println("5. Send Message");
        System.out.println("6. View Inbox");
        if (isResearcher()) {
            System.out.println("7. Research Papers (sorted)");
            System.out.println("8. Join Research Project");
        }
        System.out.println("0. Logout");
    }

    @Override
    public String toString() {
        return "Teacher{name='" + getFullName() + "', title=" + title + ", rating=" + rating + "}";
    }
}
