package university.classes;

import university.enums.DayOfWeek;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private int semester;

    public Schedule(DayOfWeek dayOfWeek,
                    LocalTime startTime,
                    LocalTime endTime,
                    int semester) {

        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.semester = semester;
    }

    public int getDuration() {
        return (int) Duration
                .between(startTime, endTime)
                .toMinutes();
    }

    public boolean conflictsWith(Schedule other) {

        if (this.dayOfWeek != other.dayOfWeek) {
            return false;
        }

        return !this.endTime.isBefore(other.startTime)
                && !other.endTime.isBefore(this.startTime);
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getSemester() {
        return semester;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof Schedule)) {
            return false;
        }

        Schedule schedule = (Schedule) object;

        return semester == schedule.semester
                && dayOfWeek == schedule.dayOfWeek
                && Objects.equals(startTime, schedule.startTime)
                && Objects.equals(endTime, schedule.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, startTime, endTime, semester);
    }

    @Override
    public String toString() {

        return dayOfWeek +
                " " +
                startTime +
                "-" +
                endTime +
                " (" +
                getDuration() +
                " min, Sem " +
                semester +
                ")";
    }
}