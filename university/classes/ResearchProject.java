package university.classes;

import university.exceptions.NotResearcherException;
import university.model.Researcher;

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
        validateTopic(topic);

        this.topic = topic;
        this.papers = new ArrayList<>();
        this.participants = new ArrayList<>();
    }

    private void validateTopic(String topic) {
        if (topic == null || topic.isBlank()) {
            throw new IllegalArgumentException("Project topic cannot be empty.");
        }
    }

    public void addParticipants(Object person) throws NotResearcherException {
        if (person == null) {
            throw new NotResearcherException("Participant cannot be null.");
        }

        if (!(person instanceof Researcher)) {
            throw new NotResearcherException(
                    person.getClass().getSimpleName()
                            + " is not a Researcher and cannot join the project."
            );
        }

        Researcher researcher = (Researcher) person;

        if (!participants.contains(researcher)) {
            participants.add(researcher);
        }
    }

    public void removeParticipant(Researcher researcher) {
        if (researcher == null) {
            throw new IllegalArgumentException("Researcher cannot be null.");
        }

        participants.remove(researcher);
    }

    public void addPaper(ResearchPaper paper) {
        if (paper == null) {
            throw new IllegalArgumentException("Paper cannot be null.");
        }

        if (!papers.contains(paper)) {
            papers.add(paper);
        }
    }

    public void removePaper(ResearchPaper paper) {
        if (paper == null) {
            throw new IllegalArgumentException("Paper cannot be null.");
        }

        papers.remove(paper);
    }

    public int getPaperCount() {
        return papers.size();
    }

    public int getParticipantCount() {
        return participants.size();
    }

    public int getTotalCitations() {
        return papers.stream()
                .mapToInt(ResearchPaper::getCitations)
                .sum();
    }

    public double getAverageCitations() {
        if (papers.isEmpty()) {
            return 0.0;
        }

        return papers.stream()
                .mapToInt(ResearchPaper::getCitations)
                .average()
                .orElse(0.0);
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        validateTopic(topic);
        this.topic = topic;
    }

    public List<ResearchPaper> getPapers() {
        return papers;
    }

    public List<Researcher> getParticipants() {
        return participants;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ResearchProject)) {
            return false;
        }

        ResearchProject project = (ResearchProject) object;
        return Objects.equals(topic, project.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic);
    }

    @Override
    public String toString() {
        return "ResearchProject{topic='" +
                topic +
                "', papers=" +
                papers.size() +
                ", participants=" +
                participants.size() +
                ", totalCitations=" +
                getTotalCitations() +
                "}";
    }
}