package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import service.AppointmentService;
import service.PatientsService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ReportsController {

    @FXML private Button btnPatientAge, btnPatientName, btnPatientEmail;
    @FXML private Button btnApptDate, btnApptTime;
    @FXML private TextArea outputArea;

    private PatientsService patientsService;
    private AppointmentService appointmentService;

    public void setServices(PatientsService patientsService, AppointmentService appointmentService) {
        this.patientsService = patientsService;
        this.appointmentService = appointmentService;
    }

    @FXML
    public void initialize() {
        btnPatientAge.setOnAction(event -> patientsService.generatePatientAgeReport());
        btnPatientName.setOnAction(event -> patientsService.generatePatientNameReport());
        btnPatientEmail.setOnAction(event -> patientsService.generatePatientEmailReport());

        btnApptDate.setOnAction(event -> appointmentService.generateAppointmentDateReport());
        btnApptTime.setOnAction(event -> appointmentService.generateAppointmentTimeReport());
    }
}