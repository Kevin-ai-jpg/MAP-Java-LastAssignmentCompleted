import Validator.PatientValidator;
import Validator.ValidationException;
import domain.Patient;
import filters.AbstractFilter;
import filters.FilterPatientsByAge;
import repository.IRepository;
import repository.MemoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.PatientsService;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PatientsServiceTests {

    private PatientsService service;
    private IRepository<Integer, Patient> repo;
    private PatientValidator validator;
    private Patient validPatient1;
    private Patient validPatient2;

    @BeforeEach
    void setUp() {
        repo = new MemoryRepository<>();
        validator = new PatientValidator();
        service = new PatientsService(repo, validator);
        validPatient1 = new Patient(1, "Ana", "ana@gmail.com", "0711111111", 25);
        validPatient2 = new Patient(2, "Bogdan", "bogdan@gmail.com", "0722222222", 30);
        try {
            service.addPatient(validPatient1);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddPatient() {
        try {
            service.addPatient(validPatient2);
        } catch (ValidationException e) {
            assert false;
        }
        assertEquals(2, service.getAll().size());

        try {
            service.addPatient(validPatient2);
            assert false;
        } catch (ValidationException e) {
            assert true;
            assertEquals("ID already exists for other entity", e.getMessage());
        }
        assertEquals(2, service.getAll().size());

        Patient invalidPatient = new Patient(0, "aaa", "aaa@mail.com", "0712345678", 30);
        try {
            service.addPatient(invalidPatient);
            assert false;
        } catch (ValidationException e) {
            assert true;
            assertEquals("Patient ID should be greater than 0.", e.getMessage());
        }
        assertEquals(2, service.getAll().size());
    }

    @Test
    void testRemovePatient() {
        try {
            service.removePatient(validPatient1);
        } catch (ValidationException e) {
            assert false;
        }
        assertEquals(0, service.getAll().size());

        try {
            service.removePatient(validPatient2);
        } catch (ValidationException e) {
            assert true;
            assertEquals("ID does not exist", e.getMessage());
        }
        assertEquals(0, service.getAll().size());

        Patient invalidPatient = new Patient(0, "aaa", "aaa@mail.com", "0712345678", 30);
        try {
            service.removePatient(invalidPatient);
            assert false;
        } catch (ValidationException e) {
            assert true;
            assertEquals("Patient ID should be greater than 0.", e.getMessage());
        }
        assertEquals(0, service.getAll().size());
    }

    @Test
    void testUpdatePatient() {
        Patient updatedPatient = new Patient(1, "Ana Maria", "ana@gmail.com", "0711111111", 26);

        try {
            service.updatePatient(updatedPatient);
        } catch (ValidationException e) {
            assert false;
        }
        assertEquals(1, service.getAll().size());
        assertEquals("Ana Maria", service.findById(1).get().getName());

        try {
            service.updatePatient(validPatient2);
            assert false;
        } catch (ValidationException e) {
            assert true;
            assertEquals("ID does not exist", e.getMessage());
        }
        assertEquals("Ana Maria", service.findById(1).get().getName());

        Patient invalidUpdate = new Patient(1, null, "ana@gmail.com", "0711111111", 26);
        try {
            service.updatePatient(invalidUpdate);
            assert false;
        } catch (ValidationException e) {
            assert true;
            assertEquals("Patient name should not be null.", e.getMessage());
        }
        assertEquals("Ana Maria", service.findById(1).get().getName());
    }

    @Test
    void testFindById() {
        Optional<Patient> found = Optional.empty();
        try {
            found = service.findById(1);
        } catch (ValidationException e) {
            assert false;
        }
        assertTrue(found.isPresent());
        assertEquals("Ana", found.get().getName());

        try {
            Optional<Patient> notFound = service.findById(3);
            assertFalse(notFound.isPresent());
        } catch (ValidationException e) {
            assert false;
        }
    }

    @Test
    void testGetAll() {
        assertEquals(1, service.getAll().size());
        try {
            service.addPatient(validPatient2);
        } catch (ValidationException e) {
            assert false;
        }
        assertEquals(2, service.getAll().size());
    }

    @Test
    void testGetAllFiltered() {
        try {
            service.addPatient(validPatient2);
        } catch (ValidationException e) {
            assert false;
        }

        AbstractFilter<Patient> filter = new FilterPatientsByAge(30);
        ArrayList<Patient> filteredList = null;

        try {
            filteredList = service.getAllFiltered(filter);
        } catch (ValidationException e) {
            assert false;
        }
        assertNotNull(filteredList);
        assertEquals(1, filteredList.size());
        assertEquals(2, filteredList.get(0).getID());
    }
}