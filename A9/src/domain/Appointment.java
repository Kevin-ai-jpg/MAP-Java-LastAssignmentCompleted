package domain;

import java.io.Serializable;

public class Appointment implements Identifiable<Integer>, Serializable {
    private int id;
    private int PatientID;
    private String date;
    private String time;

    public Appointment(int id, int PatientID, String date, String time) {
        this.id = id;
        this.PatientID = PatientID;
        this.date = date;
        this.time = time;
    }

    @Override
    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }
    public int getPatientID() {
        return PatientID;
    }
    public void setPatientID(int PatientID) {
        this.PatientID = PatientID;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String toString(){
        return "Appointment with id: " + this.id + " is requested by the patient with id: " + this.getPatientID() +
                " at the date and time: " + this.date + " " + this.time;
    }
}
