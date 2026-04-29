package university.patterns;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Design Pattern #1: Singleton
 * Ensures one Logger instance throughout the application.
 */
public class Logger implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Logger instance;
    private final List<String> logs;

    private Logger() {
        logs = new ArrayList<>();
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        String entry = "[" + LocalDateTime.now() + "] " + message;
        logs.add(entry);
        System.out.println("LOG: " + entry);
    }

    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }

    public void clearLogs() {
        logs.clear();
    }
}
