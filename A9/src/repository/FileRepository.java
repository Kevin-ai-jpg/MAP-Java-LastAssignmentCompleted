package repository;

import domain.Identifiable;
import java.util.Optional;

public abstract class FileRepository<ID, T extends Identifiable<ID>> extends MemoryRepository<ID, T> {
    protected String filename;
    protected abstract void readFromFile();
    protected abstract void writeToFile();

    public FileRepository(String FileName) {
        this.filename = FileName;
        readFromFile();
    }

    @Override
    public void add(ID id, T element) {
        super.add(id, element);
        writeToFile();
    }

    @Override
    public Optional<T> delete(ID id) {
        Optional<T> deletedElement = super.delete(id);
        writeToFile();
        return deletedElement;
    }

    @Override
    public void modify(ID id, T new_element) {
        super.modify(id, new_element);
        writeToFile();
    }


}
