package university.patterns;

import university.enums.ManagerType;
import university.enums.TeacherTitle;
import university.model.*;

/**
 * Design Pattern #3: Factory Method
 * Creates different User types without exposing instantiation logic to the caller.
 * Caller just says "give me a STUDENT" and the factory handles the rest.
 */
public class UserFactory {

    public enum UserType { STUDENT, TEACHER, MANAGER, ADMIN }

    public static User createUser(UserType type, String username, String password, String email,
                                  String firstName, String lastName, String school, Object... extras) {
        return switch (type) {
            case STUDENT -> {
                int year = extras.length > 0 ? (int) extras[0] : 1;
                yield new Student(username, password, email, firstName, lastName, year);
            }
            case TEACHER -> {
                double salary = extras.length > 0 ? (double) extras[0] : 50000.0;
                TeacherTitle title = extras.length > 1 ? (TeacherTitle) extras[1] : TeacherTitle.LECTURER;
                yield new Teacher(username, password, email, firstName, lastName, school, salary, title);
            }
            case MANAGER -> {
                double salary = extras.length > 0 ? (double) extras[0] : 60000.0;
                ManagerType mType = extras.length > 1 ? (ManagerType) extras[1] : ManagerType.OR;
                yield new Manager(username, password, email, firstName, lastName, school, salary, mType);
            }
            case ADMIN -> {
                double salary = extras.length > 0 ? (double) extras[0] : 70000.0;
                yield new Admin(username, password, email, firstName, lastName, school, salary);
            }
        };
    }
}
