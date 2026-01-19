package actions;

public interface IAction<ID, T> {
    void executeUndo();
    void executeRedo();
}