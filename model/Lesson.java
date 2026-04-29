package university.model;

import university.enums.LessonType;

import java.io.Serializable;
import java.util.Objects;

public class Lesson implements Serializable, Comparable<Lesson> {
    private static final long serialVersionUID = 1L;

    private String lessonId;
    private String name;
    private LessonType type;
    private boolean isAvailable;
    private Room room;
    private Schedule schedule;

    public Lesson(String name, LessonType type, Room room, Schedule schedule) {
        this.lessonId = "LES-" + System.currentTimeMillis();
        this.name = name;
        this.type = type;
        this.isAvailable = true;
        this.room = room;
        this.schedule = schedule;
    }

    public double getTotal() { return 0; }

    public boolean isPassed() { return isAvailable; }

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
    public int hashCode() { return Objects.hash(lessonId); }

    @Override
    public String toString() {
        return "Lesson{name='" + name + "', type=" + type + ", room=" + room + ", schedule=" + schedule + "}";
    }

    public String getLessonId() { return lessonId; }
    public String getName() { return name; }
    public LessonType getType() { return type; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public Room getRoom() { return room; }
    public Schedule getSchedule() { return schedule; }
}
