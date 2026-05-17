package university.utils;

import university.exceptions.AuthenticationException;
import university.model.User;
import university.patterns.Logger;

public class AuthService {
    private static User currentUser;

    private AuthService() {
    }

    public static User login(String emailOrUsername, String password) throws AuthenticationException {
    emailOrUsername = emailOrUsername.trim();
    password = password.trim();

    User user = DataStorage.getInstance().findUserByUsername(emailOrUsername);

    if (user == null) {
        user = DataStorage.getInstance().findUserByEmail(emailOrUsername);
    }

    if (user == null) {
        throw new AuthenticationException("User not found: " + emailOrUsername);
    }

    user.login(emailOrUsername, password);
    currentUser = user;

    Logger.getInstance().log("LOGIN: " + user.getEmail());
    System.out.println("Welcome, " + user.getFullName() + "!");

    return user;
}

    public static void logout() {
        if (currentUser != null) {
            Logger.getInstance().log("LOGOUT: " + currentUser.getEmail());
            currentUser.logout();
            currentUser = null;
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null && currentUser.isLoggedIn();
    }
}