package actions;

import repository.IRepository;

public class ActionRemove<ID, T> implements IAction<ID, T> {
    private final IRepository<ID, T> repository;
    private final T deletedElem;
    private final ID deletedElemId;

    public ActionRemove(IRepository<ID, T> repository, ID deletedElemId, T deletedElem) {
        this.repository = repository;
        this.deletedElemId = deletedElemId;
        this.deletedElem = deletedElem;
    }

    @Override
    public void executeUndo() {
        repository.add(deletedElemId, deletedElem);
    }

    @Override
    public void executeRedo() {
        repository.delete(deletedElemId);
    }
}