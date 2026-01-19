import Validator.AppointmentValidator;
import domain.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.IRepository;
import repository.MemoryRepository;
import service.AppointmentService;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentServiceTests {

    private AppointmentService service;
    private IRepository<Integer, Appointment> repo;
    private AppointmentValidator validator;

    @BeforeEach
    void setUp() {
        repo = new MemoryRepository<>();
        validator = new AppointmentValidator();
        service = new AppointmentService(repo, validator);
    }

    @Test
    void testUndoRedo() {
        // Add an appointment and undo
        Appointment appointment = new Appointment(1, 1, "2026-01-06", "10:00");
        service.addAppointment(appointment);
        assertEquals(1, service.getAll().size());

        service.undo();
        assertEquals(0, service.getAll().size());

        // Redo the addition
        service.redo();
        assertEquals(1, service.getAll().size());

        // Remove an appointment and undo
        service.removePatient(appointment);
        assertEquals(0, service.getAll().size());

        service.undo();
        assertEquals(1, service.getAll().size());

        // Update an appointment and undo
        Appointment updatedAppointment = new Appointment(1, 1, "2026-01-07", "11:00");
        service.updateAppointment(updatedAppointment);
        assertEquals("2026-01-07", service.findById(1).get().getDate());

        service.undo();
        assertEquals("2026-01-06", service.findById(1).get().getDate());
    }
}