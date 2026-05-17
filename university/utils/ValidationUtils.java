package university.utils;

import university.exceptions.InvalidEmailException;
import university.exceptions.WeakPasswordException;
import university.exceptions.ValidationException;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static void validateName(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty.");
        }

        if (value.length() < 2) {
            throw new ValidationException(fieldName + " must contain at least 2 characters.");
        }

        if (!value.matches("[A-Za-zА-Яа-я.]+")) {
            throw new ValidationException(fieldName + " must contain only letters.");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidEmailException("Email cannot be empty.");
        }

        email = email.trim();

        if (!email.contains("@")) {
            throw new InvalidEmailException("Email must contain '@'.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidEmailException("Invalid email format. Example: student@kbtu.kz");
        }
    }

    public static void validatePassword(String password, String username, String firstName, String lastName, String email) {
        if (password == null || password.isEmpty()) {
            throw new WeakPasswordException("Password cannot be empty.");
        }

        if (password.length() < 8) {
            throw new WeakPasswordException("Password must contain at least 8 characters.");
        }

        if (password.contains(" ")) {
            throw new WeakPasswordException("Password must not contain spaces.");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new WeakPasswordException("Password must contain at least one uppercase letter.");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new WeakPasswordException("Password must contain at least one lowercase letter.");
        }

        if (!password.matches(".*[0-9].*")) {
            throw new WeakPasswordException("Password must contain at least one digit.");
        }

        if (!password.matches(".*[^A-Za-z0-9].*")) {
            throw new WeakPasswordException("Password must contain at least one special character.");
        }

        String lowerPassword = password.toLowerCase();

        if (username != null && !username.isBlank()
                && lowerPassword.contains(username.toLowerCase())) {
            throw new WeakPasswordException("Password must not contain username.");
        }

        if (firstName != null && !firstName.isBlank()
                && lowerPassword.contains(firstName.toLowerCase())) {
            throw new WeakPasswordException("Password must not contain first name.");
        }

        if (lastName != null && !lastName.isBlank()
                && lowerPassword.contains(lastName.toLowerCase())) {
            throw new WeakPasswordException("Password must not contain last name.");
        }

        if (email != null && email.contains("@")) {
            String emailName = email.substring(0, email.indexOf("@")).toLowerCase();

            if (!emailName.isBlank() && lowerPassword.contains(emailName)) {
                throw new WeakPasswordException("Password must not contain email name.");
            }
        }

        String[] commonPasswords = {
                "password",
                "password123",
                "admin",
                "admin123",
                "qwerty",
                "qwerty123",
                "123456",
                "12345678",
                "11111111"
        };

        for (String weak : commonPasswords) {
            if (lowerPassword.equals(weak)) {
                throw new WeakPasswordException("Password is too common and easy to guess.");
            }
        }
    }
}