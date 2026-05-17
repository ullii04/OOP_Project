package university.exceptions;

public class WeakPasswordException extends ValidationException {
    public WeakPasswordException(String message) {
        super(message);
    }
}