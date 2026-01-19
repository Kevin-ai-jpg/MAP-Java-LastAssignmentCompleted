package Validator;

public interface Validator<T> {
    public void validate(T element) throws ValidationException;
}
