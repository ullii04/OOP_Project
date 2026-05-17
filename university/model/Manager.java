package university.model;
import university.classes.*;
import university.enums.ManagerType;
import university.exceptions.GradeRequirementException;
import university.patterns.Logger;
import java.util.*;

public class Manager extends Employee {
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

    
    public Manager(String firstName, String lastName, String email, String password,
                   String school, double salary, ManagerType type) {
        super(firstName, lastName, email, password, school, salary);
        this.type = type;
        this.news = new ArrayList<>();
        this.pendingEnrollments = new ArrayList<>();
    }

   
    public void approveRegistration(Enrollment enrollment) {
        try {
            Student student = enrollment.getStudent();
            Course course = enrollment.getCourse();

            
            if (student.getCredits() + course.getCredits() > 21) {
                rejectRegistration(enrollment);
                System.out.println("🚨 Registration rejected automatically: Credit limit exceeded for " + student.getFullName());
                return;
            }

            
            if (course.getPrerequisite() != null) {
                Course prerequisite = course.getPrerequisite();
                if (!student.getPassedCourses().contains(prerequisite)) {
                    throw new GradeRequirementException("Student " + student.getFullName() + 
                        " does not meet requirements for " + course.getName() + 
                        ". Must pass prerequisite first: " + prerequisite.getName());
                }
            }

            
            enrollment.approve();
            student.getCourses().add(course);
            pendingEnrollments.remove(enrollment);
            
            Logger.getInstance().log(getUsername() + " approved enrollment for " + student.getUsername() + " to " + course.getName());
            System.out.println(" Enrollment approved: " + student.getFullName() + " -> " + course.getName());

        } catch (GradeRequirementException e) {
            System.out.println(" Registration Academic Error: " + e.getMessage());
            rejectRegistration(enrollment); 
        }
    }

    
    public void rejectRegistration(Enrollment enrollment) {
        enrollment.reject();
        pendingEnrollments.remove(enrollment);
        
        Logger.getInstance().log(getUsername() + " rejected enrollment for " + enrollment.getStudent().getUsername());
        System.out.println("Enrollment rejected for: " + enrollment.getStudent().getFullName());
    }

   
    public void addCourseForRegistration(Course course) {
        course.setAvailable(true);
        System.out.println("Course opened for registration: " + course.getName());
    }

    public void assignCourseToTeacher(Course course, Teacher teacher) {
        course.assignTeacher(teacher);
        teacher.manageCourse(course);
        System.out.println(" Assigned " + teacher.getFullName() + " to " + course.getName());
    }

    
    public void viewAcademicRiskStudents(List<Student> students) {
        System.out.println("\n=== STUDENTS AT ACADEMIC RISK (Fails > 3) ===");
        long count = students.stream()
                .filter(s -> s.getFailCount() > 3)
                .peek(s -> System.out.printf("Student: %-20s | Fails: %d | GPA: %.2f%n", 
                        s.getFullName(), s.getFailCount(), s.getGpa()))
                .count();
        
        if (count == 0) {
            System.out.println("No students at academic risk. Excellent!");
        }
    }

   
    public void generateReport(List<Student> students) {
        System.out.println("\n====== ACADEMIC PERFORMANCE REPORT ======");
        System.out.printf("%-20s %-6s %-6s %-10s%n", "Name", "Year", "GPA", "Status");
        System.out.println("-".repeat(48));
        
        students.stream()
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .forEach(s -> {
                    String status = s.getFailCount() > 3 ? "PROBATION" : "ACTIVE";
                    System.out.printf("%-20s %-6d %-6.2f %-10s%n",
                            s.getFullName(), s.getYear(), s.getGpa(), status);
                });
                
        double avg = students.stream().mapToDouble(Student::getGpa).average().orElse(0);
        System.out.println("-".repeat(48));
        System.out.printf("Average University GPA: %.2f%n", avg);
    }

    
    public void manageNews(String newsItem) {
        if (newsItem == null || newsItem.isBlank()) {
            throw new IllegalArgumentException("News content cannot be empty.");
        }
        news.add(newsItem);
        System.out.println("News published: " + newsItem);
    }

    
    public void viewStudentsSortedByGpa(List<Student> students) {
        System.out.println("\n=== Students sorted by GPA ===");
        students.stream()
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .forEach(s -> System.out.printf("  %-20s GPA: %.2f%n", s.getFullName(), s.getGpa()));
    }

    
    public void viewStudentsSortedAlphabetically(List<Student> students) {
        System.out.println("\n=== Students sorted alphabetically ===");
        students.stream()
                .sorted(Comparator.comparing(Student::getLastName))
                .forEach(s -> System.out.println("  " + s.getFullName()));
    }

    public void addPendingEnrollment(Enrollment e) { 
        pendingEnrollments.add(e); 
    }

    @Override
    public void showMenu() {
        System.out.println("\n=== Manager Menu ===");
        System.out.println("1. View Pending Registrations");
        System.out.println("2. Approve/Reject Student Registrations");
        System.out.println("3. Add Course for Registration");
        System.out.println("4. Assign Course to Teacher");
        System.out.println("5. Generate Academic Report");
        System.out.println("6. View Academic Risk Students (Fails > 3)");
        System.out.println("7. View Students (sorted by GPA)");
        System.out.println("8. View Students (alphabetically)");
        System.out.println("9. Manage News");
        System.out.println("10. View Inbox / Notifications");
        System.out.println("0. Logout");
    }

  
    public ManagerType getType() { return type; }
    public List<String> getNews() { return news; }
    public List<Enrollment> getPendingEnrollments() { return pendingEnrollments; }

    @Override
    public String toString() {
        return "Manager{" +
                "id='" + getEmployeeId() + '\'' +
                ", name='" + getFullName() + '\'' +
                ", school='" + getSchool() + '\'' +
                ", type=" + type +
                '}';
    }
}
