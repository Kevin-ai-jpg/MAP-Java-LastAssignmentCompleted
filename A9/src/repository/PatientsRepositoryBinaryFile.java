package repository;

import domain.Patient;

import java.io.*;
import java.util.HashMap;

public class PatientsRepositoryBinaryFile extends FileRepository<Integer, Patient>{
    public  PatientsRepositoryBinaryFile(String filename) {
        super(filename);
    }

    @Override
    protected void readFromFile() {
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(this.filename));
            this.elements = (HashMap<Integer, Patient>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            this.elements = new HashMap<>();
        } catch (IOException e) {
            throw new RuntimeException("Error reading binary file: " + this.filename, e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found during deserialization.", e);
        }
    }

    @Override
    protected void writeToFile() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(this.filename));
            objectOutputStream.writeObject(this.elements);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
