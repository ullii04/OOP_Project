package university.model;

import university.exceptions.LowResearcherException;
import university.exceptions.NotResearcherException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Researcher is a standalone class (composition relationship with Teacher/Student/Employee).
 * As shown in UML: composition circle from Teacher -> Researcher.
 * Can be attached to any person (Teacher, Student, or plain Employee).
 *
 * This also serves as the Strategy pattern host — sorting strategy is passed via Comparator.
 */
public class Researcher implements Serializable {
    private static final long serialVersionUID = 1L;

    private int hIndex;
    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;

    // Back-reference to the owning person's name (for display)
    private String ownerName;

    public Researcher(String ownerName) {
        this.ownerName = ownerName;
        this.hIndex = 0;
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public Researcher(String ownerName, int hIndex) {
        this(ownerName);
        this.hIndex = hIndex;
    }

    /**
     * Prints papers sorted by the given Comparator — Strategy Pattern #5.
     * The comparator IS the strategy; caller decides sort order.
     */
    public void printPapers(Comparator<ResearchPaper> c) {
        System.out.println("=== Research Papers of " + ownerName + " ===");
        if (papers.isEmpty()) {
            System.out.println("  No papers yet.");
            return;
        }
        papers.stream().sorted(c).forEach(p -> System.out.println("  " + p));
    }

    /**
     * Add a participant to a ResearchProject.
     * If the person is not a Researcher (i.e., has no Researcher role), throws NotResearcherException.
     */
    public void addParticipants(ResearchProject project) throws LowResearcherException {
        if (hIndex < 3) {
            throw new LowResearcherException(
                    ownerName + " h-index=" + hIndex + " is below the minimum of 3 to join a project.");
        }
        project.getParticipants().add(this);
        if (!projects.contains(project)) {
            projects.add(project);
        }
        System.out.println(ownerName + " joined project: " + project.getTopic());
    }

    public void addPaper(ResearchPaper paper) {
        papers.add(paper);
        university.patterns.Logger.getInstance().log(ownerName + " added paper: " + paper.getTitle());
    }

    public int getIndex() {
        return hIndex;
    }

    public void setHIndex(int hIndex) {
        this.hIndex = hIndex;
    }

    // Getters
    public int getHIndex() { return hIndex; }
    public List<ResearchPaper> getPapers() { return papers; }
    public List<ResearchProject> getProjects() { return projects; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String name) { this.ownerName = name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Researcher)) return false;
        Researcher that = (Researcher) o;
        return Objects.equals(ownerName, that.ownerName);
    }

    @Override
    public int hashCode() { return Objects.hash(ownerName); }

    @Override
    public String toString() {
        return "Researcher{owner='" + ownerName + "', hIndex=" + hIndex
                + ", papers=" + papers.size() + ", projects=" + projects.size() + "}";
    }
}
