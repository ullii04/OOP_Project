package university.classes;

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

    public static final Comparator<ResearchPaper> BY_DATE =
            Comparator.comparing(ResearchPaper::getDate);

    public static final Comparator<ResearchPaper> BY_CITATIONS =
            Comparator.comparingInt(ResearchPaper::getCitations).reversed();

    public static final Comparator<ResearchPaper> BY_LENGTH =
            Comparator.comparingInt(ResearchPaper::getPages).reversed();

    public ResearchPaper(String doi,
                         String title,
                         List<String> authors,
                         String journal,
                         int citations,
                         int pages,
                         LocalDate date,
                         String statistics) {

        validatePaper(doi, title, authors, journal, citations, pages, date, statistics);

        this.doi = doi;
        this.title = title;
        this.authors = authors;
        this.journal = journal;
        this.citations = citations;
        this.pages = pages;
        this.date = date;
        this.statistics = statistics;
    }

    private void validatePaper(String doi,
                               String title,
                               List<String> authors,
                               String journal,
                               int citations,
                               int pages,
                               LocalDate date,
                               String statistics) {

        if (doi == null || doi.isBlank()) {
            throw new IllegalArgumentException("DOI cannot be empty.");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }

        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("Authors cannot be empty.");
        }

        if (journal == null || journal.isBlank()) {
            throw new IllegalArgumentException("Journal cannot be empty.");
        }

        if (citations < 0) {
            throw new IllegalArgumentException("Citations cannot be negative.");
        }

        if (pages <= 0) {
            throw new IllegalArgumentException("Pages must be greater than 0.");
        }

        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }

        if (statistics == null || statistics.isBlank()) {
            throw new IllegalArgumentException("Statistics cannot be empty.");
        }
    }

    public void increaseCitations(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Citation amount must be positive.");
        }

        citations += amount;
    }

    public void addAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author name cannot be empty.");
        }

        authors.add(author);
    }

    public boolean isHighlyCited() {
        return citations >= 100;
    }

    public int getAuthorCount() {
        return authors.size();
    }

    @Override
    public int compareTo(ResearchPaper other) {
        return BY_CITATIONS.compare(this, other);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ResearchPaper)) {
            return false;
        }

        ResearchPaper paper = (ResearchPaper) object;
        return Objects.equals(doi, paper.doi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doi);
    }

    @Override
    public String toString() {
        return String.format(
                "ResearchPaper{title='%s', journal='%s', citations=%d, pages=%d, date=%s, doi='%s'}",
                title, journal, citations, pages, date, doi
        );
    }

    public String getDoi() {
        return doi;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getJournal() {
        return journal;
    }

    public int getCitations() {
        return citations;
    }

    public void setCitations(int citations) {
        if (citations < 0) {
            throw new IllegalArgumentException("Citations cannot be negative.");
        }

        this.citations = citations;
    }

    public int getPages() {
        return pages;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStatistics() {
        return statistics;
    }
}