package university.model;

import university.patterns.Logger;

import java.io.Serializable;
import java.util.List;

public class Admin extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    public Admin(String username, String password, String email,
                 String firstName, String lastName, String school, double salary) {
        super(username, password, email, firstName, lastName, school, salary);
    }

    public void manageUsers(List<User> users) {
        System.out.println("=== All Users ===");
        users.forEach(System.out::println);
    }

    public void addUser(List<User> users, User user) {
        users.add(user);
        Logger.getInstance().log(getUsername() + " added user: " + user.getUsername());
        System.out.println("User added: " + user.getUsername());
    }

    public void removeUser(List<User> users, User user) {
        users.remove(user);
        Logger.getInstance().log(getUsername() + " removed user: " + user.getUsername());
        System.out.println("User removed: " + user.getUsername());
    }

    public void updateUser(User user, String newEmail) {
        user.setEmail(newEmail);
        Logger.getInstance().log(getUsername() + " updated user: " + user.getUsername());
        System.out.println("User updated: " + user.getUsername());
    }

    public void viewLogs() {
        System.out.println("=== System Logs ===");
        Logger.getInstance().getLogs().forEach(System.out::println);
    }

    @Override
    public void showMenu() {
        System.out.println("=== Admin Menu ===");
        System.out.println("1. Manage Users (Add/Remove/Update)");
        System.out.println("2. View System Logs");
        System.out.println("3. View All Users");
        System.out.println("0. Logout");
    }

    @Override
    public String toString() {
        return "Admin{name='" + getFullName() + "'}";
    }
}
