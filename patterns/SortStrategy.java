package university.patterns;

import university.model.ResearchPaper;

import java.util.Comparator;

/**
 * Design Pattern #5: Strategy
 * Encapsulates sorting algorithms for ResearchPaper as interchangeable strategies.
 * Used by Researcher.printPapers(Comparator c) — the comparator IS the strategy.
 * Caller selects strategy at runtime without changing Researcher class.
 */
public class SortStrategy {

    /** Strategy 1: Sort by number of citations descending (most cited first) */
    public static final Comparator<ResearchPaper> BY_CITATIONS =
            Comparator.comparingInt(ResearchPaper::getCitations).reversed();

    /** Strategy 2: Sort by publication date ascending (oldest first) */
    public static final Comparator<ResearchPaper> BY_DATE =
            Comparator.comparing(ResearchPaper::getDate);

    /** Strategy 3: Sort by article length (pages) descending (longest first) */
    public static final Comparator<ResearchPaper> BY_LENGTH =
            Comparator.comparingInt(ResearchPaper::getPages).reversed();

    /** Strategy 4: Sort alphabetically by title */
    public static final Comparator<ResearchPaper> BY_TITLE =
            Comparator.comparing(ResearchPaper::getTitle);

    /** Returns the named strategy, defaulting to BY_CITATIONS */
    public static Comparator<ResearchPaper> getStrategy(String name) {
        return switch (name.toLowerCase()) {
            case "date" -> BY_DATE;
            case "length", "pages" -> BY_LENGTH;
            case "title" -> BY_TITLE;
            default -> BY_CITATIONS;
        };
    }
}
