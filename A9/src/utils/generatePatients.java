package utils;

import domain.Patient;

import java.util.ArrayList;
import java.util.List;

public class generatePatients {
    public static List<Patient> generateSamplePatients(int count) {
        List<Patient> patients = new ArrayList<>();
        for (int index = 1; index <= count; index++) {
            Integer age = index%100;
            Integer id = index;
            Patient patient = new Patient(index, "P"+index, "P"+index+"@gmail.com", "07123"+(index), age);
            patients.add(patient);
        }
        return patients;
    }
}


