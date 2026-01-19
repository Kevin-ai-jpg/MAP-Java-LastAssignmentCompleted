package repository;

import java.io.*;

import domain.Appointment;
import domain.Patient;

public class AppointmentRepositoryCSVFile extends FileRepository<Integer, Appointment> {
    public AppointmentRepositoryCSVFile(String filename) {
        super(filename);
    }
    public static final int  NUMBER_OF_ATTRIBUTES = 4;
    @Override
    public void readFromFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line = bufferedReader.readLine();
            while(line != null) {
                String[] tokens = line.split(",");
                if(tokens.length == NUMBER_OF_ATTRIBUTES){
                    int id = Integer.parseInt(tokens[0]);
                    int patientId = Integer.parseInt(tokens[1]);
                    String date = tokens[2];
                    String time = tokens[3];

                    Appointment appointmentToRead = new Appointment(id,  patientId, date, time);
                    super.add(appointmentToRead.getID(), appointmentToRead);
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void writeToFile(){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename))){
            for (Appointment appoinmentToDisplay : this.elements.values()){
                bufferedWriter.write(appoinmentToDisplay.getID() + "," + appoinmentToDisplay.getPatientID() + "," + appoinmentToDisplay.getDate() + "," + appoinmentToDisplay.getTime() + "\n");
            }
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
