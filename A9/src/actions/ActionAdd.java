package actions;

import repository.IRepository;

public class ActionAdd<ID, T> implements IAction<ID, T> {
    private final IRepository<ID, T> repository;
    private final T addedElement;
    private final ID addedElementId;

    public ActionAdd(IRepository<ID, T> repo, ID addedElementId, T addedElement) {
        this.repository = repo;
        this.addedElementId = addedElementId;
        this.addedElement = addedElement;
    }

    @Override
    public void executeUndo() {
        repository.delete(addedElementId);
    }

    @Override
    public void executeRedo() {
        repository.add(addedElementId, addedElement);
    }
}