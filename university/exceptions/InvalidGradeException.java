package university.exceptions;

public class InvalidGradeException extends Exception {
    public InvalidGradeException(String message) {
        super(message);
    }

    public InvalidGradeException(String gradePart, double score, double min, double max) {
        super(gradePart + " must be between " + min + " and " + max + ". Given: " + score);
    }
}