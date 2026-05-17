package university.patterns;

import university.classes.*;

import university.classes.ResearchPaper;

import java.util.Comparator;


public class SortStrategy {

    
    public static final Comparator<ResearchPaper> BY_CITATIONS =
            Comparator.comparingInt(ResearchPaper::getCitations).reversed();

    
    public static final Comparator<ResearchPaper> BY_DATE =
            Comparator.comparing(ResearchPaper::getDate);

    
    public static final Comparator<ResearchPaper> BY_LENGTH =
            Comparator.comparingInt(ResearchPaper::getPages).reversed();

    
    public static final Comparator<ResearchPaper> BY_TITLE =
            Comparator.comparing(ResearchPaper::getTitle);

   
    public static Comparator<ResearchPaper> getStrategy(String name) {
        return switch (name.toLowerCase()) {
            case "date" -> BY_DATE;
            case "length", "pages" -> BY_LENGTH;
            case "title" -> BY_TITLE;
            default -> BY_CITATIONS;
        };
    }
}
