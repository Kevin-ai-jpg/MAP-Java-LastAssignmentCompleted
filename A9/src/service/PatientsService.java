package service;

import Validator.PatientValidator;
import actions.*;
import domain.Appointment;
import domain.Patient;
import filters.AbstractFilter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import repository.FilterRepository;
import repository.IRepository;

public class PatientsService {
    private IRepository<Integer, Patient> patientsRepository;
    private PatientValidator patientValidator;
    private IRepository<Integer, Appointment> appointmentRepository; 

    public PatientsService(IRepository<Integer, Patient> patientsRepository, PatientValidator patientValidator, IRepository<Integer, Appointment> appointmentRepository) {
        this.patientsRepository = patientsRepository;
        this.patientValidator = patientValidator;
        this.appointmentRepository = appointmentRepository; 
    }

    private Stack<IAction<Integer, Patient>> undoStack = new Stack<>();
    private Stack<IAction<Integer, Patient>> redoStack = new Stack<>();

    public void addPatient(Patient patient) {
        patientValidator.validate(patient);
        ActionAdd<Integer, Patient> actionAdd = new ActionAdd<>(patientsRepository, patient.getID(), patient);
        actionAdd.executeRedo();
        undoStack.push(actionAdd);
        redoStack.clear();
    }

    public Optional<Patient> removePatient(Patient patient) {
        patientValidator.validate(patient);
        List<Appointment> patientAppointments = appointmentRepository.getAll().stream()
                .filter(appointment -> appointment.getPatientID() == patient.getID())
                .collect(Collectors.toList());
        ActionRemovePatientCascade action = new ActionRemovePatientCascade(patientsRepository, appointmentRepository, patient, patientAppointments);
        action.executeRedo();
        undoStack.push(action);
        redoStack.clear();

        return Optional.of(patient);
    }

    public void updatePatient(Patient patient) {
        patientValidator.validate(patient);
        Optional<Patient> oldPatient = patientsRepository.findById(patient.getID());

        if (oldPatient.isPresent()) {
            ActionUpdate<Integer, Patient> actionUpdate = new ActionUpdate<>(patientsRepository, patient.getID(), oldPatient.get(), patient);
            actionUpdate.executeRedo();
            undoStack.push(actionUpdate);
            redoStack.clear();
        }
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            throw new RuntimeException("Nothing to undo!");
        }
        IAction<Integer, Patient> action = undoStack.pop();
        action.executeUndo();
        redoStack.push(action);
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            throw new RuntimeException("Nothing to redo!");
        }
        IAction<Integer, Patient> action = redoStack.pop();
        action.executeRedo();
        undoStack.push(action);
    }

    public Optional<Patient> findById(int id) {
        return patientsRepository.findById(id);
    }

    public ArrayList<Patient> getAll() {
        return patientsRepository.getAll();
    }

    public ArrayList<Patient> getAllFiltered(AbstractFilter<Patient> filter) {
        FilterRepository<Integer, Patient> patientFilterRepository = new FilterRepository<>(filter);
        patientsRepository.getAll().forEach(patient -> {
            patientFilterRepository.add(patient.getID(), patient);
        });
        return patientFilterRepository.getAll();
    }

    public void generatePatientAgeReport() {
        Map<Integer, Long> reportData = patientsRepository.getAll().stream()
                .collect(Collectors.groupingBy(
                        Patient::getAge,
                        Collectors.counting()
                ));

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("D:/UBB-Year2/MAP-Java/a7-Kevin-ai-jpg/A7/data/patient_age_report.txt"))) {
            bufferedWriter.write("Patient Age Report\n");
            reportData.forEach((age, count) -> {
                try {
                    bufferedWriter.write("Age: " + age + " Count: " + count + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("Report generated: patient_age_report.txt");
        } catch (IOException e) {
            throw new RuntimeException("Error writing report: " + e.getMessage());
        }
    }

    public void generatePatientNameReport() {
        Map<String, Long> reportData = patientsRepository.getAll().stream()
                .collect(Collectors.groupingBy(
                        Patient::getName,
                        Collectors.counting()
                ));

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("D:/UBB-Year2/MAP-Java/a7-Kevin-ai-jpg/A7/data/patient_name_report.txt"))) {
            bufferedWriter.write("Patient Name Report\n");
            reportData.forEach((name, count) -> {
                try {
                    bufferedWriter.write("Name: " + name + " Count: " + count + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("Report generated: patient_name_report.txt");
        } catch (IOException e) {
            throw new RuntimeException("Error writing report: " + e.getMessage());
        }
    }

    public void generatePatientEmailReport() {
        Map<String, Long> reportData = patientsRepository.getAll().stream()
                .collect(Collectors.groupingBy(
                        Patient::getEmail,
                        Collectors.counting()
                ));

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("D:/UBB-Year2/MAP-Java/a7-Kevin-ai-jpg/A7/data/patient_email_report.txt"))) {
            bufferedWriter.write("Patient Email Report\n");
            reportData.forEach((email, count) -> {
                try {
                    bufferedWriter.write("Email: " + email + " Count: " + count + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("Report generated: patient_email_report.txt");
        } catch (IOException e) {
            throw new RuntimeException("Error writing report: " + e.getMessage());
        }
    }
}
