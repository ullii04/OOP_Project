package university.model;

import university.enums.LessonType;
import university.classes.Room;
import university.classes.Schedule;
import university.exceptions.GradeRequirementException; 
import java.io.Serializable;
import java.util.Objects;

public class Lesson implements Serializable, Comparable<Lesson> {
    private static final long serialVersionUID = 1L;

    private final String lessonId; 
    private String name;
    private LessonType type;
    private boolean isAvailable;
    private Room room;
    private Schedule schedule;
    private double maxCapacityScore; 

    
    public Lesson(String name, LessonType type, Room room, Schedule schedule) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Lesson name cannot be empty.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Lesson type cannot be null.");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null.");
        }
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule cannot be null.");
        }

        this.lessonId = "LES-" + System.currentTimeMillis();
        this.name = name;
        this.type = type;
        this.isAvailable = true;
        this.room = room;
        this.schedule = schedule;
        this.maxCapacityScore = 100.0; 
    }

   
    public void checkStudentEligibility(Student student) throws GradeRequirementException {
        if (this.type == LessonType.LABORATORY && student.getGpa() < 2.0) {
            throw new GradeRequirementException("Student " + student.getFullName() + 
                " does not meet the minimum GPA requirement (2.0) to attend laboratory: " + this.name);
        }
        
        if (!isAvailable) {
            throw new GradeRequirementException("Lesson " + this.name + " is currently unavailable or full.");
        }
    }

  
    public double getTotal() { 
        return maxCapacityScore; 
    }

    public void setMaxCapacityScore(double score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100.");
        }
        this.maxCapacityScore = score;
    }

    public boolean isPassed() { 
        return isAvailable; 
    }

    @Override
    public int compareTo(Lesson other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson)) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(lessonId, lesson.lessonId);
    }

    @Override
    public int hashCode() { 
        return Objects.hash(lessonId); 
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id='" + lessonId + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", room=" + (room != null ? room.getRoomNumber() : "N/A") +
                ", isAvailable=" + isAvailable +
                '}';
    }

    // Getters & Setters
    public String getLessonId() { return lessonId; }
    
    public String getName() { return name; }
    public void setName(String name) { 
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty.");
        this.name = name; 
    }
    
    public LessonType getType() { return type; }
    public void setType(LessonType type) { this.type = type; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
    
    public Schedule getSchedule() { return schedule; }
    public void setSchedule(Schedule schedule) { this.schedule = schedule; }
}
