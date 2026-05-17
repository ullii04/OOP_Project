package university.model;

import university.classes.*;
import university.enums.TeacherTitle;
import university.exceptions.InvalidGradeException;
import university.exceptions.GradeRequirementException; 
import university.patterns.Logger;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends Employee {
    private static final long serialVersionUID = 1L;

    private TeacherTitle title;
    private List<Course> courses;
    private List<Lesson> schedule;
    private List<Double> ratings;

    private Researcher researcherProfile;

    public Teacher(String username, String password, String email,
                   String firstName, String lastName, String school, 
                   double salary, TeacherTitle title) {
        super(username, password, email, firstName, lastName, school, salary);

        this.title = title;
        this.courses = new ArrayList<>();
        this.schedule = new ArrayList<>();
        this.ratings = new ArrayList<>();

        if (title == TeacherTitle.PROFESSOR) {
            this.researcherProfile = new Researcher(getFullName(), 3);
        }
    }

    public Teacher(String firstName, String lastName, String email, String password,
                   String school, double salary, TeacherTitle title) {
        super(firstName, lastName, email, password, school, salary);

        this.title = title;
        this.courses = new ArrayList<>();
        this.schedule = new ArrayList<>();
        this.ratings = new ArrayList<>();

        if (title == TeacherTitle.PROFESSOR) {
            this.researcherProfile = new Researcher(getFullName(), 3);
        }
    }

    public void putMark(Student student, Course course,
                        double attestation1, double attestation2, double finalGrade) {
        try {
            if ((attestation1 + attestation2) < 30.0 && finalGrade > 0) {
                throw new GradeRequirementException(
                        "Student " + student.getFullName()
                                + " cannot take the final exam. Total attestation score must be at least 30.0. Current: "
                                + (attestation1 + attestation2)
                );
            }

            Mark mark = new Mark(attestation1, attestation2, finalGrade, student, course);
            student.addMark(mark);

            Logger.getInstance().log(
                    getUsername() + " put mark for "
                            + student.getUsername()
                            + " in "
                            + course.getName()
            );

            System.out.println("Mark successfully updated for "
                    + student.getFullName()
                    + " in "
                    + course.getName());

        } catch (InvalidGradeException e) {
            System.out.println("Invalid grade error: " + e.getMessage());
        } catch (GradeRequirementException e) {
            System.out.println("Academic requirement error: " + e.getMessage());
        }
    }

    public void manageCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
        }
    }

    public void addLessonToSchedule(Lesson lesson) {
        if (!schedule.contains(lesson)) {
            schedule.add(lesson);
        }
    }

    public void viewMyCourses() {
        System.out.println("\n=== Courses taught by " + title + " " + getFullName() + " ===");

        if (courses.isEmpty()) {
            System.out.println("No courses assigned yet.");
        } else {
            courses.forEach(c ->
                    System.out.println(" - " + c.getName() + " (" + c.getCredits() + " credits)")
            );
        }
    }

    public void viewMySchedule() {
        System.out.println("\n=== Schedule for " + getFullName() + " ===");

        if (schedule.isEmpty()) {
            System.out.println("No lessons scheduled.");
        } else {
            schedule.forEach(System.out::println);
        }
    }

    public void addRating(double rating) {
        if (rating < 1.0 || rating > 5.0) {
            throw new IllegalArgumentException("Teacher rating must be between 1 and 5.");
        }

        ratings.add(rating);
    }

    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0.0;
        }

        return ratings.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public boolean isResearcher() {
        return researcherProfile != null;
    }

    public void setResearcherProfile(Researcher researcherProfile) {
        this.researcherProfile = researcherProfile;
    }

    public Researcher getResearcherProfile() {
        return researcherProfile;
    }

    public void setResearcherRole(Researcher researcherRole) {
        this.researcherProfile = researcherRole;
    }

    public Researcher getResearcherRole() {
        return researcherProfile;
    }

    @Override
    public void showMenu() {
        System.out.println("\n=== Teacher Menu ===");
        System.out.println("1. View My Courses");
        System.out.println("2. View My Schedule");
        System.out.println("3. Put/Update Student Mark");
        System.out.println("4. View Inbox / Notifications");
        System.out.println("5. Send Message to Colleague");
        System.out.println("6. Send Complaint");

        if (isResearcher()) {
            System.out.println("7. Research Papers");
            System.out.println("8. Join Research Project");
        }

        System.out.println("0. Logout");
    }

    public TeacherTitle getTitle() {
        return title;
    }

    public void setTitle(TeacherTitle title) {
        this.title = title;

        if (title == TeacherTitle.PROFESSOR && researcherProfile == null) {
            this.researcherProfile = new Researcher(getFullName(), 3);
        }
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Lesson> getSchedule() {
        return schedule;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id='" + getEmployeeId() + '\'' +
                ", title=" + title +
                ", name='" + getFullName() + '\'' +
                ", school='" + getSchool() + '\'' +
                ", averageRating=" + String.format("%.2f", getAverageRating()) +
                ", researcher=" + isResearcher() +
                '}';
    }
}
