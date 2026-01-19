package ui;

import domain.Patient;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import repository.IRepository;
import service.PatientsService;


public class PatientUI extends Application {

    private static PatientsService StaticPatientsService;
    private ObservableList<Patient> TableToDisplayPatientsData = FXCollections.observableArrayList();

    // UI Controls
    private TableView<Patient> PatientsDataTable = new TableView<>();
    private TextField textFieldForPatientID = new TextField();
    private TextField textFieldForPatientName = new TextField();
    private TextField textFieldForPatientEmail = new TextField();
    private TextField textFieldForPatientPhone = new TextField();
    private TextField textFieldForPatientAge = new TextField();

    public static void setStaticPatientsService(PatientsService service) {
        StaticPatientsService = service;
    }

    @Override
    public void start(Stage stage) {
        if (StaticPatientsService == null) {
            IRepository<Integer, Patient> patientsRepository = new repository.PatientsRepository();
            repository.AppointmentRepository appointmentRepository = new repository.AppointmentRepository();
            Validator.PatientValidator patientValidator = new Validator.PatientValidator();
            this.StaticPatientsService = new PatientsService(patientsRepository, patientValidator, appointmentRepository);
        }
        TableColumn<Patient, Integer> columnToInputPatientID = new TableColumn<>("ID");
        columnToInputPatientID.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getID()));

        TableColumn<Patient, String> columnToInputPatientName = new TableColumn<>("Name");
        columnToInputPatientName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));

        TableColumn<Patient, String> columnToInputPatientEmail = new TableColumn<>("Email");
        columnToInputPatientEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));

        TableColumn<Patient, String> columnToInputPatientPhone = new TableColumn<>("Phone");
        columnToInputPatientPhone.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTelephone()));

        TableColumn<Patient, Integer> columnToInputPatientAge = new TableColumn<>("Age");
        columnToInputPatientAge.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getAge()));

        PatientsDataTable.getColumns().addAll(columnToInputPatientID, columnToInputPatientName, columnToInputPatientEmail, columnToInputPatientPhone, columnToInputPatientAge);
        PatientsDataTable.setItems(TableToDisplayPatientsData);

        textFieldForPatientID.setPromptText("ID");
        textFieldForPatientName.setPromptText("Name");
        textFieldForPatientEmail.setPromptText("Email");
        textFieldForPatientPhone.setPromptText("Telephone");
        textFieldForPatientAge.setPromptText("Age");

        Button buttonToAddPatient = new Button("Add Patient");
        buttonToAddPatient.setOnAction(actionEvent -> addAction());

        Button buttonToUpdatePatient = new Button("Update Patient");
        buttonToUpdatePatient.setOnAction(actionEvent -> updateAction());

        Button buttonToDeletePatient = new Button("Delete Patient");
        buttonToDeletePatient.setOnAction(actionEvent -> deleteAction());

        VBox rightSidebarWithPatientDetails = new VBox(30);
        rightSidebarWithPatientDetails.setPadding(new Insets(15));
        rightSidebarWithPatientDetails.setPrefWidth(300);
        rightSidebarWithPatientDetails.getChildren().addAll(
                new Label("Patient Details:"),
                textFieldForPatientID, textFieldForPatientName, textFieldForPatientEmail, textFieldForPatientPhone, textFieldForPatientAge,
                buttonToAddPatient, buttonToUpdatePatient, buttonToDeletePatient
        );

        PatientsDataTable.getSelectionModel().selectedItemProperty().addListener((observableValue, OldPatient, NewPatient) -> {
            if (NewPatient != null) {
                textFieldForPatientID.setText(String.valueOf(NewPatient.getID()));
                textFieldForPatientName.setText(NewPatient.getName());
                textFieldForPatientEmail.setText(NewPatient.getEmail());
                textFieldForPatientPhone.setText(NewPatient.getTelephone());
                textFieldForPatientAge.setText(String.valueOf(NewPatient.getAge()));
                textFieldForPatientID.setDisable(true);
            }
        });

        BorderPane mainGUILayout = new BorderPane();
        mainGUILayout.setCenter(PatientsDataTable);
        mainGUILayout.setRight(rightSidebarWithPatientDetails);

        Scene displayedWindow = new Scene(mainGUILayout, 900, 600);
        stage.setTitle("Patient Management System");
        stage.setScene(displayedWindow);
        stage.show();

        if (StaticPatientsService != null) refreshTable();
    }

    private void addAction() {
        try {
            Patient patientToAdd = extractPatientFromForm();
            StaticPatientsService.addPatient(patientToAdd);
            refreshTable();
        } catch (Exception AnyException) {
            showAlert("Error Adding", AnyException.getMessage());
        }
    }

    private void updateAction() {
        try {
            Patient patientToUpdate = extractPatientFromForm();
            StaticPatientsService.updatePatient(patientToUpdate);
            refreshTable();
        } catch (Exception AnyException) {
            showAlert("Error Updating", AnyException.getMessage());
        }
    }

    private void deleteAction() {
        Patient selected = PatientsDataTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "no patient selected");
            return;
        }
        try {
            StaticPatientsService.removePatient(selected);
            refreshTable();
        } catch (Exception AnyException) {
            showAlert("Error Deleting", AnyException.getMessage());
        }
    }

    private void refreshTable() {
        if (StaticPatientsService == null) return;
        TableToDisplayPatientsData.setAll(StaticPatientsService.getAll());
    }

    private Patient extractPatientFromForm() {
        int id = Integer.parseInt(textFieldForPatientID.getText());
        String name = textFieldForPatientName.getText();
        String email = textFieldForPatientEmail.getText();
        String phone = textFieldForPatientPhone.getText();
        int age = Integer.parseInt(textFieldForPatientAge.getText());
        return new Patient(id, name, email, phone, age);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}