import Validator.AppointmentValidator;
import Validator.PatientValidator;
import domain.Appointment;
import domain.Patient;
import repository.*;
import service.AppointmentService;
import service.PatientsService;
import ui.UI;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    public static IRepository<Integer, Patient> readPropertiesInitPatientsRepository(Properties properties) {
        IRepository<Integer, Patient> repository = null;
        String repositoryType = properties.getProperty("RepositoryType");

        if (repositoryType == null)
            throw new IllegalArgumentException("RepositoryType not found in properties file!");

        switch (repositoryType.toLowerCase()) {
            case "memory":
                repository = new PatientsRepository();
                repository.add(1, new Patient(1, "John", "john@gmail.com", "0774524445", 25));
                repository.add(2, new Patient(2, "Victor", "victor@gmail.com", "0735524445", 20));
                repository.add(3, new Patient(3, "Luca", "luca@gmail.com", "0735524445", 15));
                repository.add(4, new Patient(4, "Kevin", "kevin@gmail.com", "0735524445", 15));
                repository.add(5, new Patient(5, "Mark", "mark@gmail.com", "07654321452", 20));
                break;

            case "csvfile":
                String patientPath = properties.getProperty("PatientsPath");
                repository = new PatientsRepositoryCSVFile(patientPath);
                break;

            case "binaryfile":
                String patientBinPath = properties.getProperty("PatientsPath");
                repository = new PatientsRepositoryBinaryFile(patientBinPath);
                break;
            case "database":
                String databasePath = properties.getProperty("URL");
                repository = new PatientsRepositoryDB(databasePath);
                break;
            default:
                throw new IllegalArgumentException("Invalid repository type: " + repositoryType);
        }
        return repository;
    }

    public static IRepository<Integer, Appointment> readPropertiesInitAppointmentsRepository(Properties properties) {
        IRepository<Integer, Appointment> repository = null;
        String repoType = properties.getProperty("RepositoryType");

        if (repoType == null)
            throw new IllegalArgumentException("RepositoryType not found in properties file");

        switch (repoType.toLowerCase()) {
            case "memory":
                repository = new AppointmentRepository();
                repository.add(1, new Appointment(1, 1, "2025-11-10", "10:00"));
                repository.add(2, new Appointment(2, 2, "2025-11-12", "14:30"));
                repository.add(3, new Appointment(3, 3, "2025-11-15", "14:30"));
                repository.add(4, new Appointment(4, 1, "2025-11-15", "11:30"));
                repository.add(5, new Appointment(5, 5, "2025-12-01", "09:00"));
                break;

            case "csvfile":
                String appointmentPath = properties.getProperty("AppointmentsPath");
                repository = new AppointmentRepositoryCSVFile(appointmentPath);
                break;
            case "binaryfile":
                String appointmentBinPath = properties.getProperty("AppointmentsPath");
                repository = new AppointmentRepositoryBinaryFile(appointmentBinPath);
                break;
            case "database":
                String appointmentDatabasePath = properties.getProperty("URL");
                repository = new AppointmentsRepositoryDB(appointmentDatabasePath);
                break;
            default:
                throw new IllegalArgumentException("Invalid repository type: " + repoType);
        }
        return repository;
    }

    public static void main(String[] args) {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("settings.properties")) {

            Properties properties = new Properties();
            properties.load(input);

            IRepository<Integer, Patient> patientsRepository = readPropertiesInitPatientsRepository(properties);
            IRepository<Integer, Appointment> appointmentRepository = readPropertiesInitAppointmentsRepository(properties);

            PatientValidator patientValidator = new PatientValidator();
            AppointmentValidator appointmentValidator = new AppointmentValidator();

            PatientsService patientsService = new PatientsService(patientsRepository, patientValidator, (AppointmentRepository) appointmentRepository);
            AppointmentService appointmentService = new AppointmentService(appointmentRepository, appointmentValidator);

            UI ui = new UI(patientsService, appointmentService);
            ui.runMenu();

        } catch (IOException e) {
            System.err.println("Error reading settings.properties: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}
