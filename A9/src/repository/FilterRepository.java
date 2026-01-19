package repository;

import domain.Patient;
import filters.AbstractFilter;

import java.util.ArrayList;

public class FilterRepository<ID, T> extends MemoryRepository<ID, T> {
    public AbstractFilter<T> filter;

    public FilterRepository(AbstractFilter<T> filter) {

        this.filter = filter;
    }

    @Override
    public ArrayList<T> getAll() {
        ArrayList<T> resultListAfterFilter = new ArrayList<>();
        for(T element : super.getAll()) {
            if(filter.accept(element)) {
                resultListAfterFilter.add(element);
            }
        }
        return resultListAfterFilter;
    }



}
