package university.classes;
import university.enums.StudentYear;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import university.model.*;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String courseId;
    private String name;
    private int credits;
    private int maxStudents;
    private StudentYear year;
    private String major;

    private final List<Teacher> instructors;
    private final List<Lesson> lessons;
    private final List<Student> students;

    private boolean available;

    public Course(String name, int credits, int maxStudents, StudentYear year, String major) {
        if (credits <= 0 || credits > 10) {
            throw new IllegalArgumentException("Credits must be between 1 and 10.");
        }

        if (maxStudents <= 0) {
            throw new IllegalArgumentException("Max students must be positive.");
        }

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
        if (lesson != null && !lessons.contains(lesson)) {
            lessons.add(lesson);
        }
    }

    public void assignTeacher(Teacher teacher) {
        if (teacher != null && !instructors.contains(teacher)) {
            instructors.add(teacher);
        }
    }

    public boolean enrollStudent(Student student) {
        if (!available) {
            return false;
        }

        if (students.size() >= maxStudents) {
            return false;
        }

        if (students.contains(student)) {
            return false;
        }

        students.add(student);
        return true;
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public boolean hasAvailablePlaces() {
        return students.size() < maxStudents;
    }

    public int getAvailablePlaces() {
        return maxStudents - students.size();
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

    public StudentYear getYear() {
        return year;
    }

    public String getMajor() {
        return major;
    }

    public List<Teacher> getInstructors() {
        return Collections.unmodifiableList(instructors);
    }

    public List<Lesson> getLessons() {
        return Collections.unmodifiableList(lessons);
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }

    @Override
    public String toString() {
        return "Course{name='" + name
                + "', credits=" + credits
                + ", year=" + year
                + ", major='" + major
                + "', students=" + students.size() + "/" + maxStudents
                + ", available=" + available
                + "}";
    }
}
