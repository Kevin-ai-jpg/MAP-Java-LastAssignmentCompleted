package filters;

import domain.Patient;

import java.util.Objects;

public class FilterPatientsByAge implements AbstractFilter<Patient> {
    public int age;
    public FilterPatientsByAge(int age) {

        this.age = age;
    }

    public boolean accept(Patient patient) {

        return age == patient.getAge();
    }
}
