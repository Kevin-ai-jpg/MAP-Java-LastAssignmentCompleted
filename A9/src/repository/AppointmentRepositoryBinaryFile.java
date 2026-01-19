package repository;

import domain.Appointment;
import domain.Patient;

import java.io.*;
import java.util.HashMap;
import java.util.Optional;

public class AppointmentRepositoryBinaryFile extends FileRepository<Integer, Appointment>{
    public  AppointmentRepositoryBinaryFile(String filename) {
        super(filename);
    }

    @Override
    protected void readFromFile() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(this.filename));
            this.elements = (HashMap<Integer, Appointment>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            this.elements = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error reading binary file: " + this.filename, e);
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

    @Override
    public Optional<Appointment> delete(Integer id) {
        return super.delete(id);
    }

}
