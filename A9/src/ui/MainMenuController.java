package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import service.AppointmentService;
import service.PatientsService;

import java.io.IOException;

public class MainMenuController {

    @FXML private Button btnGoToPatients;
    @FXML private Button btnGoToAppointments;
    @FXML private Button btnGoToReports;

    private PatientsService patientsService;
    private AppointmentService appointmentService;

    public void setServices(PatientsService patientsService, AppointmentService appointmentService) {
        this.patientsService = patientsService;
        this.appointmentService = appointmentService;
    }

    @FXML
    public void initialize() {
        if (btnGoToPatients != null) {
            btnGoToPatients.setOnAction(event -> openWindow("PatientManagement.fxml", "Manage Patients"));
        }
        if (btnGoToAppointments != null) {
            btnGoToAppointments.setOnAction(event -> openWindow("AppointmentManagement.fxml", "Manage Appointments"));
        }
        if (btnGoToReports != null) {
            btnGoToReports.setOnAction(event -> openWindow("ReportsManagement.fxml", "Manage Reports"));
        }
    }

    private void openWindow(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof PatientManagementController) {
                ((PatientManagementController) controller).setService(patientsService);
            } else if (controller instanceof AppointmentManagementController) {
                ((AppointmentManagementController) controller).setService(appointmentService, patientsService);
            } else if (controller instanceof ReportsController) {
                ((ReportsController) controller).setServices(patientsService, appointmentService);
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}