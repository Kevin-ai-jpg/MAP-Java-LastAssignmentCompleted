package repository;

import Validator.ValidationException;
import domain.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class PatientsRepositoryDB implements IRepository<Integer, Patient> {
    private String URL;
    private Connection connection = null;
    public PatientsRepositoryDB(String URL) {
        this.URL = URL;
    }


    private void openConnection() {
        try {
            if (connection == null || connection.isClosed())
                connection = DriverManager.getConnection(this.URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void add(Integer integer, Patient patient) throws ValidationException {
        openConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into Patients VALUES (?,?,?,?,?)");
            preparedStatement.setInt(1, patient.getID());
            preparedStatement.setString(2,patient.getName());
            preparedStatement.setString(3, patient.getEmail());
            preparedStatement.setString(4, patient.getTelephone());
            preparedStatement.setDouble(5,patient.getAge());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        finally
        {
            closeConnection();
        }
    }

    @Override
    public Optional<Patient> delete(Integer integer) throws ValidationException {
        openConnection();
        try {
            Patient patient = findById(integer).orElse(null);
            if (patient == null) {
                return Optional.empty();
            }
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE from Patients where PatientsID = ?");
            preparedStatement.setInt(1, integer);
            preparedStatement.executeUpdate();
            return Optional.of(patient);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public void modify(Integer integer, Patient patient) throws ValidationException {
        openConnection();
        try {
            String sqlStringForUpdating = "UPDATE Patients SET Name=?, Email=?, Phone=?, Age=? WHERE PatientsID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStringForUpdating);

            preparedStatement.setString(1, patient.getName());
            preparedStatement.setString(2, patient.getEmail());
            preparedStatement.setString(3, patient.getTelephone());
            preparedStatement.setInt(4,patient.getAge());
            preparedStatement.setInt(5,integer);

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public Optional<Patient> findById(Integer integer) throws ValidationException {
        openConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Patients WHERE PatientsID=?");
            preparedStatement.setInt(1, integer);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Patient patient = new Patient(
                    resultSet.getInt("PatientsID"),
                    resultSet.getString("Name"),
                    resultSet.getString("Email"),
                    resultSet.getString("Phone"),
                    resultSet.getInt("Age")
                );
                return Optional.of(patient);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public ArrayList<Patient> getAll() {
        ArrayList<Patient> patientsList = new ArrayList<>();
        openConnection();
        try {
            String sqlStringToGetAllDataFromPatients = "SELECT * FROM Patients";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStringToGetAllDataFromPatients);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Patient patient = new Patient(
                        resultSet.getInt("PatientsID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Email"),
                        resultSet.getString("Phone"),
                        resultSet.getInt("Age")
                );
                patientsList.add(patient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return patientsList;
    }
}
