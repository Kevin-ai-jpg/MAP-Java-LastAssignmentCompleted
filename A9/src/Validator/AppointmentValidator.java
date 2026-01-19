package Validator;

import domain.Appointment;
import domain.Patient;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AppointmentValidator implements  Validator<Appointment> {
    public void validate(Appointment appointment) throws ValidationException {
        String error = "";

        if(appointment.getID() <= 0) {
            error += "Appointmnet ID should be greater than 0.";
        }

        if(appointment.getPatientID() <= 0) {
            error += "Patient ID should be greater than 0.";
        }

        if (appointment.getDate() == null) {
            error += "Appointment date cannot be null.";
        }

        if (appointment.getTime() == null) {
            error += "Appointment time should not be null";
        }

        if(!error.isEmpty()) {
            throw new ValidationException(error);
        }
    }
}
