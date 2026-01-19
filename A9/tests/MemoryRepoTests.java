import domain.Patient;
import Validator.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.MemoryRepository;

import java.util.Optional;

class MemoryRepositoryTest {

    private MemoryRepository<Integer, Patient> repo;
    private Patient patient1;
    private Patient patient2;

    @BeforeEach
    void setUpRepo() {
        repo = new MemoryRepository<>();
        patient1 = new Patient(1, "Ana", "ana@mail.com", "111", 25);
        patient2 = new Patient(2, "Bogdan", "bogdan@mail.com", "222", 30);
        try {
            repo.add(patient1.getID(), patient1);
            repo.add(patient2.getID(), patient2);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAdd() {
        Patient patient3 = new Patient(3, "Carmen", "carmen@mail.com", "333", 40);

        try {
            repo.add(patient3.getID(), patient3);
        } catch (ValidationException e) {
            assert false;
        }
        Assertions.assertEquals(3, repo.getAll().size());

        try {
            repo.add(patient3.getID(), patient3);
            assert false;
        } catch (ValidationException e) {
            assert true;
        }
        Assertions.assertEquals(3, repo.getAll().size());
    }

    @Test
    void testDelete() {
        try {
            repo.delete(1);
        } catch (ValidationException e) {
            Assertions.fail("Successful delete threw ValidationException: " + e.getMessage());
        }
        Assertions.assertEquals(1, repo.getAll().size());
        try {
            Optional<Patient> result = repo.delete(99);
            Assertions.assertTrue(result.isEmpty());
        } catch (ValidationException e) {
            Assertions.fail("Deleting non-existent ID should NOT throw ValidationException.");
        }
        Assertions.assertEquals(1, repo.getAll().size());
        Assertions.assertThrows(ValidationException.class, () -> {
            repo.delete(null);
        });
        Assertions.assertEquals(1, repo.getAll().size());
    }
    @Test
    void testFindById() {
        Optional<Patient> found = Optional.empty();
        try {
            found = repo.findById(1);
        } catch (ValidationException e) {
            assert false;
        }
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(patient1.getName(), found.get().getName());

        try {
            Optional<Patient> notFound = repo.findById(99);
            Assertions.assertFalse(notFound.isPresent());
        } catch (ValidationException e) {
            assert false;
        }
    }

    @Test
    void testModify() {
        Patient updatedPatient = new Patient(1, "Ana Maria", "anamaria@mail.com", "111", 26);
        try {
            repo.modify(1, updatedPatient);
        } catch (ValidationException e) {
            assert false;
        }
        Optional<Patient> foundOptional = Optional.empty();
        try {
            foundOptional = repo.findById(1);
        } catch (ValidationException e) {
            assert false;
        }
        Assertions.assertTrue(foundOptional.isPresent());
        Patient found = foundOptional.get();
        Assertions.assertEquals("Ana Maria", found.getName());
        Assertions.assertEquals(26, found.getAge());
        Assertions.assertEquals(2, repo.getAll().size());

        Patient patientNoRepo = new Patient(4, "alin", "alin@mail.com", "1234", 50);
        try {
            repo.modify(4, patientNoRepo);
            assert false;
        } catch (ValidationException e) {
            assert true;
        }
        Assertions.assertEquals(2, repo.getAll().size());
    }
}