package university.model;

import java.io.Serializable;
import java.util.*;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseId;
    private String name;
    private int credits;
    private int maxStudents;
    private int year;
    private String major;
    private List<Teacher> instructors;
    private List<Lesson> lessons;
    private boolean isAvailable;
    private List<Student> students;

    public Course(String name, int credits, int maxStudents, int year, String major) {
        this.courseId = "CRS-" + System.currentTimeMillis();
        this.name = name;
        this.credits = credits;
        this.maxStudents = maxStudents;
        this.year = year;
        this.major = major;
        this.instructors = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.isAvailable = true;
        this.students = new ArrayList<>();
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public void assignTeacher(Teacher teacher) {
        if (!instructors.contains(teacher)) {
            instructors.add(teacher);
        }
    }

    public List<Student> getStudents() { return students; }

    public void enrollStudent(Student student) {
        if (students.size() < maxStudents && !students.contains(student)) {
            students.add(student);
        }
    }

    public void removeStudent(Student student) { students.remove(student); }

    // Getters
    public String getCourseId() { return courseId; }
    public String getName() { return name; }
    public int getCredits() { return credits; }
    public int getMaxStudents() { return maxStudents; }
    public int getYear() { return year; }
    public String getMajor() { return major; }
    public List<Teacher> getInstructors() { return instructors; }
    public List<Lesson> getLessons() { return lessons; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }

    @Override
    public int hashCode() { return Objects.hash(courseId); }

    @Override
    public String toString() {
        return "Course{name='" + name + "', credits=" + credits + ", year=" + year + ", major='" + major + "'}";
    }
}
