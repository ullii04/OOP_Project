package university.utils;

import university.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Centralized data storage — Singleton pattern.
 * Handles serialization/deserialization of all university data.
 */
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
        if (!file.exists()) return null;
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

    // User operations
    public void addUser(User user) { users.add(user); }
    public void removeUser(User user) { users.remove(user); }
    public List<User> getUsers() { return users; }

    public User findUserByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    // Course operations
    public void addCourse(Course course) { courses.add(course); }
    public List<Course> getCourses() { return courses; }
    public Course findCourseByName(String name) {
        return courses.stream().filter(c -> c.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    // Research project operations
    public void addResearchProject(ResearchProject project) { researchProjects.add(project); }
    public List<ResearchProject> getResearchProjects() { return researchProjects; }

    // Enrollment operations
    public void addEnrollment(Enrollment enrollment) { enrollments.add(enrollment); }
    public List<Enrollment> getEnrollments() { return enrollments; }

    /** Get all Student users */
    public List<Student> getStudents() {
        return users.stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .collect(Collectors.toList());
    }

    /** Get all Teacher users */
    public List<Teacher> getTeachers() {
        return users.stream()
                .filter(u -> u instanceof Teacher)
                .map(u -> (Teacher) u)
                .collect(Collectors.toList());
    }

    /**
     * Get all Researcher objects across the university.
     * Collects from Teachers (if isResearcher), Students (if isResearcher), and any Employee with researcher role.
     */
    public List<Researcher> getAllResearchers() {
        List<Researcher> researchers = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Teacher t && t.isResearcher()) {
                researchers.add(t.getResearcherRole());
            } else if (u instanceof Student s && s.isResearcher()) {
                researchers.add(s.getResearcherRole());
            }
        }
        return researchers;
    }
}
