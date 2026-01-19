package utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import domain.Patient;

public class TraditionalThreadsExample {
    public static void updateHighRiskPatients(List<Patient> patients) {
        int numberOfThreads = 4;
        int totalPatients = patients.size();
        int patientsPerThread = totalPatients / numberOfThreads;
        TraditionalThreads[] threads = new TraditionalThreads[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            int startIdx = i * patientsPerThread;
            int endIdx;
            if (i == numberOfThreads - 1) {
                endIdx = totalPatients;
            } else {
                endIdx = startIdx + patientsPerThread;
            }
            threads[i] = new TraditionalThreads(startIdx, endIdx, patients);
            threads[i].start();
        }

        for (TraditionalThreads thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateWithExecutorService(List<Patient> patients) {
        int noThreads = 4;
        ExecutorService executor = Executors.newFixedThreadPool(noThreads);
        int numberOfPatients = patients.size();
        int intervalLength = numberOfPatients / noThreads;
        int remainder = numberOfPatients % noThreads;

        int start = 0;
        for (int i = 0; i < noThreads; i++) {
            int end = start + intervalLength;
            if (i == noThreads - 1)
                end = end + remainder;

            int startIdx = start;
            int endIdx = end;

            executor.submit(() -> {
                for (int k = startIdx; k < endIdx; k++) {
                    Patient patient = patients.get(k);
                    if (patient.getAge() > 60) {
                        patient.setName(patient.getName() + " - High Risk");
                    }
                }
            });
            start = end;
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
