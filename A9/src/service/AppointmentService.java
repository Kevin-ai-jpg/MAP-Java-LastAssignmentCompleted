package service;

import Validator.AppointmentValidator;
import actions.ActionAdd;
import actions.ActionRemove;
import actions.ActionUpdate;
import actions.IAction;
import domain.Appointment;
import filters.AbstractFilter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;
import repository.FilterRepository;
import repository.IRepository;

public class AppointmentService {
    private IRepository<Integer, Appointment> appointmentRepository;
    private AppointmentValidator appointmentValidator;

    public AppointmentService(IRepository<Integer, Appointment> appointmentRepository,  AppointmentValidator appointmentValidator) {
        this.appointmentValidator = appointmentValidator;
        this.appointmentRepository = appointmentRepository;
    }

    private Stack<IAction<Integer, Appointment>> undoStack = new Stack<>();
    private Stack<IAction<Integer, Appointment>> redoStack = new Stack<>();

    public void addAppointment(Appointment appointment) {
        appointmentValidator.validate(appointment);
        ActionAdd<Integer, Appointment> actionAdd = new ActionAdd<>(appointmentRepository, appointment.getID(), appointment);
        actionAdd.executeRedo();
        undoStack.push(actionAdd);
        redoStack.clear();
    }

    public Optional<Appointment> removeAppointment(Appointment appointment) {
        appointmentValidator.validate(appointment);
        ActionRemove<Integer, Appointment> actionRemove = new ActionRemove<>(appointmentRepository, appointment.getID(), appointment);
        actionRemove.executeRedo();
        undoStack.push(actionRemove);
        redoStack.clear();
        return Optional.of(appointment);
    }

    public void updateAppointment(Appointment appointment) {
        appointmentValidator.validate(appointment);
        Optional<Appointment> oldAppointment = appointmentRepository.findById(appointment.getID());
        ActionUpdate<Integer, Appointment> actionUpdate = new ActionUpdate<>(appointmentRepository, appointment.getID(), oldAppointment.get(), appointment);
        actionUpdate.executeRedo();
        undoStack.push(actionUpdate);
        redoStack.clear();
    }

//    public void undo() {
//        if (undoStack.isEmpty()) {
//            throw new RuntimeException("No thing to undo!");
//        }
//        IAction<Integer, Appointment> action = undoStack.pop();
//        action.executeUndo();
//        redoStack.push(action);
//    }
//
//    public void redo() {
//        if (redoStack.isEmpty()) {
//            throw new RuntimeException("No thing to redo");
//        }
//        IAction<Integer, Appointment> action = redoStack.pop();
//        action.executeRedo();
//        undoStack.push(action);
//    }

    public Optional<Appointment> findById(int id) {
        return appointmentRepository.findById(id);
    }

    public ArrayList<Appointment> getAll() {
        return appointmentRepository.getAll();
    }

    public ArrayList<Appointment> getAllFiltered(AbstractFilter<Appointment> filter) {
        FilterRepository<Integer, Appointment> filteredRepo = new FilterRepository<>(filter);
        appointmentRepository.getAll().forEach(appointment -> {
            appointmentValidator.validate(appointment);
            filteredRepo.add(appointment.getID(), appointment);
        });
        return filteredRepo.getAll();
    }

    public void generateAppointmentDateReport() {
        Map<String, Long> reportData = appointmentRepository.getAll().stream()
                .collect(Collectors.groupingBy(
                        Appointment::getDate,
                        Collectors.counting()
                ));

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("D:/UBB-Year2/MAP-Java/a7-Kevin-ai-jpg/A7/data/appointment_date_report.txt"))) {
            bufferedWriter.write("Appointment Date Report\n");
            reportData.forEach((date, count) -> {
                try {
                    bufferedWriter.write("Date: " + date + " Count: " + count + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("Report generated: appointment_date_report.txt");
        } catch (IOException e) {
            throw new RuntimeException("Error writing report: " + e.getMessage());
        }
    }

    public void generateAppointmentTimeReport() {
        Map<String, Long> reportData = appointmentRepository.getAll().stream()
                .collect(Collectors.groupingBy(
                        Appointment::getTime,
                        Collectors.counting()
                ));

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("D:/UBB-Year2/MAP-Java/a7-Kevin-ai-jpg/A7/data/appointment_time_report.txt"))) {
            bufferedWriter.write("Appointment Time Report\n");
            reportData.forEach((time, count) -> {
                try {
                    bufferedWriter.write("Time: " + time + " Count: " + count + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("Report generated: appointment_time_report.txt");
        } catch (IOException e) {
            throw new RuntimeException("Error writing report: " + e.getMessage());
        }
    }
}
