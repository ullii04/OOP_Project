package university.model;

import university.enums.DayOfWeek;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Schedule stores day, start/end time, semester.
 * getDuration() returns session length in minutes — as in UML: +getDuration(): int
 */
public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private int semester;

    public Schedule(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, int semester) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.semester = semester;
    }

    /**
     * Returns duration in minutes. As per UML: +getDuration(): int
     */
    public int getDuration() {
        return (int) java.time.Duration.between(startTime, endTime).toMinutes();
    }

    public boolean conflictsWith(Schedule other) {
        if (this.dayOfWeek != other.dayOfWeek) return false;
        return !this.endTime.isBefore(other.startTime) && !other.endTime.isBefore(this.startTime);
    }

    // Getters
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public int getSemester() { return semester; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Schedule)) return false;
        Schedule s = (Schedule) o;
        return semester == s.semester && dayOfWeek == s.dayOfWeek
                && Objects.equals(startTime, s.startTime) && Objects.equals(endTime, s.endTime);
    }

    @Override
    public int hashCode() { return Objects.hash(dayOfWeek, startTime, endTime, semester); }

    @Override
    public String toString() {
        return dayOfWeek + " " + startTime + "-" + endTime + " (" + getDuration() + " min, Sem " + semester + ")";
    }
}
