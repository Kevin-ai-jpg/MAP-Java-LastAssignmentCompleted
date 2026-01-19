package filters;

import domain.Patient;

import java.util.Objects;

public class FilterPatientsByName implements AbstractFilter<Patient> {
    public String name;

    public FilterPatientsByName(String name) {

        this.name = name;
    }

    @Override
    public boolean accept(Patient entity) {

        return Objects.equals(name, entity.getName());
    }
}
