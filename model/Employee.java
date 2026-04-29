package university.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Employee extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    private double salary;
    private String firstName;
    private String lastName;
    private String employeeId;
    private String school;
    private List<String> inbox;

    public Employee(String username, String password, String email,
                    String firstName, String lastName, String school, double salary) {
        super(username, password, email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = "EMP-" + System.currentTimeMillis();
        this.school = school;
        this.salary = salary;
        this.inbox = new ArrayList<>();
    }

    public void sendMessage(Employee recipient, String message) {
        String msg = "[From " + getFullName() + "]: " + message;
        recipient.receiveMessage(msg);
        university.patterns.Logger.getInstance().log(getUsername() + " sent message to " + recipient.getUsername());
    }

    public void receiveMessage(String message) {
        inbox.add(message);
    }

    public void viewInbox() {
        if (inbox.isEmpty()) {
            System.out.println("Inbox is empty.");
        } else {
            System.out.println("=== Inbox for " + getFullName() + " ===");
            inbox.forEach(m -> System.out.println("  " + m));
        }
    }

    public void sendComplaint(Employee recipient, String complaint) {
        String msg = "[COMPLAINT from " + getFullName() + "]: " + complaint;
        recipient.receiveMessage(msg);
        university.patterns.Logger.getInstance().log(getUsername() + " sent complaint.");
    }

    public String getFullName() { return firstName + " " + lastName; }

    // Getters & Setters
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmployeeId() { return employeeId; }
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    public List<String> getInbox() { return inbox; }

    @Override
    public String toString() {
        return "Employee{name='" + getFullName() + "', school='" + school + "', salary=" + salary + "}";
    }
}
