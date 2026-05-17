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
        this.topic = topic;
        this.papers = new ArrayList<>();
        this.participants = new ArrayList<>();
    }

    public void addParticipants(Object person) throws NotResearcherException {

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

    public void addPaper(ResearchPaper paper) {
        papers.add(paper);
    }

    public void removePaper(ResearchPaper paper) {
        papers.remove(paper);
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
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
                "}";
    }
}