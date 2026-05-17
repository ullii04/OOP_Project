package university.classes;

import university.model.Student;
import university.model.Teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String courseId;
    private String name;
    private int credits;
    private int maxStudents;
    private int year;
    private String major;

    private List<Teacher> instructors;
    private List<Lesson> lessons;
    private boolean available;
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
        this.students = new ArrayList<>();
        this.available = true;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public void assignTeacher(Teacher teacher) {
        if (!instructors.contains(teacher)) {
            instructors.add(teacher);
        }
    }

    public void enrollStudent(Student student) {
        if (students.size() < maxStudents && !students.contains(student)) {
            students.add(student);
        }
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public String getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public int getYear() {
        return year;
    }

    public String getMajor() {
        return major;
    }

    public List<Teacher> getInstructors() {
        return instructors;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public List<Student> getStudents() {
        return students;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Course)) {
            return false;
        }

        Course course = (Course) object;
        return Objects.equals(courseId, course.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }

    @Override
    public String toString() {
        return "Course{name='" + name + "', credits=" + credits +
                ", year=" + year + ", major='" + major + "'}";
    }
}