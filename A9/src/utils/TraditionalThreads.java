package utils;

import domain.Patient;

import java.util.List;

class TraditionalThreads extends Thread {
    private int startIdx, endIdx;
    private List<Patient> patients;
    public TraditionalThreads(int startIdx, int endIdx, List<Patient> patients) {
        this.startIdx = startIdx;
        this.endIdx = endIdx;
        this.patients = patients;
    }
    public void run() {
        for (int i = startIdx; i < endIdx; i++) {
            Patient patient = patients.get(i);
            if (patient.getAge() > 60) {
                patient.setName(patient.getName() + " - High Risk");
            }
        }
    }
}

