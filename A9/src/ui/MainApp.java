package ui;

import Validator.AppointmentValidator;
import Validator.PatientValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.AppointmentRepositoryCSVFile;
import repository.PatientsRepositoryCSVFile;
import service.AppointmentService;
import service.PatientsService;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        PatientsRepositoryCSVFile patientsRepo = new PatientsRepositoryCSVFile("data/patients.csv");
        PatientValidator patientValidator = new PatientValidator();
        AppointmentRepositoryCSVFile apptRepo = new AppointmentRepositoryCSVFile("data/appointments.csv");
        AppointmentValidator appointmentValidator = new AppointmentValidator();

        PatientsService patientsService = new PatientsService(patientsRepo, patientValidator, apptRepo);
        AppointmentService appointmentService = new AppointmentService(apptRepo, appointmentValidator); // Add validators if needed

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Parent root = loader.load();

        MainMenuController controller = loader.getController();
        controller.setServices(patientsService, appointmentService);

        primaryStage.setTitle("Dental Clinic Manager");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}