package university.classes;

import java.io.Serializable;
import java.util.Objects;

public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    private String roomNumber;
    private String building;
    private int capacity;
    private String type;

    public Room(String roomNumber,
                String building,
                int capacity,
                String type) {

        this.roomNumber = roomNumber;
        this.building = building;
        this.capacity = capacity;
        this.type = type;
    }

    public boolean isAvailable(Schedule schedule) {
        return true;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getBuilding() {
        return building;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof Room)) {
            return false;
        }

        Room room = (Room) object;

        return Objects.equals(roomNumber, room.roomNumber) &&
               Objects.equals(building, room.building);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber, building);
    }

    @Override
    public String toString() {

        return "Room{" +
                building +
                "-" +
                roomNumber +
                ", capacity=" +
                capacity +
                ", type=" +
                type +
                "}";
    }
}