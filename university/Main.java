package university;
import university.enums.*;
import university.exceptions.AuthenticationException;
import university.exceptions.LowResearcherException;
import university.exceptions.MaxCreditsExceededException;
import university.exceptions.NotResearcherException;
import university.model.*;
import university.patterns.NewsPublisher;
import university.patterns.SortStrategy;
import university.patterns.UserFactory;
import university.utils.AuthService;
import university.utils.DataStorage;
import university.classes.*;
import java.time.LocalDate;
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

    private static void showLoginMenu() {
        System.out.println("\n1. Login");
        System.out.println("0. Exit");
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

    private static void handleMenu(User user) {
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        if (choice.equals("0")) {
            AuthService.logout();
            return;
        }

        if (user instanceof Admin admin) {
            handleAdminMenu(choice, admin);
        } else if (user instanceof Manager manager) {
            handleManagerMenu(choice, manager);
        } else if (user instanceof Teacher teacher) {
            handleTeacherMenu(choice, teacher);
        } else if (user instanceof Student student) {
            handleStudentMenu(choice, student);
        } else {
            System.out.println("Unknown user type.");
        }
    }


    private static void handleAdminMenu(String choice, Admin admin) {
        switch (choice) {
            case "1" -> {
                System.out.println("a) Add user");
                System.out.println("b) Remove user");
                System.out.println("c) Update user email");
                System.out.print("Choice: ");

                String sub = scanner.nextLine().trim();

                switch (sub) {
                    case "a" -> {
                        System.out.print("Type (STUDENT/TEACHER/MANAGER/ADMIN): ");
                        String type = scanner.nextLine().trim().toUpperCase();

                        System.out.print("Username: ");
                        String username = scanner.nextLine().trim();

                        System.out.print("Password: ");
                        String password = scanner.nextLine().trim();

                        System.out.print("Email: ");
                        String email = scanner.nextLine().trim();

                        System.out.print("First name: ");
                        String firstName = scanner.nextLine().trim();

                        System.out.print("Last name: ");
                        String lastName = scanner.nextLine().trim();

                        System.out.print("School/Department: ");
                        String school = scanner.nextLine().trim();

                        try {
                            UserFactory.UserType userType = UserFactory.UserType.valueOf(type);
                            User newUser = UserFactory.createUser(
                                    userType,
                                    username,
                                    password,
                                    email,
                                    firstName,
                                    lastName,
                                    school
                            );

                            admin.addUser(db.getUsers(), newUser);
                            System.out.println("User added.");
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }

                    case "b" -> {
                        System.out.print("Username to remove: ");
                        String username = scanner.nextLine().trim();

                        User target = db.findUserByUsername(username);

                        if (target != null) {
                            admin.removeUser(db.getUsers(), target);
                            System.out.println("User removed.");
                        } else {
                            System.out.println("User not found.");
                        }
                    }

                    case "c" -> {
                        System.out.print("Username: ");
                        String username = scanner.nextLine().trim();

                        System.out.print("New email: ");
                        String email = scanner.nextLine().trim();

                        User target = db.findUserByUsername(username);

                        if (target != null) {
                            admin.updateUser(target, email);
                            System.out.println("User updated.");
                        } else {
                            System.out.println("User not found.");
                        }
                    }

                    default -> System.out.println("Invalid option.");
                }
            }

            case "2" -> admin.viewLogs();

            case "3" -> admin.manageUsers(db.getUsers());

            default -> System.out.println("Invalid option.");
        }
    }


    private static void handleManagerMenu(String choice, Manager manager) {
        switch (choice) {
            case "1" -> {
                List<Enrollment> pending = manager.getPendingEnrollments();

                if (pending.isEmpty()) {
                    System.out.println("No pending enrollments.");
                    return;
                }

                for (int i = 0; i < pending.size(); i++) {
                    System.out.println(i + ") " + pending.get(i));
                }

                System.out.print("Enter index to approve or -1 to skip: ");

                try {
                    int index = Integer.parseInt(scanner.nextLine().trim());

                    if (index >= 0 && index < pending.size()) {
                        manager.approveRegistration(pending.get(index));
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number.");
                }
            }

            case "2" -> {
                System.out.print("Course name: ");
                String courseName = scanner.nextLine().trim();

                Course course = db.findCourseByName(courseName);

                if (course != null) {
                    manager.addCourseForRegistration(course);
                    System.out.println("Course opened for registration.");
                } else {
                    System.out.println("Course not found.");
                }
            }

            case "3" -> {
                System.out.print("Course name: ");
                String courseName = scanner.nextLine().trim();

                System.out.print("Teacher username: ");
                String teacherUsername = scanner.nextLine().trim();

                Course course = db.findCourseByName(courseName);
                User user = db.findUserByUsername(teacherUsername);

                if (course != null && user instanceof Teacher teacher) {
                    manager.assignCourseToTeacher(course, teacher);
                    System.out.println("Teacher assigned.");
                } else {
                    System.out.println("Course or teacher not found.");
                }
            }

            case "4" -> manager.generateReport(db.getStudents());

            case "5" -> {
                System.out.print("News text: ");
                String news = scanner.nextLine().trim();

                manager.manageNews(news);
                newsPublisher.publishNews(news);
            }

            case "6" -> manager.viewStudentsSortedByGpa(db.getStudents());

            case "7" -> manager.viewStudentsSortedAlphabetically(db.getStudents());

            default -> System.out.println("Invalid option.");
        }
    }


    private static void handleTeacherMenu(String choice, Teacher teacher) {
        switch (choice) {
            case "1" -> teacher.getCourses().forEach(System.out::println);

            case "2" -> {
                System.out.print("Course name: ");
                String courseName = scanner.nextLine().trim();

                Course course = db.findCourseByName(courseName);

                if (course != null) {
                    teacher.manageCourse(course);
                } else {
                    System.out.println("Course not found.");
                }
            }

            case "3" -> {
                System.out.print("Course name: ");
                String courseName = scanner.nextLine().trim();

                Course course = db.findCourseByName(courseName);

                if (course != null) {
                    teacher.viewStudents(course);
                } else {
                    System.out.println("Course not found.");
                }
            }

            case "4" -> {
                System.out.print("Student username: ");
                String studentUsername = scanner.nextLine().trim();

                System.out.print("Course name: ");
                String courseName = scanner.nextLine().trim();

                User user = db.findUserByUsername(studentUsername);
                Course course = db.findCourseByName(courseName);

                if (user instanceof Student student && course != null) {
                    try {
                        System.out.print("Attestation 1 (0-30): ");
                        double att1 = Double.parseDouble(scanner.nextLine().trim());

                        System.out.print("Attestation 2 (0-30): ");
                        double att2 = Double.parseDouble(scanner.nextLine().trim());

                        System.out.print("Final exam (0-40): ");
                        double finalExam = Double.parseDouble(scanner.nextLine().trim());

                        teacher.putMark(student, course, att1, att2, finalExam);
                        System.out.println("Mark recorded.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid grade input.");
                    }
                } else {
                    System.out.println("Student or course not found.");
                }
            }

            case "5" -> {
                System.out.print("Recipient username: ");
                String recipientUsername = scanner.nextLine().trim();

                User recipient = db.findUserByUsername(recipientUsername);

                if (recipient instanceof Employee employee) {
                    System.out.print("Message: ");
                    String message = scanner.nextLine().trim();

                    teacher.sendMessage(employee, message);
                } else {
                    System.out.println("Recipient not found or not an employee.");
                }
            }

            case "6" -> teacher.viewInbox();

            case "7" -> {
                if (!teacher.isResearcher()) {
                    System.out.println("This teacher is not a researcher.");
                    return;
                }

                System.out.println("Sort by:");
                System.out.println("1) Citations");
                System.out.println("2) Date");
                System.out.println("3) Length");
                System.out.println("4) Title");
                System.out.print("Choice: ");

                String sortChoice = scanner.nextLine().trim();

                teacher.getResearcherProfile().printPapers(switch (sortChoice) {
                    case "2" -> SortStrategy.BY_DATE;
                    case "3" -> SortStrategy.BY_LENGTH;
                    case "4" -> SortStrategy.BY_TITLE;
                    default -> SortStrategy.BY_CITATIONS;
                });
            }

            case "8" -> {
                if (!teacher.isResearcher()) {
                    System.out.println("This teacher is not a researcher.");
                    return;
                }

                System.out.print("Project topic: ");
                String topic = scanner.nextLine().trim();

                ResearchProject project = db.getResearchProjects()
                        .stream()
                        .filter(p -> p.getTopic().equalsIgnoreCase(topic))
                        .findFirst()
                        .orElse(null);

                if (project == null) {
                    project = new ResearchProject(topic);
                    db.addResearchProject(project);
                }

                try {
                    project.addParticipants(teacher.getResearcherProfile());
                    teacher.getResearcherProfile().addProject(project);
                    System.out.println("Joined project: " + topic);
                } catch (NotResearcherException | LowResearcherException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            default -> System.out.println("Invalid option.");
        }
    }


    private static void handleStudentMenu(String choice, Student student) {
        switch (choice) {
            case "1" -> db.getCourses()
                    .stream()
                    .filter(Course::isAvailable)
                    .forEach(System.out::println);

            case "2" -> {
                System.out.print("Course name: ");
                String courseName = scanner.nextLine().trim();

                Course course = db.findCourseByName(courseName);

                if (course != null) {
                    try {
                        Enrollment enrollment = student.registerCourse(course);
                        db.addEnrollment(enrollment);

                        db.getUsers()
                                .stream()
                                .filter(user -> user instanceof Manager)
                                .forEach(user -> ((Manager) user).addPendingEnrollment(enrollment));

                        System.out.println("Registration request sent to manager.");
                    } catch (MaxCreditsExceededException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                } else {
                    System.out.println("Course not found.");
                }
            }

            case "3" -> student.viewMarks();

            case "4" -> student.getTranscript().generate();

            case "5" -> {
                System.out.print("Teacher username: ");
                String teacherUsername = scanner.nextLine().trim();

                User user = db.findUserByUsername(teacherUsername);

                if (user instanceof Teacher teacher) {
                    System.out.print("Rating (1-5): ");

                    try {
                        double rating = Double.parseDouble(scanner.nextLine().trim());
                        student.rateTeacher(teacher, rating);
                        System.out.println("Teacher rated.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid rating.");
                    }
                } else {
                    System.out.println("Teacher not found.");
                }
            }

            case "6" -> {
                System.out.print("Course name: ");
                String courseName = scanner.nextLine().trim();

                Course course = db.findCourseByName(courseName);

                if (course != null) {
                    course.getInstructors().forEach(System.out::println);
                } else {
                    System.out.println("Course not found.");
                }
            }

            case "7" -> {
                if (!student.isResearcher()) {
                    System.out.println("This student is not a researcher.");
                    return;
                }

                System.out.println("Sort by:");
                System.out.println("1) Citations");
                System.out.println("2) Date");
                System.out.println("3) Length");
                System.out.print("Choice: ");

                String sortChoice = scanner.nextLine().trim();

                student.getResearcherProfile().printPapers(switch (sortChoice) {
                    case "2" -> SortStrategy.BY_DATE;
                    case "3" -> SortStrategy.BY_LENGTH;
                    default -> SortStrategy.BY_CITATIONS;
                });
            }

            default -> System.out.println("Invalid option.");
        }
    }


    private static void seedData() {
        if (!db.getUsers().isEmpty()) {
            return;
        }

        Admin admin = new Admin(
                "admin",
                "Pass123!",
                "admin@uni.edu",
                "System",
                "Admin",
                "IT",
                80000
        );
        db.addUser(admin);

        Teacher professor = new Teacher(
                "prof.smith",
                "Pass5678!",
                "smith@uni.edu",
                "John",
                "Smith",
                "CS",
                70000,
                TeacherTitle.PROFESSOR
        );

        if (!professor.isResearcher()) {
            professor.setResearcherProfile(new Researcher(professor.getFullName(), 5));
        }

        professor.getResearcherProfile().setHIndex(5);
        db.addUser(professor);

        Teacher tutor = new Teacher(
                "tutor.jones",
                "Pass1213!",
                "jones@uni.edu",
                "Alice",
                "Jones",
                "CS",
                45000,
                TeacherTitle.TUTOR
        );

        tutor.setResearcherProfile(new Researcher(tutor.getFullName(), 2));
        db.addUser(tutor);

        Manager manager = new Manager(
                "manager1",
                "Pass1234!",
                "manager@uni.edu",
                "Bob",
                "Manager",
                "OR",
                65000,
                ManagerType.OR
        );
        db.addUser(manager);

        Student student1 = new Student(
            "student1",
            "Pass21819!",
            "s1@uni.edu",
            "Emma",
            "Brown",
            2
        );

        Student student4 = new Student(
            "student4",
            "Pass1623!",
            "s4@uni.edu",
            "Liam",
            "White",
            4
        );

        student1.setResearcherProfile(new Researcher(student1.getFullName(), 0));

        try {
            student4.setResearchSupervisor(professor.getResearcherProfile());
        } catch (LowResearcherException e) {
            System.out.println("Seed error: " + e.getMessage());
        }

        db.addUser(student1);
        db.addUser(student4);

        Course cs101 = new Course(
                "CS101 - Intro to CS",
                3,
                30,
                StudentYear.FIRST,
                "CS"
        );

        Course cs301 = new Course(
                "CS301 - Algorithms",
                4,
                25,
                StudentYear.THIRD,
                "CS"
        );

        cs101.setAvailable(true);
        cs301.setAvailable(true);

        cs101.assignTeacher(professor);
        cs101.assignTeacher(tutor);
        cs301.assignTeacher(professor);

        db.addCourse(cs101);
        db.addCourse(cs301);

        ResearchPaper paper1 = new ResearchPaper(
                "10.1109/TNNLS.2022.1234",
                "Deep Learning in NLP",
                List.of("John Smith"),
                "IEEE Transactions on NLP",
                150,
                12,
                LocalDate.of(2022, 5, 10),
                "Accuracy 97%"
        );

        ResearchPaper paper2 = new ResearchPaper(
                "10.1145/acm.2023.5678",
                "Quantum Computing Survey",
                List.of("John Smith", "Alice Jones"),
                "ACM Computing Surveys",
                80,
                20,
                LocalDate.of(2023, 3, 15),
                "Survey paper"
        );

        professor.getResearcherProfile().addPaper(paper1);
        professor.getResearcherProfile().addPaper(paper2);

        ResearchProject project = new ResearchProject("AI for Healthcare");

        try {
            project.addParticipants(professor.getResearcherProfile());
        } catch (NotResearcherException e) {
            System.out.println("Seed error: " + e.getMessage());
        }

        db.addResearchProject(project);

        System.out.println("=== Demo data seeded ===");
        System.out.println("admin/Pass123!");
        System.out.println("prof.smith/Pass5678!");
        System.out.println("tutor.jones/Pass1213!");
        System.out.println("manager1/Pass1234!");
        System.out.println("student1/Pass1819!");
        System.out.println("student4/Pass1623!");
        System.out.println("=============================================");
    }
}
