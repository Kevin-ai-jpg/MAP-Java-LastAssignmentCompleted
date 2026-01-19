package utils;

import domain.Patient;

import java.util.List;

public class mainThreads {
    public static void main(String[] args) {
        int numberOfPatients = 100000;
        List<Patient> patients = generatePatients.generateSamplePatients(numberOfPatients);

        long startTime = System.currentTimeMillis();
        TraditionalThreadsExample.updateHighRiskPatients(patients);
        long endTime = System.currentTimeMillis();

        System.out.println("Time " + (endTime - startTime));

        int highRiskCount = 0;
        for (Patient patient : patients) {
            if (patient.getAge() > 60 && patient.getName().contains("High Risk")) {
                highRiskCount++;
            }
        }
        System.out.println("Total High Risk Patients for simple threads: " + highRiskCount);

        List<Patient> patients2 = generatePatients.generateSamplePatients(numberOfPatients);
        long startTime2 = System.currentTimeMillis();
        TraditionalThreadsExample.updateWithExecutorService(patients2);
        long endTime2 = System.currentTimeMillis();
        System.out.println("Time with Executor Service: " + (endTime2 - startTime2));

        int highRiskCount2 = 0;
        for (Patient patient : patients2) {
            if (patient.getAge() > 60 && patient.getName().contains("High Risk")) {
                highRiskCount2++;
            }
        }
        System.out.println("Total High Risk Patients for Executor Service: " + highRiskCount2);
    }
    
}
