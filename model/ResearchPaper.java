package university.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ResearchPaper implements Serializable, Comparable<ResearchPaper> {
    private static final long serialVersionUID = 1L;

    private String doi;
    private String title;
    private List<String> authors;
    private String journal;
    private int citations;
    private int pages;
    private LocalDate date;
    private String statistics;

    // Comparators
    public static final Comparator<ResearchPaper> BY_DATE = Comparator.comparing(ResearchPaper::getDate);
    public static final Comparator<ResearchPaper> BY_CITATIONS = Comparator.comparingInt(ResearchPaper::getCitations).reversed();
    public static final Comparator<ResearchPaper> BY_LENGTH = Comparator.comparingInt(ResearchPaper::getPages).reversed();

    public ResearchPaper(String doi, String title, List<String> authors,
                         String journal, int citations, int pages, LocalDate date, String statistics) {
        this.doi = doi;
        this.title = title;
        this.authors = authors;
        this.journal = journal;
        this.citations = citations;
        this.pages = pages;
        this.date = date;
        this.statistics = statistics;
    }

    @Override
    public int compareTo(ResearchPaper other) {
        return BY_CITATIONS.compare(this, other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchPaper)) return false;
        ResearchPaper that = (ResearchPaper) o;
        return Objects.equals(doi, that.doi);
    }

    @Override
    public int hashCode() { return Objects.hash(doi); }

    @Override
    public String toString() {
        return String.format("ResearchPaper{title='%s', journal='%s', citations=%d, pages=%d, date=%s, doi='%s'}",
                title, journal, citations, pages, date, doi);
    }

    // Getters
    public String getDoi() { return doi; }
    public String getTitle() { return title; }
    public List<String> getAuthors() { return authors; }
    public String getJournal() { return journal; }
    public int getCitations() { return citations; }
    public void setCitations(int citations) { this.citations = citations; }
    public int getPages() { return pages; }
    public LocalDate getDate() { return date; }
    public String getStatistics() { return statistics; }
}
