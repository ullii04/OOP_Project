package university;

import university.enums.DayOfWeek;
import university.enums.LessonType;
import university.enums.ManagerType;
import university.enums.TeacherTitle;
import university.exceptions.AuthenticationException;
import university.exceptions.LowResearcherException;
import university.exceptions.MaxCreditsExceededException;
import university.exceptions.NotResearcherException;
import university.model.*;
import university.patterns.Logger;
import university.patterns.NewsPublisher;
import university.patterns.ResearcherDecorator;
import university.patterns.SortStrategy;
import university.patterns.UserFactory;
import university.utils.AuthService;
import university.utils.DataStorage;
import university.utils.ResearchUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DataStorage db = DataStorage.getInstance();
    private static final NewsPublisher newsPublisher = new NewsPublisher();

    public static void main(String[] args) {
        seedData();
        System.out.println("=========================================");
        System.out.println("  Research University Information System ");
        System.out.println("=========================================");

        while (true) {
            if (!AuthService.isLoggedIn()) {
                showLoginMenu();
            } else {
                User user = AuthService.getCurrentUser();
                user.showMenu();
                handleMenu(user);
            }
        }
    }

    // ==================== LOGIN ====================
    private static void showLoginMenu() {
        System.out.println("\n1. Login\n0. Exit");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        if (choice.equals("0")) {
            db.saveToFile();
            System.out.println("Goodbye!");
            System.exit(0);
        }
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        try {
            AuthService.login(username, password);
        } catch (AuthenticationException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    // ==================== MENU DISPATCH ====================
    private static void handleMenu(User user) {
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        if (choice.equals("0")) { AuthService.logout(); return; }
        if (user instanceof Admin a) handleAdminMenu(choice, a);
        else if (user instanceof Manager m) handleManagerMenu(choice, m);
        else if (user instanceof Teacher t) handleTeacherMenu(choice, t);
        else if (user instanceof Student s) handleStudentMenu(choice, s);
    }

    // ==================== ADMIN ====================
    private static void handleAdminMenu(String choice, Admin admin) {
        switch (choice) {
            case "1" -> {
                System.out.println("a) Add  b) Remove  c) Update email");
                String sub = scanner.nextLine().trim();
                switch (sub) {
                    case "a" -> {
                        System.out.print("Type (STUDENT/TEACHER/MANAGER/ADMIN/RESEARCHER): ");
                        String type = scanner.nextLine().trim().toUpperCase();
                        System.out.print("Username: "); String u = scanner.nextLine().trim();
                        System.out.print("Password: "); String p = scanner.nextLine().trim();
                        System.out.print("Email: ");    String e = scanner.nextLine().trim();
                        System.out.print("FirstName: ");String fn = scanner.nextLine().trim();
                        System.out.print("LastName: "); String ln = scanner.nextLine().trim();
                        System.out.print("School: ");   String sc = scanner.nextLine().trim();
                        try {
                            UserFactory.UserType ut = UserFactory.UserType.valueOf(type);
                            User newUser = UserFactory.createUser(ut, u, p, e, fn, ln, sc);
                            admin.addUser(db.getUsers(), newUser);
                        } catch (Exception ex) { System.out.println("Error: " + ex.getMessage()); }
                    }
                    case "b" -> {
                        System.out.print("Username to remove: "); String u = scanner.nextLine().trim();
                        User target = db.findUserByUsername(u);
                        if (target != null) admin.removeUser(db.getUsers(), target);
                        else System.out.println("User not found.");
                    }
                    case "c" -> {
                        System.out.print("Username: ");  String u = scanner.nextLine().trim();
                        System.out.print("New email: "); String em = scanner.nextLine().trim();
                        User target = db.findUserByUsername(u);
                        if (target != null) admin.updateUser(target, em);
                        else System.out.println("User not found.");
                    }
                }
            }
            case "2" -> admin.viewLogs();
            case "3" -> admin.manageUsers(db.getUsers());
            default -> System.out.println("Invalid option.");
        }
    }

    // ==================== MANAGER ====================
    private static void handleManagerMenu(String choice, Manager manager) {
        switch (choice) {
            case "1" -> {
                List<Enrollment> pending = manager.getPendingEnrollments();
                if (pending.isEmpty()) { System.out.println("No pending enrollments."); return; }
                for (int i = 0; i < pending.size(); i++)
                    System.out.println(i + ") " + pending.get(i));
                System.out.print("Enter index to approve (or -1 to skip): ");
                try {
                    int idx = Integer.parseInt(scanner.nextLine().trim());
                    if (idx >= 0 && idx < pending.size()) manager.approveRegistration(pending.get(idx));
                } catch (NumberFormatException ex) { System.out.println("Invalid."); }
            }
            case "2" -> {
                System.out.print("Course name: "); String cn = scanner.nextLine().trim();
                Course c = db.findCourseByName(cn);
                if (c != null) manager.addCourseForRegistration(c);
                else System.out.println("Course not found.");
            }
            case "3" -> {
                System.out.print("Course name: ");     String cn = scanner.nextLine().trim();
                System.out.print("Teacher username: ");String tu = scanner.nextLine().trim();
                Course c = db.findCourseByName(cn);
                User t = db.findUserByUsername(tu);
                if (c != null && t instanceof Teacher teacher) manager.assignCourseToTeacher(c, teacher);
                else System.out.println("Course or teacher not found.");
            }
            case "4" -> manager.generateReport(db.getStudents());
            case "5" -> {
                System.out.print("News text: "); String news = scanner.nextLine().trim();
                manager.manageNews(news);
                newsPublisher.publishNews(news);
            }
            case "6" -> manager.viewStudentsSortedByGpa(db.getStudents());
            case "7" -> manager.viewStudentsSortedAlphabetically(db.getStudents());
            default -> System.out.println("Invalid option.");
        }
    }

    // ==================== TEACHER ====================
    private static void handleTeacherMenu(String choice, Teacher teacher) {
        switch (choice) {
            case "1" -> teacher.getCourses().forEach(System.out::println);
            case "2" -> {
                System.out.print("Course name: "); String cn = scanner.nextLine().trim();
                Course c = db.findCourseByName(cn);
                if (c != null) teacher.manageCourse(c);
                else System.out.println("Not found.");
            }
            case "3" -> {
                System.out.print("Course name: "); String cn = scanner.nextLine().trim();
                Course c = db.findCourseByName(cn);
                if (c != null) teacher.viewStudents(c);
                else System.out.println("Not found.");
            }
            case "4" -> {
                System.out.print("Student username: "); String su = scanner.nextLine().trim();
                System.out.print("Course name: ");      String cn = scanner.nextLine().trim();
                User s = db.findUserByUsername(su);
                Course c = db.findCourseByName(cn);
                if (s instanceof Student student && c != null) {
                    try {
                        System.out.print("Attestation 1 (0-100): "); double a1 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Attestation 2 (0-100): "); double a2 = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Final exam   (0-100): ");  double fe = Double.parseDouble(scanner.nextLine().trim());
                        teacher.putMark(student, c, a1, a2, fe);
                        System.out.println("Mark recorded.");
                    } catch (NumberFormatException ex) { System.out.println("Invalid grade input."); }
                } else System.out.println("Student or course not found.");
            }
            case "5" -> {
                System.out.print("Recipient username: "); String ru = scanner.nextLine().trim();
                User rec = db.findUserByUsername(ru);
                if (rec instanceof Employee emp) {
                    System.out.print("Message: "); String msg = scanner.nextLine().trim();
                    teacher.sendMessage(emp, msg);
                } else System.out.println("Recipient not found.");
            }
            case "6" -> teacher.viewInbox();
            case "7" -> {
                if (!teacher.isResearcher()) { System.out.println("Not a researcher."); return; }
                System.out.println("Sort by: 1) Citations  2) Date  3) Length  4) Title");
                String s = scanner.nextLine().trim();
                teacher.getResearcherRole().printPapers(switch (s) {
                    case "2" -> SortStrategy.BY_DATE;
                    case "3" -> SortStrategy.BY_LENGTH;
                    case "4" -> SortStrategy.BY_TITLE;
                    default  -> SortStrategy.BY_CITATIONS;
                });
            }
            case "8" -> {
                if (!teacher.isResearcher()) { System.out.println("Not a researcher."); return; }
                System.out.print("Project topic: "); String topic = scanner.nextLine().trim();
                ResearchProject proj = db.getResearchProjects().stream()
                        .filter(p -> p.getTopic().equalsIgnoreCase(topic)).findFirst().orElse(null);
                if (proj == null) { proj = new ResearchProject(topic); db.addResearchProject(proj); }
                try {
                    teacher.getResearcherRole().addParticipants(proj);
                    System.out.println("Joined project: " + topic);
                } catch (LowResearcherException ex) { System.out.println("Error: " + ex.getMessage()); }
            }
            default -> System.out.println("Invalid option.");
        }
    }

    // ==================== STUDENT ====================
    private static void handleStudentMenu(String choice, Student student) {
        switch (choice) {
            case "1" -> db.getCourses().stream().filter(Course::isAvailable).forEach(System.out::println);
            case "2" -> {
                System.out.print("Course name: "); String cn = scanner.nextLine().trim();
                Course c = db.findCourseByName(cn);
                if (c != null) {
                    try {
                        student.registerCourse(c);
                        Enrollment enr = new Enrollment(student, c);
                        db.addEnrollment(enr);
                        db.getUsers().stream()
                                .filter(u -> u instanceof Manager)
                                .forEach(u -> ((Manager) u).addPendingEnrollment(enr));
                    } catch (MaxCreditsExceededException ex) { System.out.println("Error: " + ex.getMessage()); }
                } else System.out.println("Course not found.");
            }
            case "3" -> student.viewMarks();
            case "4" -> student.getTranscript().generate();
            case "5" -> {
                System.out.print("Teacher username: "); String tu = scanner.nextLine().trim();
                User t = db.findUserByUsername(tu);
                if (t instanceof Teacher teacher) {
                    System.out.print("Rating (1-5): ");
                    try { student.rateTeacher(teacher, Double.parseDouble(scanner.nextLine().trim())); }
                    catch (NumberFormatException ex) { System.out.println("Invalid."); }
                } else System.out.println("Teacher not found.");
            }
            case "6" -> {
                System.out.print("Course name: "); String cn = scanner.nextLine().trim();
                Course c = db.findCourseByName(cn);
                if (c != null) c.getInstructors().forEach(System.out::println);
                else System.out.println("Course not found.");
            }
            case "7" -> {
                if (!student.isResearcher()) { System.out.println("Not a researcher."); return; }
                System.out.println("Sort by: 1) Citations  2) Date  3) Length");
                String s = scanner.nextLine().trim();
                student.getResearcherRole().printPapers(switch (s) {
                    case "2" -> SortStrategy.BY_DATE;
                    case "3" -> SortStrategy.BY_LENGTH;
                    default  -> SortStrategy.BY_CITATIONS;
                });
            }
            default -> System.out.println("Invalid option.");
        }
    }

    // ==================== SEED DATA ====================
    private static void seedData() {
        if (!db.getUsers().isEmpty()) return;

        // Admin
        Admin admin = new Admin("admin", "admin123", "admin@uni.edu", "System", "Admin", "IT", 80000);
        db.addUser(admin);

        // Professor (auto-gets Researcher role)
        Teacher prof = new Teacher("prof.smith", "pass123", "smith@uni.edu",
                "John", "Smith", "CS", 70000, TeacherTitle.PROFESSOR);
        prof.getResearcherRole().setHIndex(5); // professor's h-index = 5
        db.addUser(prof);

        // Tutor (not a researcher by default)
        Teacher tutor = new Teacher("tutor.jones", "pass123", "jones@uni.edu",
                "Alice", "Jones", "CS", 45000, TeacherTitle.TUTOR);
        // Tutor voluntarily becomes a researcher with h-index=2
        tutor.setResearcherRole(new Researcher(tutor.getFullName(), 2));
        db.addUser(tutor);

        // Manager
        Manager manager = new Manager("manager1", "pass123", "manager@uni.edu",
                "Bob", "Manager", "OR", 65000, ManagerType.OR);
        db.addUser(manager);

        // Students
        Student s1 = new Student("student1", "pass123", "s1@uni.edu", "Emma", "Brown", 2);
        Student s4 = new Student("student4", "pass123", "s4@uni.edu", "Liam", "White", 4);
        db.addUser(s1);
        db.addUser(s4);

        // s1 is also a researcher (Decorator pattern demo)
        Researcher baseResearcher = new Researcher(s1.getFullName(), 0);
        ResearcherDecorator decoratedResearcher = new ResearcherDecorator(baseResearcher);
        s1.enableResearcherRole(decoratedResearcher);

        // 4th year student gets a supervisor
        try { s4.setResearchSupervisor(prof.getResearcherRole()); }
        catch (LowResearcherException ex) { System.out.println("Seed error: " + ex.getMessage()); }

        // Courses
        Course cs101 = new Course("CS101 - Intro to CS", 3, 30, 1, "CS");
        Course cs301 = new Course("CS301 - Algorithms", 4, 25, 3, "CS");
        cs101.setAvailable(true); cs301.setAvailable(true);
        cs101.assignTeacher(prof); cs101.assignTeacher(tutor);
        cs301.assignTeacher(prof);
        db.addCourse(cs101); db.addCourse(cs301);

        // Research papers
        ResearchPaper paper1 = new ResearchPaper(
                "10.1109/TNNLS.2022.1234", "Deep Learning in NLP",
                List.of("John Smith"), "IEEE Transactions on NLP",
                150, 12, LocalDate.of(2022, 5, 10), "Accuracy 97%");

        ResearchPaper paper2 = new ResearchPaper(
                "10.1145/acm.2023.5678", "Quantum Computing Survey",
                List.of("John Smith", "Alice Jones"), "ACM Computing Surveys",
                80, 20, LocalDate.of(2023, 3, 15), "Survey paper");

        prof.getResearcherRole().addPaper(paper1);
        prof.getResearcherRole().addPaper(paper2);

        // Research project
        ResearchProject project = new ResearchProject("AI for Healthcare");
        try {
            project.addParticipants(prof.getResearcherRole());
            prof.getResearcherRole().addParticipants(project);
        } catch (NotResearcherException | LowResearcherException ex) {
            System.out.println("Seed error: " + ex.getMessage());
        }
        db.addResearchProject(project);

        System.out.println("=== Demo data seeded ===");
        System.out.println("  admin/admin123  |  prof.smith/pass123  |  tutor.jones/pass123");
        System.out.println("  manager1/pass123  |  student1/pass123  |  student4/pass123");
        System.out.println("=============================================");
    }
}
