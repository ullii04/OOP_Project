package university.classes;

import university.enums.LessonType;

import java.io.Serializable;
import java.util.Objects;

public class Lesson implements Serializable, Comparable<Lesson> {

    private static final long serialVersionUID = 1L;

    private final String lessonId;
    private String name;
    private LessonType type;
    private boolean available;
    private Room room;
    private Schedule schedule;

    public Lesson(String name, LessonType type, Room room, Schedule schedule) {
        this.lessonId = "LES-" + System.currentTimeMillis();
        this.name = name;
        this.type = type;
        this.room = room;
        this.schedule = schedule;
        this.available = true;
    }

    public double getTotal() {
        return 0;
    }

    public boolean isPassed() {
        return available;
    }

    @Override
    public int compareTo(Lesson other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Lesson)) {
            return false;
        }

        Lesson lesson = (Lesson) object;
        return Objects.equals(lessonId, lesson.lessonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId);
    }

    @Override
    public String toString() {
        return "Lesson{name='" + name +
                "', type=" + type +
                ", room=" + room +
                ", schedule=" + schedule + "}";
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getName() {
        return name;
    }

    public LessonType getType() {
        return type;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Room getRoom() {
        return room;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}