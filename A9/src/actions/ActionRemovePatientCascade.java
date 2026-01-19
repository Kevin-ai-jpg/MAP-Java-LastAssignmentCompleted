package actions;

import domain.Appointment;
import domain.Patient;
import repository.IRepository;
import java.util.List;

public class ActionRemovePatientCascade implements IAction<Integer, Patient> {
    private final IRepository<Integer, Patient> patientRepository;
    private final IRepository<Integer, Appointment> appointmentRepository;
    private final Patient patient;
    private final List<Appointment> PatientAppointments;

    public ActionRemovePatientCascade(IRepository<Integer, Patient> patientRepository, IRepository<Integer, Appointment> appointmentRepository, Patient patient, List<Appointment> PatientAppointments) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.patient = patient;
        this.PatientAppointments = PatientAppointments;
    }

    @Override
    public void executeRedo() {
        patientRepository.delete(patient.getID());
        for (Appointment appointment : PatientAppointments) {
            appointmentRepository.delete(appointment.getID());
        }
    }

    @Override
    public void executeUndo() {
        patientRepository.add(patient.getID(), patient);
        for (Appointment appointment : PatientAppointments) {
            appointmentRepository.add(appointment.getID(), appointment);
        }
    }
}