package university.utils;

import university.exceptions.AuthenticationException;
import university.model.User;
import university.patterns.Logger;

public class AuthService {
    private static User currentUser;

    public static User login(String username, String password) throws AuthenticationException {
        User user = DataStorage.getInstance().findUserByUsername(username);
        if (user == null) {
            throw new AuthenticationException("User not found: " + username);
        }
        user.login(username, password); // throws AuthenticationException if fails
        currentUser = user;
        Logger.getInstance().log("LOGIN: " + username);
        System.out.println("Welcome, " + username + "!");
        return user;
    }

    public static void logout() {
        if (currentUser != null) {
            Logger.getInstance().log("LOGOUT: " + currentUser.getUsername());
            currentUser.logout();
            currentUser = null;
        }
    }

    public static User getCurrentUser() { return currentUser; }
    public static boolean isLoggedIn() { return currentUser != null; }
}
