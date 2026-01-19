package ui;

import Validator.ValidationException;
import domain.Appointment;
import domain.Patient;
import filters.FilterAppointmentsByDate;
import filters.FilterAppointmentsByTime;
import filters.FilterPatientsByAge;
import filters.FilterPatientsByName;
import service.AppointmentService;
import service.PatientsService;

import java.util.Optional;
import java.util.Scanner;

public class UI {
    private PatientsService patientsService;
    private AppointmentService appointmentService;
    private Scanner scanner = new Scanner(System.in);

    public UI(PatientsService patientsService, AppointmentService appointmentService) {
        this.patientsService = patientsService;
        this.appointmentService = appointmentService;
    }

    public final int GET_ALL_PATIENTS = 1;
    public final int ADD_PATIENT = 2;
    public final int UPDATE_PATIENT = 3;
    public final int DELETE_PATIENT = 4;
    public final int  ADD_APPOINTMENT = 5;
    public final int UPDATE_APPOINTMENT = 6;
    public final int DELETE_APPOINTMENT = 7;
    public final int GET_ALL_APPOINTMENTS = 8;
    public final int FILTER_PATIENTS_BY_AGE = 9;
    public final int FILTER_PATIENTS_BY_NAME = 10;
    public final int FILTER_APPOINTMENTS_BY_DATE = 11;
    public final int FILTER_APPOINTMENTS_BY_TIME = 12;
    public final int GENERATE_PATIENT_AGE_REPORT = 14;
    public final int GENERATE_PATIENT_NAME_REPORT = 15;
    public final int GENERATE_PATIENT_EMAIL_REPORT = 16;
    public final int GENERATE_APPOINTMENT_DATE_REPORT = 17;
    public final int GENERATE_APPOINTMENT_TIME_REPORT = 18;
    public final int GENERATE_PATIENTS_GUI_APP = 19;
    public final int EXIT = 20;


    public void displayMenu() {
        System.out.println("Dentist App menu");
        System.out.println(GET_ALL_PATIENTS + " - " + "Get all patients");
        System.out.println(ADD_PATIENT + " - " + "Add patient");
        System.out.println(UPDATE_PATIENT + " - " + "Update patient");
        System.out.println(DELETE_PATIENT + " - " + "Delete patient");
        System.out.println(ADD_APPOINTMENT + " - " + "Add appointment to patient");
        System.out.println(UPDATE_APPOINTMENT + " - " + "Update appointment to patient");
        System.out.println(DELETE_APPOINTMENT + " - " + "Delete appointment to patient");
        System.out.println(GET_ALL_APPOINTMENTS + " - " + "Get all appointments");
        System.out.println(FILTER_PATIENTS_BY_AGE + " - " + "Filter patients by age");
        System.out.println(FILTER_PATIENTS_BY_NAME + " - " + "Filter patients by name");
        System.out.println(FILTER_APPOINTMENTS_BY_DATE + " - " + "Filter appointments by date");
        System.out.println(FILTER_APPOINTMENTS_BY_TIME + " - " + "Filter appointments by time");
        System.out.println(GENERATE_PATIENT_AGE_REPORT + " - " + "Generate Patient Age Report");
        System.out.println(GENERATE_PATIENT_NAME_REPORT + " - " + "Generate Patient Name Report");
        System.out.println(GENERATE_PATIENT_EMAIL_REPORT + " - " + "Generate Patient Email Report");
        System.out.println(GENERATE_APPOINTMENT_DATE_REPORT + " - " + "Generate Appointment Date Report");
        System.out.println(GENERATE_APPOINTMENT_TIME_REPORT + " - " + "Generate Appointment Time Report");
        System.out.println(GENERATE_PATIENTS_GUI_APP + " - " + "Generate the patient GUI");
        System.out.println(EXIT + " - " + "Exit");
    }

    public Appointment AppointmentInfo() {
        System.out.println("Enter appointment ID");
        int id =  scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter the patient ID that has requested the appointment");
        int PatientID = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter the appointment date(YYYY-MM-DD)");
        String date = scanner.nextLine();
//        scanner.nextLine();

        System.out.println("Enter the appointment time(HH:MM)");
        String time = scanner.nextLine();

        Appointment appointment = new Appointment(id, PatientID, date, time);
        return appointment;

    }

    public Patient PatientInfo() {
        System.out.print("Enter ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Telephone: ");
        String telephone = scanner.nextLine();

        System.out.print("Enter Age: ");
        int age = scanner.nextInt();

        Patient newPatient = new Patient(id, name, email, telephone, age);
        return newPatient;
    }

