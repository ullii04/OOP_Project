package university.utils;
import university.model.Researcher;
import university.classes.ResearchPaper;

import java.util.Comparator;
import java.util.List;


public class ResearchUtils {
    public static void printAllPapers(List<Researcher> researchers, Comparator<ResearchPaper> comparator) {
        System.out.println("====== ALL RESEARCH PAPERS IN UNIVERSITY ======");
        researchers.stream()
                .flatMap(r -> r.getPapers().stream())
                .sorted(comparator)
                .forEach(System.out::println);
    }

    public static void printTopCitedResearcherOfSchool(List<Researcher> researchers) {
        System.out.println("=== Top Cited Researcher of University ===");
        researchers.stream()
                .max(Comparator.comparingInt(r ->
                        r.getPapers().stream().mapToInt(ResearchPaper::getCitations).sum()))
                .ifPresentOrElse(
                        r -> System.out.println("Top: " + r.getOwnerName()
                                + " | Total citations: "
                                + r.getPapers().stream().mapToInt(ResearchPaper::getCitations).sum()),
                        () -> System.out.println("No researchers found."));
    }

    public static void printTopCitedResearcherOfYear(List<Researcher> researchers, int year) {
        System.out.println("=== Top Cited Researcher of Year " + year + " ===");
        researchers.stream()
                .max(Comparator.comparingInt(r ->
                        r.getPapers().stream()
                                .filter(p -> p.getDate().getYear() == year)
                                .mapToInt(ResearchPaper::getCitations).sum()))
                .ifPresentOrElse(
                        r -> {
                            int total = r.getPapers().stream()
                                    .filter(p -> p.getDate().getYear() == year)
                                    .mapToInt(ResearchPaper::getCitations).sum();
                            System.out.println("Top: " + r.getOwnerName() + " | Citations in " + year + ": " + total);
                        },
                        () -> System.out.println("No researchers found for year " + year));
    }
}
