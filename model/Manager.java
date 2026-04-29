package university.model;

import university.enums.ManagerType;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Manager extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private ManagerType type;
    private List<String> news;
    private List<Enrollment> pendingEnrollments;

    public Manager(String username, String password, String email,
                   String firstName, String lastName, String school,
                   double salary, ManagerType type) {
        super(username, password, email, firstName, lastName, school, salary);
        this.type = type;
        this.news = new ArrayList<>();
        this.pendingEnrollments = new ArrayList<>();
    }

    public void approveRegistration(Enrollment enrollment) {
        enrollment.approve();
        pendingEnrollments.remove(enrollment);
        university.patterns.Logger.getInstance().log(getUsername() + " approved enrollment: " + enrollment);
        System.out.println("Enrollment approved: " + enrollment);
    }

    public void rejectRegistration(Enrollment enrollment) {
        enrollment.reject();
        pendingEnrollments.remove(enrollment);
        university.patterns.Logger.getInstance().log(getUsername() + " rejected enrollment: " + enrollment);
        System.out.println("Enrollment rejected: " + enrollment);
    }

    public void addCourseForRegistration(Course course) {
        course.setAvailable(true);
        System.out.println("Course added for registration: " + course.getName());
    }

    public void assignCourseToTeacher(Course course, Teacher teacher) {
        course.assignTeacher(teacher);
        teacher.manageCourse(course);
        System.out.println("Assigned " + teacher.getFullName() + " to " + course.getName());
    }

    public void generateReport(List<Student> students) {
        System.out.println("====== ACADEMIC PERFORMANCE REPORT ======");
        System.out.printf("%-20s %-6s %-6s%n", "Name", "Year", "GPA");
        System.out.println("-".repeat(35));
        students.stream()
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .forEach(s -> System.out.printf("%-20s %-6d %-6.2f%n",
                        s.getFullName(), s.getYear(), s.getGpa()));
        double avg = students.stream().mapToDouble(Student::getGpa).average().orElse(0);
        System.out.println("-".repeat(35));
        System.out.printf("Average GPA: %.2f%n", avg);
    }

    public void manageNews(String newsItem) {
        news.add(newsItem);
        System.out.println("News published: " + newsItem);
    }

    public void viewStudentsSortedByGpa(List<Student> students) {
        System.out.println("=== Students sorted by GPA ===");
        students.stream().sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .forEach(s -> System.out.printf("  %-20s GPA: %.2f%n", s.getFullName(), s.getGpa()));
    }

    public void viewStudentsSortedAlphabetically(List<Student> students) {
        System.out.println("=== Students sorted alphabetically ===");
        students.stream().sorted(Comparator.comparing(Student::getLastName))
                .forEach(s -> System.out.println("  " + s.getFullName()));
    }

    public void addPendingEnrollment(Enrollment e) { pendingEnrollments.add(e); }

    // Getters
    public ManagerType getType() { return type; }
    public List<String> getNews() { return news; }
    public List<Enrollment> getPendingEnrollments() { return pendingEnrollments; }

    @Override
    public void showMenu() {
        System.out.println("=== Manager Menu ===");
        System.out.println("1. Approve Student Registrations");
        System.out.println("2. Add Course for Registration");
        System.out.println("3. Assign Course to Teacher");
        System.out.println("4. Generate Academic Report");
        System.out.println("5. Manage News");
        System.out.println("6. View Students (sorted by GPA)");
        System.out.println("7. View Students (alphabetically)");
        System.out.println("0. Logout");
    }

    @Override
    public String toString() {
        return "Manager{name='" + getFullName() + "', type=" + type + "}";
    }
}
