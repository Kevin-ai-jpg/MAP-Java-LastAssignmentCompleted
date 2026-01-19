package domain;

import java.io.Serializable;
import java.util.Objects;

public class Patient implements Identifiable<Integer>, Serializable {
    private int id;
    private String name;
    private String email;
    private String telephone;
    private int age;
    public Patient(int id, String name, String email, String telephone, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) return true;
        else if (other != null && this.getClass() == other.getClass()) {
            Patient patient = (Patient) other;
            return this.id == patient.id && this.name == patient.name && this.email == patient.email && this.telephone == patient.telephone && this.age == patient.age;
        }
        else return false;
    }

    @Override
    public String toString() {
        return "Patient: " + this.name + " with id: " + this.id + " age: " + this.age + " email: " + this.email + " telephone: " + this.telephone;
    }
}
