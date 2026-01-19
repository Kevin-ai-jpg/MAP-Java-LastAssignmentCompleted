import Validator.PatientValidator;
import Validator.ValidationException;
import domain.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PatientValidatorTests {

    private PatientValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PatientValidator();
    }

    @Test
    void testValidateValidPatient() {
        Patient validPatient = new Patient(1, "Kevin", "kevin@email.com", "0712345678", 30);
        assertDoesNotThrow(() -> {
            validator.validate(validPatient);
        });
    }

    @Test
    void testValidateInvalidID() {
        Patient patient = new Patient(0, "Kevin", "kevin@email.com", "0712345678", 30);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validator.validate(patient);
        });

        assertEquals("Patient ID should be greater than 0.", exception.getMessage());
    }

    @Test
    void testValidateInvalidName() {
        Patient patient = new Patient(1, null, "kevin@email.com", "0712345678", 30);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validator.validate(patient);
        });

        assertEquals("Patient name should not be null.", exception.getMessage());
    }

    @Test
    void testValidateInvalidEmail() {
        Patient patientNull = new Patient(1, "Kevin", null, "0712345678", 30);
        Patient patientNoAt = new Patient(1, "Kevin", "kevin.email.com", "0712345678", 30);
        Patient patientNoDot = new Patient(1, "Kevin", "kevin@emailcom", "0712345678", 30);

        String expectedError = "Patient email should be a valid email address.";

        ValidationException ex1 = assertThrows(ValidationException.class, () -> validator.validate(patientNull));
        ValidationException ex2 = assertThrows(ValidationException.class, () -> validator.validate(patientNoAt));
        ValidationException ex3 = assertThrows(ValidationException.class, () -> validator.validate(patientNoDot));

        assertEquals(expectedError, ex1.getMessage());
        assertEquals(expectedError, ex2.getMessage());
        assertEquals(expectedError, ex3.getMessage());
    }
    @Test
    void testValidateInvalidTelephone() {
        Patient patient = new Patient(1, "Kevin", "kevin@email.com", "", 30);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validator.validate(patient);
        });
        assertEquals("Patient telephone should not be empty.", exception.getMessage());
    }
    @Test
    void testValidateInvalidAge() {
        Patient patient = new Patient(1, "Kevin", "kevin@email.com", "0712345678", 0);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validator.validate(patient);
        });
        assertEquals("Patient age should be greater than 0.", exception.getMessage());
    }
    @Test
    void testValidateMultipleErrors() {
        Patient patient = new Patient(0, null, "kevinemail", "123", -5);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validator.validate(patient);
        });
        String expectedError = "Patient ID should be greater than 0.Patient name " +
                "should not be null.Patient email should be a valid email address." +
                "Patient age should be greater than 0.";

        assertEquals(expectedError, exception.getMessage());
    }
}