package repository;

import Validator.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface IRepository<ID, T> {
    public void add(ID id, T addedElement) throws ValidationException;
    public Optional<T> delete(ID id) throws ValidationException;
    public void modify(ID id, T newUpdatedElement) throws ValidationException;
    public Optional<T> findById(ID id) throws ValidationException;
    public ArrayList<T> getAll();
}
