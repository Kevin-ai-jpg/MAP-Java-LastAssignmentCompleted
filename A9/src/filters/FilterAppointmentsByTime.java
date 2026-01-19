package filters;

import domain.Appointment;
import domain.Patient;

import java.util.Objects;

public class FilterAppointmentsByTime implements AbstractFilter<Appointment> {
    public String time;
    public FilterAppointmentsByTime(String time) {

        this.time = time;
    }

    public boolean accept(Appointment appointment) {

        return Objects.equals(time, appointment.getTime());
    }
}
