package repository;

import Validator.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class MemoryRepository<ID, T> implements IRepository<ID, T> {
    protected HashMap<ID, T> elements = new HashMap<>();

    @Override
    public void add(ID id, T element) throws ValidationException {
        if (id == null || elements == null) {
            throw new ValidationException("ID and entity cannot be null");
        }
        if (elements.containsKey(id)) {
            throw new ValidationException("ID already exists for other entity");
        }
        elements.put(id, element);
    }

    @Override
    public Optional<T> delete(ID id) throws ValidationException {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        if (!elements.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.ofNullable(elements.remove(id));
    }

    @Override
    public ArrayList<T> getAll() {
        return new ArrayList<>(elements.values());
    }

    @Override
    public Optional<T> findById(ID id) throws ValidationException {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return Optional.ofNullable(elements.get(id));
    }

    @Override
    public void modify(ID id, T new_element) throws ValidationException {
        if (id == null || new_element == null) {
            throw new ValidationException("ID cannot be null or the entity is null");
        }
        if (!elements.containsKey(id)) {
            throw new ValidationException("ID does not exist");
        }
        elements.remove(id);
        elements.put(id, new_element);
    }
}


