package filters;

import domain.Appointment;
import domain.Patient;

import java.util.Objects;

public class FilterAppointmentsByDate implements AbstractFilter<Appointment> {
    public String date;
    public FilterAppointmentsByDate(String date) {

        this.date = date;
    }

    public boolean accept(Appointment appointment) {

        return Objects.equals(date, appointment.getDate());
    }
}
