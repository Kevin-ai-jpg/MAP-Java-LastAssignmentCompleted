package ui;

import domain.Appointment;
import filters.FilterAppointmentsByDate;
import filters.FilterAppointmentsByTime;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import service.AppointmentService;
import service.PatientsService;

public class AppointmentManagementController {

    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, Integer> idColumn, patientIdColumn;
    @FXML private TableColumn<Appointment, String> dateColumn, timeColumn;

    @FXML private TextField idField, patientIdField, dateField, timeField;
    @FXML private Button addButton, updateButton, deleteButton;

    @FXML private TextField filterDateField, filterTimeField;
    @FXML private Button filterDateButton, filterTimeButton, resetFilterButton;

    @FXML private Button undoButton, redoButton;

    private AppointmentService service;
    private PatientsService patientsService;
    private ObservableList<Appointment> appointmentsData = FXCollections.observableArrayList();

    public void setService(AppointmentService service, PatientsService patientsService) {
        this.service = service;
        this.patientsService = patientsService;
        refreshTable();
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getID()).asObject());
        patientIdColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getPatientID()).asObject());
        dateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate().toString()));
        timeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTime().toString()));

        appointmentTable.setItems(appointmentsData);

        addButton.setOnAction(event -> addHandler());
        updateButton.setOnAction(event -> updateHandler());
        deleteButton.setOnAction(event -> deleteHandler());

        filterDateButton.setOnAction(event -> applyDateFilter());
        filterTimeButton.setOnAction(event -> applyTimeFilter());
        resetFilterButton.setOnAction(event -> refreshTable());

        undoButton.setOnAction(event -> undoHandler());
        redoButton.setOnAction(event -> redoHandler());

        appointmentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                idField.setText(String.valueOf(newSel.getID()));
                patientIdField.setText(String.valueOf(newSel.getPatientID()));
                dateField.setText(newSel.getDate());
                timeField.setText(newSel.getTime());
            }
        });
    }

    private void applyDateFilter() {
        try {
            String date = filterDateField.getText();
            FilterAppointmentsByDate filter = new FilterAppointmentsByDate(date);
            appointmentsData.clear();
            appointmentsData.addAll(service.getAllFiltered(filter));
        } catch (Exception e) {
            showAlert("Error", "Filter Error: " + e.getMessage());
        }
    }

    private void applyTimeFilter() {
        try {
            String time = filterTimeField.getText();
            FilterAppointmentsByTime filter = new FilterAppointmentsByTime(time);
            appointmentsData.clear();
            appointmentsData.addAll(service.getAllFiltered(filter));
        } catch (Exception e) {
            showAlert("Error", "Filter Error: " + e.getMessage());
        }
    }

    private void refreshTable() {
        if (service != null) {
            appointmentsData.setAll(service.getAll());
        }
        filterDateField.clear();
        filterTimeField.clear();
    }

    private void addHandler() {
        try {
            Appointment a = extractFromFields();
            service.addAppointment(a);
            refreshTable();
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void updateHandler() {
        try {
            Appointment a = extractFromFields();
            service.updateAppointment(a);
            refreshTable();
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void deleteHandler() {
        Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                service.removeAppointment(selected);
                refreshTable();
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        }
        refreshTable();
    }

    private Appointment extractFromFields() {
        int id = Integer.parseInt(idField.getText());
        int pid = Integer.parseInt(patientIdField.getText());
        String  date = dateField.getText();
        String time = timeField.getText();
        return new Appointment(id, pid, date, time);
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }

    private void undoHandler() {
        try {
            patientsService.undo();
            refreshTable();
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void redoHandler() {
        try {
            patientsService.redo();
            refreshTable();
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }
}