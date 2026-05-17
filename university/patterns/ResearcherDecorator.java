package university.patterns;
import university.model.Researcher;


public class ResearcherDecorator extends Researcher {
    private final Researcher wrapped;

    public ResearcherDecorator(Researcher wrapped) {
        super(wrapped.getOwnerName(), wrapped.getHIndex());
        this.wrapped = wrapped;
    }

    @Override
    public void addPaper(university.classes.ResearchPaper paper) {
        System.out.println("[Decorator] Adding paper with extra validation: " + paper.getTitle());
        wrapped.addPaper(paper);
        if (!getPapers().contains(paper)) {
            super.addPaper(paper);
        }
    }

    @Override
    public String toString() {
        return "ResearcherDecorator{wrapping=" + wrapped.getOwnerName() + "}";
    }
}
