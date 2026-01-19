package filters;

public interface AbstractFilter<T> {
    boolean accept(T entity);
}
