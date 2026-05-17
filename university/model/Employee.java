package university.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import university.interfaces.Observer;
import university.patterns.Logger;

public abstract class Employee extends User implements Observer, Serializable {
    private static final long serialVersionUID = 1L;

    private double salary;
    private final String employeeId;
    private String school;
    private List<String> inbox;

    
    public Employee(String username, String password, String email,
                    String firstName, String lastName, String school, double salary) {
        super(username, password, email, firstName, lastName);
        
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }
        
        this.employeeId = "EMP-" + System.currentTimeMillis();
        this.school = school;
        this.salary = salary;
        this.inbox = new ArrayList<>();
    }

    
    public Employee(String firstName, String lastName, String email, String password, 
                    String school, double salary) {
        super(firstName, lastName, email, password);
        
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }
        
        this.employeeId = "EMP-" + System.currentTimeMillis();
        this.school = school;
        this.salary = salary;
        this.inbox = new ArrayList<>();
    }

    
    public void sendMessage(Employee recipient, String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message content cannot be empty.");
        }
        if (recipient == null) {
            throw new IllegalArgumentException("Recipient cannot be null.");
        }
        
        String msg = "[From " + getFullName() + "]: " + message;
        recipient.receiveMessage(msg);
        Logger.getInstance().log(getUsername() + " sent message to " + recipient.getUsername());
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
        if (complaint == null || complaint.isBlank()) {
            throw new IllegalArgumentException("Complaint content cannot be empty.");
        }
        if (recipient == null) {
            throw new IllegalArgumentException("Recipient cannot be null.");
        }
        
        String msg = "[COMPLAINT from " + getFullName() + "]: " + complaint;
        recipient.receiveMessage(msg);
        Logger.getInstance().log(getUsername() + " sent complaint to " + recipient.getUsername());
    }

    
    @Override
    public void update(String event) {
        String notification = "[📢 System Event]: " + event;
        receiveMessage(notification); 
        Logger.getInstance().log("Employee " + getUsername() + " auto-notified of event: " + event);
    }

    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { super.setFirstName(firstName); }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { super.setLastName(lastName); }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { 
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }
        this.salary = salary; 
    }
    
    public String getEmployeeId() { return employeeId; }
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    public List<String> getInbox() { return inbox; }

    @Override
    public void printInfo() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "userId='" + getUserId() + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", school='" + school + '\'' +
                ", salary=" + salary +
                '}';
    }
}
