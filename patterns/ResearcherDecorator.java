package university.patterns;

import university.model.Researcher;

/**
 * Design Pattern #4: Decorator
 * ResearcherDecorator wraps a base Researcher object and adds extra behavior.
 * Example: logging every paper addition, enforcing extra constraints.
 *
 * Note: Since Researcher is now a concrete class (per UML),
 * the Decorator extends Researcher and wraps another Researcher instance.
 */
public class ResearcherDecorator extends Researcher {
    private final Researcher wrapped;

    public ResearcherDecorator(Researcher wrapped) {
        super(wrapped.getOwnerName(), wrapped.getHIndex());
        this.wrapped = wrapped;
    }

    @Override
    public void addPaper(university.model.ResearchPaper paper) {
        System.out.println("[Decorator] Adding paper with extra validation: " + paper.getTitle());
        wrapped.addPaper(paper);
        // sync to this decorator's list as well
        if (!getPapers().contains(paper)) {
            super.addPaper(paper);
        }
    }

    @Override
    public String toString() {
        return "ResearcherDecorator{wrapping=" + wrapped.getOwnerName() + "}";
    }
}