    public void runMenu() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = -1;
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid choice.");
                scanner.nextLine();
                continue;
            }
            if(choice == EXIT)
                running = false;
            try {
                switch (choice) {
                    case GET_ALL_PATIENTS:
                        patientsService.getAll().forEach(patientToDisplay -> System.out.println(patientToDisplay));
                        break;
                    case ADD_PATIENT:
                        //add a patient
                        patientsService.addPatient(PatientInfo());
                        break;

                    case UPDATE_PATIENT:
                        //update patient
                        System.out.println("What patient do you want to update?");
                        patientsService.getAll().forEach(patientToDisplay -> System.out.println(patientToDisplay));
                        System.out.println("Enter the id of the patient you want to update: ");
//                        int PatientidForUpdate = scanner.nextInt();
                        scanner.nextLine();
                        Patient newPatient = PatientInfo();

                        patientsService.updatePatient(newPatient);
                        break;
                    case DELETE_PATIENT:
                        //remove patient
                        System.out.print("Enter ID: ");
                        int PatientIdForRemoving = scanner.nextInt();
                        scanner.nextLine();
                        Optional<Patient> patientToRemove = patientsService.findById(PatientIdForRemoving);
                        if (patientToRemove.isPresent()) {
                            patientsService.removePatient(patientToRemove.get());
                        } else {
                            System.out.println("Patient not found.");
                        }
                        break;
                    case ADD_APPOINTMENT:
                        //add appointment
                        appointmentService.addAppointment(AppointmentInfo());
                        break;
                    case UPDATE_APPOINTMENT:
                        //update appointment
                        System.out.println("What appointment do you want to update?");
                        appointmentService.getAll().forEach(appointmentToDisplay -> System.out.println(appointmentToDisplay));
                        System.out.println("Enter the id of the appointment you want to update: ");
                        int AppointmentIdForUpdating = scanner.nextInt();
                        scanner.nextLine();
                        Appointment newAppointment = AppointmentInfo();

                        appointmentService.updateAppointment(newAppointment);
                        break;
                    case DELETE_APPOINTMENT:
                        //remove appointment
                        System.out.println("What appointment do you want to delete?(Enter the ID)");
                        int AppointmentIdForRemoving = scanner.nextInt();
                        scanner.nextLine();
                        Optional<Appointment> appointmentToRemove = appointmentService.findById(AppointmentIdForRemoving);
                        if (appointmentToRemove.isPresent()) {
                            appointmentService.removeAppointment(appointmentToRemove.get());
                        } else {
                            System.out.println("Appointment not found.");
                        }
                        break;
                    case GET_ALL_APPOINTMENTS:
                        //get all appointments
                        appointmentService.getAll().forEach(appointmentToDisplay -> System.out.println(appointmentToDisplay));
                        break;
                    case FILTER_PATIENTS_BY_AGE:
                        //filter patients by age
                        System.out.println("Enter the age for filter");
                        int ageConstraint = scanner.nextInt();
                        //scanner.nextLine();
                        FilterPatientsByAge ageFilter = new FilterPatientsByAge(ageConstraint);
                        patientsService.getAllFiltered(ageFilter).forEach(FilteredPatient -> System.out.println(FilteredPatient));
                        break;
                    case FILTER_PATIENTS_BY_NAME:
                        //filter patients by name
                        System.out.println("Enter the name for filter");
                        scanner.nextLine();
                        String nameConstraint = scanner.nextLine();
                        FilterPatientsByName nameFilter = new FilterPatientsByName(nameConstraint);
                        patientsService.getAllFiltered(nameFilter).forEach(FilteredPatient -> System.out.println(FilteredPatient));
                        break;
                    case FILTER_APPOINTMENTS_BY_DATE:
                        //filter appointment by date
                        System.out.println("Enter the date for filter(Appointment)");
                        scanner.nextLine();
                        String dateForFilter = scanner.nextLine();
                        FilterAppointmentsByDate dateFilter = new FilterAppointmentsByDate(dateForFilter);
                        appointmentService.getAllFiltered(dateFilter).forEach(FilteredAppointment -> System.out.println(FilteredAppointment));
                        break;
                    case FILTER_APPOINTMENTS_BY_TIME:
                        //filter appointment by time
                        System.out.println("Enter the time for filter(Appointment)");
                        scanner.nextLine();
                        String timeForFilter = scanner.nextLine();
                        FilterAppointmentsByTime timeFilter = new FilterAppointmentsByTime(timeForFilter);
                        appointmentService.getAllFiltered(timeFilter).forEach(FilteredAppointment -> System.out.println(FilteredAppointment));
                        break;
                    case GENERATE_PATIENT_AGE_REPORT:
                        patientsService.generatePatientAgeReport();
                        break;
                    case GENERATE_PATIENT_NAME_REPORT:
                        patientsService.generatePatientNameReport();
                        break;
                    case GENERATE_PATIENT_EMAIL_REPORT:
                        patientsService.generatePatientEmailReport();
                        break;
                    case GENERATE_APPOINTMENT_DATE_REPORT:
                        appointmentService.generateAppointmentDateReport();
                        break;
                    case GENERATE_APPOINTMENT_TIME_REPORT:
                        appointmentService.generateAppointmentTimeReport();
                        break;
                    case GENERATE_PATIENTS_GUI_APP:
//                        System.out.println("Launching JavaFX GUI...");
//                        MainApp.setServices(patientsService, appointmentService);
//                        MainApp.launchApp();
                        break;
                }
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Unknown Error" + e.getMessage());
            }
        }
    }
}
