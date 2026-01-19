package ui;

import domain.Patient;
import filters.FilterPatientsByAge;
import filters.FilterPatientsByName;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import service.PatientsService;

public class PatientManagementController {

    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Integer> idColumn;
    @FXML private TableColumn<Patient, String> nameColumn;
    @FXML private TableColumn<Patient, String> emailColumn;
    @FXML private TableColumn<Patient, String> phoneColumn;
    @FXML private TableColumn<Patient, Integer> ageColumn;

    @FXML private TextField idField, nameField, emailField, phoneField, ageField;
    @FXML private Button addButton, updateButton, deleteButton;

    @FXML private TextField filterNameField;
    @FXML private Button filterNameButton;
    @FXML private TextField filterAgeField;
    @FXML private Button filterAgeButton;
    @FXML private Button resetFilterButton;

    @FXML private Button undoButton, redoButton;

    private PatientsService service;
    private ObservableList<Patient> patientsData = FXCollections.observableArrayList();

    public void setService(PatientsService service) {
        this.service = service;
        refreshTable();
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getID()).asObject());
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        emailColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        phoneColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTelephone()));
        ageColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getAge()).asObject());

        patientTable.setItems(patientsData);

        addButton.setOnAction(event -> addHandler());
        updateButton.setOnAction(event -> updateHandler());
        deleteButton.setOnAction(event -> deleteHandler());

        filterNameButton.setOnAction(event -> applyNameFilter());
        filterAgeButton.setOnAction(event -> applyAgeFilter());
        resetFilterButton.setOnAction(event -> refreshTable());

        undoButton.setOnAction(event -> undoHandler());
        redoButton.setOnAction(event -> redoHandler());

        patientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSelection) -> {
            if (newSelection != null) {
                idField.setText(String.valueOf(newSelection.getID()));
                nameField.setText(newSelection.getName());
                emailField.setText(newSelection.getEmail());
                phoneField.setText(newSelection.getTelephone());
                ageField.setText(String.valueOf(newSelection.getAge()));
            }
        });
    }

    private void applyNameFilter() {
        String name = filterNameField.getText();
        if (name == null || name.isEmpty()) {
            showAlert("Warning", "Please enter a name to filter.");
            return;
        }
        try {
            FilterPatientsByName filter = new FilterPatientsByName(name);
            patientsData.clear();
            service.getAllFiltered(filter).forEach(patientsData::add);
        } catch (Exception e) {
            showAlert("Error", "Filter Error: " + e.getMessage());
        }
    }

    private void applyAgeFilter() {
        try {
            int age = Integer.parseInt(filterAgeField.getText());
            FilterPatientsByAge filter = new FilterPatientsByAge(age);
            patientsData.clear();
            service.getAllFiltered(filter).forEach(patientsData::add);
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for age.");
        } catch (Exception e) {
            showAlert("Error", "Filter Error: " + e.getMessage());
        }
    }

    private void refreshTable() {
        if (service != null) {
            patientsData.setAll(service.getAll());
        }
        filterNameField.clear();
        filterAgeField.clear();
    }

    private void addHandler() {
        try {
            Patient patient = extractFromFields();
            service.addPatient(patient);
            refreshTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void updateHandler() {
        try {
            Patient patient = extractFromFields();
            service.updatePatient(patient);
            refreshTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void deleteHandler() {
        Patient patientToDelete = patientTable.getSelectionModel().getSelectedItem();
        if (patientToDelete != null) {
            try {
                service.removePatient(patientToDelete);
                refreshTable();
                clearFields();
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        }
    }

    private void undoHandler() {
        try {
            service.undo();
            refreshTable();
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void redoHandler() {
        try {
            service.redo();
            refreshTable();
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private Patient extractFromFields() {
        int id = Integer.parseInt(idField.getText());
        int age = Integer.parseInt(ageField.getText());
        return new Patient(id, nameField.getText(), emailField.getText(), phoneField.getText(), age);
    }

    private void clearFields() {
        idField.clear(); nameField.clear(); emailField.clear(); phoneField.clear(); ageField.clear();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }
}