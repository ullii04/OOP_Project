package university.utils;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import university.classes.*;
import university.model.*;

public class DataStorage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "university_data.ser";
    private static DataStorage instance;
    private List<User> users;
    private List<Course> courses;
    private List<ResearchProject> researchProjects;
    private List<Enrollment> enrollments;

    private DataStorage() {
        users = new ArrayList<>();
        courses = new ArrayList<>();
        researchProjects = new ArrayList<>();
        enrollments = new ArrayList<>();
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = loadFromFile();

            if (instance == null) {
                instance = new DataStorage();
            }
        }

        return instance;
    }

    private static DataStorage loadFromFile() {
        File file = new File(DATA_FILE);

        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (DataStorage) ois.readObject();
        } catch (Exception e) {
            System.out.println("Could not load data: " + e.getMessage());
            return null;
        }
    }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        if (findUserByEmail(user.getEmail()) != null) {
            System.out.println("User with this email already exists.");
            return;
        }

        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public User findUserById(String userId) {
        if (userId == null) {
            return null;
        }

        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }

        return null;
    }

    public User findUserByEmail(String email) {
        if (email == null) {
            return null;
        }

        String cleanEmail = email.trim();

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(cleanEmail)) {
                return user;
            }
        }

        return null;
    }

    public User findUserByUsername(String username) {
        if (username == null) {
            return null;
        }

        String cleanUsername = username.trim();

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(cleanUsername)) {
                return user;
            }
        }

        return null;
    }

    public void addCourse(Course course) {
        if (course != null) {
            courses.add(course);
        }
    }

    public List<Course> getCourses() {
        return courses;
    }

    public Course findCourseByName(String name) {
        if (name == null) {
            return null;
        }

        String cleanName = name.trim();

        for (Course course : courses) {
            if (course.getName().equalsIgnoreCase(cleanName)) {
                return course;
            }
        }

        return null;
    }

    public void addResearchProject(ResearchProject project) {
        if (project != null) {
            researchProjects.add(project);
        }
    }

    public List<ResearchProject> getResearchProjects() {
        return researchProjects;
    }

    public void addEnrollment(Enrollment enrollment) {
        if (enrollment != null) {
            enrollments.add(enrollment);
        }
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Student student) {
                students.add(student);
            }
        }

        return students;
    }

    public List<Teacher> getTeachers() {
        List<Teacher> teachers = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Teacher teacher) {
                teachers.add(teacher);
            }
        }

        return teachers;
    }

    public List<Researcher> getAllResearchers() {
        List<Researcher> researchers = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Student student && student.isResearcher()) {
                researchers.add(student.getResearcherRole());
            }

            if (user instanceof Teacher teacher && teacher.isResearcher()) {
                researchers.add(teacher.getResearcherProfile());
            }
        }

        return researchers;
    }
}
