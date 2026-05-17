package university.interfaces;

import university.exceptions.AuthenticationException;

public interface Authenticatable {
    boolean login(String emailOrUsername, String password) throws AuthenticationException;

    void logout();

    void changePassword(String oldPassword, String newPassword) throws AuthenticationException;

    boolean isLoggedIn();
}