package Validator;

import domain.Patient;

public class PatientValidator implements  Validator<Patient> {
    public void validate(Patient patient) throws ValidationException {
        String error = "";

        if(patient.getID() <= 0) {
            error += "Patient ID should be greater than 0.";
        }

        if(patient.getName() == null) {
            error += "Patient name should not be null.";
        }

        if(patient.getEmail() == null || !patient.getEmail().contains("@") || !patient.getEmail().contains(".")) {
            error += "Patient email should be a valid email address.";
        }

        if(patient.getTelephone().isEmpty()) {
            error += "Patient telephone should not be empty.";
        }

        if(patient.getAge() <= 0) {
            error += "Patient age should be greater than 0.";
        }

        if(!error.isEmpty()) {
            throw new ValidationException(error);
        }
    }
}
