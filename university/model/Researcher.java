package university.model;

import university.classes.*;
import university.exceptions.LowResearcherException;
import university.patterns.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Researcher implements Serializable {

    private static final long serialVersionUID = 1L;

    private int hIndex;
    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;
    private String ownerName;

    public Researcher(String ownerName) {
        validateOwnerName(ownerName);

        this.ownerName = ownerName;
        this.hIndex = 0;
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public Researcher(String ownerName, int hIndex) {
        validateOwnerName(ownerName);
        validateHIndex(hIndex);

        this.ownerName = ownerName;
        this.hIndex = hIndex;
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    private void validateOwnerName(String ownerName) {
        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("Researcher owner name cannot be empty.");
        }
    }

    private void validateHIndex(int hIndex) {
        if (hIndex < 0) {
            throw new IllegalArgumentException("H-index cannot be negative.");
        }
    }

    public void printPapers(Comparator<ResearchPaper> comparator) {
        System.out.println("=== Research Papers of " + ownerName + " ===");

        if (papers.isEmpty()) {
            System.out.println("  No papers yet.");
            return;
        }

        Comparator<ResearchPaper> sortingComparator = comparator;

        if (sortingComparator == null) {
            sortingComparator = ResearchPaper.BY_CITATIONS;
        }

        papers.stream()
                .sorted(sortingComparator)
                .forEach(paper -> System.out.println("  " + paper));
    }

    public void addParticipants(ResearchProject project) throws LowResearcherException {
        if (project == null) {
            throw new IllegalArgumentException("Research project cannot be null.");
        }

        if (hIndex < 3) {
            throw new LowResearcherException(
                    ownerName + " h-index=" + hIndex +
                            " is below the minimum of 3 to join a project."
            );
        }

        if (!project.getParticipants().contains(this)) {
            project.getParticipants().add(this);
        }

        if (!projects.contains(project)) {
            projects.add(project);
        }

        System.out.println(ownerName + " joined project: " + project.getTopic());
    }

    public void addPaper(ResearchPaper paper) {
        if (paper == null) {
            throw new IllegalArgumentException("Research paper cannot be null.");
        }

        if (!papers.contains(paper)) {
            papers.add(paper);

            Logger.getInstance()
                    .log(ownerName + " added paper: " + paper.getTitle());
        }
    }

    public int calculateTotalCitations() {
        return papers.stream()
                .mapToInt(ResearchPaper::getCitations)
                .sum();
    }

    public double calculateAverageCitations() {
        if (papers.isEmpty()) {
            return 0.0;
        }

        return papers.stream()
                .mapToInt(ResearchPaper::getCitations)
                .average()
                .orElse(0.0);
    }

    public int getPaperCount() {
        return papers.size();
    }

    public int getProjectCount() {
        return projects.size();
    }

    public int getIndex() {
        return hIndex;
    }

    public void setHIndex(int hIndex) {
        validateHIndex(hIndex);
        this.hIndex = hIndex;
    }

    public int getHIndex() {
        return hIndex;
    }

    public List<ResearchPaper> getPapers() {
        return papers;
    }

    public List<ResearchProject> getProjects() {
        return projects;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String name) {
        validateOwnerName(name);
        this.ownerName = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Researcher)) {
            return false;
        }

        Researcher researcher = (Researcher) object;
        return Objects.equals(ownerName, researcher.ownerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerName);
    }

    @Override
    public String toString() {
        return "Researcher{owner='" +
                ownerName +
                "', hIndex=" +
                hIndex +
                ", papers=" +
                papers.size() +
                ", projects=" +
                projects.size() +
                ", totalCitations=" +
                calculateTotalCitations() +
                ", averageCitations=" +
                String.format("%.2f", calculateAverageCitations()) +
                "}";
    }
}