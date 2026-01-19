import domain.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientTests {

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient(1, "Kevin", "kevin@example.com", "1234567890", 30);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(1, patient.getID());
        assertEquals("Kevin", patient.getName());
        assertEquals("kevin@example.com", patient.getEmail());
        assertEquals("1234567890", patient.getTelephone());
        assertEquals(30, patient.getAge());
    }

    @Test
    void testSetters() {
        patient.setID(2);
        patient.setName("kevin");
        patient.setEmail("kevinn@example.com");
        patient.setTelephone("0987654321");
        patient.setAge(25);

        assertEquals(2, patient.getID());
        assertEquals("kevin", patient.getName());
        assertEquals("kevinn@example.com", patient.getEmail());
        assertEquals("0987654321", patient.getTelephone());
        assertEquals(25, patient.getAge());
    }

    @Test
    void testEquals() {
        assertTrue(patient.equals(patient));
        Patient identicalPatient = new Patient(1, "Kevin", "kevin@example.com", "1234567890", 30);
        assertTrue(patient.equals(identicalPatient));
        assertFalse(patient.equals(null));
        Patient differentIdPatient = new Patient(2, "Kevin", "kevin@example.com", "1234567890", 30);
        assertFalse(patient.equals(differentIdPatient));
    }

    @Test
    void testHashCode() {
        Patient patient2 = new Patient(1, "Andrei", "andrei@mail.com", "1233456789", 20);
        assertEquals(patient.hashCode(), patient2.hashCode());

        Patient patient3 = new Patient(2, "Kevin", "kevin@example.com", "1234567890", 30);
        assertNotEquals(patient.hashCode(), patient3.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = "Patient: Kevin with id: 1 age: 30 email: kevin@example.com telephone: 1234567890";
        assertEquals(expectedString, patient.toString());
    }
}