package university.model;
import java.util.List;
import java.util.Objects;
import university.interfaces.Observer;
import university.patterns.Logger;
import university.utils.DataStorage;

public class Admin extends User implements Observer {
    private static final long serialVersionUID = 1L;

    private final int accessLevel;
    private final String logFilePath;

    public Admin(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
        this.accessLevel = 5;
        this.logFilePath = "system.log";
    }

    public Admin(String firstName, String lastName, String email, String password,
                 int accessLevel, String logFilePath) {
        super(firstName, lastName, email, password);

        if (accessLevel < 1 || accessLevel > 5) {
            throw new IllegalArgumentException("Access level must be between 1 and 5.");
        }

        this.accessLevel = accessLevel;
        this.logFilePath = logFilePath;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public void addUser(User user) {
        DataStorage.getInstance().addUser(user);
        Logger.getInstance().log(getFullName() + " added user: " + user.getFullName());
        System.out.println("User added: " + user.getFullName());
    }
    public void addUser(List<User> users, User user) {
    users.add(user);
    Logger.getInstance().log(getFullName() + " added user: " + user.getFullName());
    System.out.println("User added: " + user.getFullName());
}

public void removeUser(List<User> users, User user) {
    users.remove(user);
    Logger.getInstance().log(getFullName() + " removed user: " + user.getFullName());
    System.out.println("User removed: " + user.getFullName());
}

public void updateUser(User user, String newEmail) {
    user.setEmail(newEmail);
    Logger.getInstance().log(getFullName() + " updated email for user: " + user.getFullName());
    System.out.println("User updated: " + user.getFullName());
}

public void manageUsers(List<User> users) {
    System.out.println("=== All Users ===");

    if (users == null || users.isEmpty()) {
        System.out.println("No users found.");
        return;
    }

    for (User user : users) {
        System.out.println(user);
    }
}

    public void removeUser(String userId) {
        User target = DataStorage.getInstance().findUserById(userId);

        if (target == null) {
            System.out.println("User not found.");
            return;
        }

        DataStorage.getInstance().removeUser(target);
        Logger.getInstance().log(getFullName() + " removed user: " + target.getFullName());
        System.out.println("User removed: " + target.getFullName());
    }

    public void updateUser(User user) {
        Logger.getInstance().log(getFullName() + " updated user: " + user.getFullName());
        System.out.println("User updated: " + user.getFullName());
    }

    public List<String> viewLogs() {
        System.out.println("=== System Logs ===");

        List<String> logs = Logger.getInstance().getLogs();

        if (logs.isEmpty()) {
            System.out.println("No logs found.");
        } else {
            for (String log : logs) {
                System.out.println(log);
            }
        }

        return logs;
    }

    public void clearLogs() {
        Logger.getInstance().clearLogs();
        System.out.println("Logs cleared.");
    }

    @Override
    public void update(String event) {
        Logger.getInstance().log("Admin received event: " + event);
    }

    @Override
    public void showMenu() {
        System.out.println("=== Admin Menu ===");
        System.out.println("1. View all users");
        System.out.println("2. Remove user");
        System.out.println("3. View logs");
        System.out.println("4. Clear logs");
        System.out.println("5. Save data");
        System.out.println("0. Logout");
    }

    @Override
    public void printInfo() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "userId='" + getUserId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", accessLevel=" + accessLevel +
                ", logFilePath='" + logFilePath + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admin)) return false;
        if (!super.equals(o)) return false;

        Admin admin = (Admin) o;
        return accessLevel == admin.accessLevel &&
                Objects.equals(logFilePath, admin.logFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), accessLevel, logFilePath);
    }
}
