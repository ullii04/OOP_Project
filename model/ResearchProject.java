package university.model;

import university.exceptions.NotResearcherException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResearchProject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String topic;
    private List<ResearchPaper> papers;
    private List<Researcher> participants;

    public ResearchProject(String topic) {
        this.topic = topic;
        this.papers = new ArrayList<>();
        this.participants = new ArrayList<>();
    }

    /**
     * addParticipants (plural, as in UML diagram).
     * Throws NotResearcherException if the passed object is not a Researcher instance.
     */
    public void addParticipants(Object person) throws NotResearcherException {
        if (!(person instanceof Researcher)) {
            throw new NotResearcherException(
                    person.getClass().getSimpleName() + " is not a Researcher and cannot join the project.");
        }
        Researcher r = (Researcher) person;
        if (!participants.contains(r)) {
            participants.add(r);
        }
    }

    public void removePaper(ResearchPaper paper) { papers.remove(paper); }
    public void addPaper(ResearchPaper paper) { papers.add(paper); }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public List<ResearchPaper> getPapers() { return papers; }
    public List<Researcher> getParticipants() { return participants; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchProject)) return false;
        ResearchProject that = (ResearchProject) o;
        return Objects.equals(topic, that.topic);
    }

    @Override
    public int hashCode() { return Objects.hash(topic); }

    @Override
    public String toString() {
        return "ResearchProject{topic='" + topic + "', papers=" + papers.size()
                + ", participants=" + participants.size() + "}";
    }
}
