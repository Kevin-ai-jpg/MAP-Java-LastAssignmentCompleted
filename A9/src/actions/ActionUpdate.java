package actions;

import repository.IRepository;

public class ActionUpdate<ID, T> implements IAction<ID, T> {
    private final IRepository<ID, T> repo;
    private final T oldElement;
    private final T newElement;
    private final ID elementId;

    public ActionUpdate(IRepository<ID, T> repo, ID elementId, T oldElement, T newElement) {
        this.repo = repo;
        this.elementId = elementId;
        this.oldElement = oldElement;
        this.newElement = newElement;
    }

    @Override
    public void executeUndo() {
        repo.modify(elementId, oldElement);
    }

    @Override
    public void executeRedo() {
        repo.modify(elementId, newElement);
    }
}