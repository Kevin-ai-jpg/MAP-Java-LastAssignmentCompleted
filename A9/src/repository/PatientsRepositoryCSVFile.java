package repository;

import java.io.*;

import domain.Patient;

public class PatientsRepositoryCSVFile extends FileRepository<Integer, Patient> {
    public PatientsRepositoryCSVFile(String filename) {
        super(filename);
    }

    public static final int NUMBER_OF_ATTRIBUTES = 5;

    @Override
    public void readFromFile() {


        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line = bufferedReader.readLine();
            while(line != null) {
                String[] tokens = line.split(",");
                if(tokens.length == NUMBER_OF_ATTRIBUTES){
                    int id = Integer.parseInt(tokens[0]);
                    String name = tokens[1];
                    String email = tokens[2];
                    String telephone = tokens[3];
                    int age = Integer.parseInt(tokens[4]);

                    Patient patient = new Patient(id, name, email, telephone, age);
                    super.add(patient.getID(),  patient);
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
            for (Patient patient : this.elements.values()){
                bufferedWriter.write(patient.getID() + "," + patient.getName() + "," + patient.getEmail() + "," + patient.getTelephone() + "," + patient.getAge() + "\n");
            }
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
