package university.exceptions;
public class MaxCreditsExceededException extends Exception {
    public MaxCreditsExceededException(String message) {
        super(message); }

    public MaxCreditsExceededException(int currentCredits, int addedCredits, int maxCredits) {
        super("Credit limit exceeded: current=" + currentCredits
                + ", added=" + addedCredits
                + ", max=" + maxCredits);
    }
}
