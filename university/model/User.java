package university.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import university.exceptions.AuthenticationException;
import university.interfaces.Authenticatable;
import university.interfaces.Printable;
import university.utils.ValidationUtils;


public abstract class User implements Authenticatable, Printable, Serializable {
    private static final long serialVersionUID = 1L;

    private final String userId;
    private String username;

    protected String firstName;
    protected String lastName;

    private String email;
    private String passwordHash;
    private boolean loggedIn = false;
    protected final LocalDate createdAt;

    // Old constructor for compatibility
    public User(String username, String password, String email) {
    ValidationUtils.validateEmail(email);
    ValidationUtils.validatePassword(password, username, null, null, email);

    this.userId = UUID.randomUUID().toString();
    this.username = username;
    this.firstName = username;
    this.lastName = "";
    setEmail(email);
    this.passwordHash = hashPassword(password);
    this.createdAt = LocalDate.now();
}
    // Constructor used by Student, Teacher, Employee, Manager
    public User(String username, String password, String email, String firstName, String lastName) {
    ValidationUtils.validateName(firstName, "First name");
    ValidationUtils.validateName(lastName, "Last name");
    ValidationUtils.validateEmail(email);
    ValidationUtils.validatePassword(password, username, firstName, lastName, email);

    this.userId = UUID.randomUUID().toString();
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    setEmail(email);
    this.passwordHash = hashPassword(password);
    this.createdAt = LocalDate.now();
}

    // UML-style constructor
    public User(String firstName, String lastName, String email, String password) {
    ValidationUtils.validateName(firstName, "First name");
    ValidationUtils.validateName(lastName, "Last name");
    ValidationUtils.validateEmail(email);

    String generatedUsername = generateUsername(firstName, lastName);
    ValidationUtils.validatePassword(password, generatedUsername, firstName, lastName, email);

    this.userId = UUID.randomUUID().toString();
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = generatedUsername;
    setEmail(email);
    this.passwordHash = hashPassword(password);
    this.createdAt = LocalDate.now();
}

   @Override
public boolean login(String emailOrUsername, String password) throws AuthenticationException {
    emailOrUsername = emailOrUsername.trim();
    password = password.trim();

    boolean usernameMatches = username != null && username.equalsIgnoreCase(emailOrUsername);
    boolean emailMatches = email != null && email.equalsIgnoreCase(emailOrUsername);

    if ((!usernameMatches && !emailMatches) || !passwordHash.equals(hashPassword(password))) {
        throw new AuthenticationException("Invalid username/email or password.");
    }

    loggedIn = true;
    return true;
}

    @Override
    public void logout() {
        loggedIn = false;
        System.out.println(getFullName() + " logged out.");
    }

    @Override
public void changePassword(String oldPassword, String newPassword) throws AuthenticationException {
    if (!this.passwordHash.equals(hashPassword(oldPassword))) {
        throw new AuthenticationException("Old password is incorrect.");
    }

    ValidationUtils.validatePassword(newPassword, username, firstName, lastName, email);

    this.passwordHash = hashPassword(newPassword);
}

    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }

    public static boolean validateEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    private String hashPassword(String password) {
        return Integer.toHexString(Objects.hash(password));
    }

    private String generateUsername(String firstName, String lastName) {
        if (lastName == null || lastName.isBlank()) {
            return firstName.toLowerCase();
        }

        return (firstName + "." + lastName).toLowerCase();
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.username = generateUsername(this.firstName, this.lastName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.username = generateUsername(this.firstName, this.lastName);
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
    ValidationUtils.validateEmail(email);
    this.email = email;
}

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getFullName() {
        if (lastName == null || lastName.isBlank()) {
            return firstName;
        }

        return firstName + " " + lastName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    // For old code compatibility
    public String getPassword() {
        return passwordHash;
    }

    // For old code compatibility
    public void setPassword(String password) {
    ValidationUtils.validatePassword(password, username, firstName, lastName, email);
    this.passwordHash = hashPassword(password);
}

    public void showMenu() {
        System.out.println("=== User Menu ===");
        System.out.println("1. View profile");
        System.out.println("0. Logout");
    }

    @Override
    public void printInfo() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", loggedIn=" + loggedIn +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}