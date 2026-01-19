import domain.Patient;
import filters.FilterPatientsByAge;
import filters.FilterPatientsByName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FilterPatientsTests {

    @Test
    void testAccept() {
        FilterPatientsByAge filter1 = new FilterPatientsByAge(30);
        FilterPatientsByName filter2 = new FilterPatientsByName("Ana");

        Patient patientWithMatchingAge = new Patient(1, "Ana", "ana@mail.com", "111", 30);
        Patient patientWithMatchingName = new Patient(2, "Bogdan", "bogdan@mail.com", "222", 25);

        boolean resultMatch = filter1.accept(patientWithMatchingAge);
        boolean resultMatch2 = filter2.accept(patientWithMatchingName);
        Assertions.assertTrue(resultMatch);
        Assertions.assertTrue(resultMatch);

        boolean resultNoMatch = filter1.accept(patientWithMatchingName);
        boolean resultNoMatch2 = filter2.accept(patientWithMatchingAge);
        Assertions.assertFalse(resultNoMatch);
        Assertions.assertFalse(resultNoMatch);
    }
}