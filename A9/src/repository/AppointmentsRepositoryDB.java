package repository;

import Validator.ValidationException;
import domain.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class AppointmentsRepositoryDB implements IRepository<Integer, Appointment> {
    private String URL;
    private Connection connection = null;
    public AppointmentsRepositoryDB(String URL) {
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
    public void add(Integer integer, Appointment AppointmentToAdd) throws ValidationException {
        openConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into Appointments VALUES (?,?,?,?)");
            preparedStatement.setInt(1, AppointmentToAdd.getID());
            preparedStatement.setInt(2, AppointmentToAdd.getPatientID());
            preparedStatement.setString(3, AppointmentToAdd.getDate());
            preparedStatement.setString(4, AppointmentToAdd.getTime());
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
    public Optional<Appointment> delete(Integer integer) throws ValidationException {
        openConnection();
        try {
            Appointment appointment = findById(integer).orElse(null);
            if (appointment == null) {
                return Optional.empty();
            }
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE from Appointments where AppointmentID = ?");
            preparedStatement.setInt(1, integer);
            preparedStatement.executeUpdate();
            return Optional.of(appointment);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public void modify(Integer integer, Appointment newUpdatedAppointment) throws ValidationException {
        openConnection();
        try {
            String SqlModifyingStatement = "UPDATE Appointments SET PatientID=?, AppDate=?, AppTime=? WHERE AppointmentID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(SqlModifyingStatement);

            preparedStatement.setInt(1, newUpdatedAppointment.getPatientID());
            preparedStatement.setString(2, newUpdatedAppointment.getDate());
            preparedStatement.setString(3, newUpdatedAppointment.getTime());
            preparedStatement.setInt(4,  integer);

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public Optional<Appointment> findById(Integer integer) throws ValidationException {
        openConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Appointments WHERE AppointmentID = ?");
            preparedStatement.setInt(1, integer);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Appointment appointment = new Appointment(
                    resultSet.getInt("AppointmentID"),
                    resultSet.getInt("PatientID"),
                    resultSet.getString("AppDate"),
                    resultSet.getString("AppTime")
                );
                return Optional.of(appointment);
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
    public ArrayList<Appointment> getAll() {
        ArrayList<Appointment> appList = new ArrayList<>();
        openConnection();
        try {
            String sqlStringGetsAllDataFromAppointments = "SELECT * FROM Appointments";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStringGetsAllDataFromAppointments);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Appointment appointmentToDisplay = new Appointment(resultSet.getInt("AppointmentID"), resultSet.getInt("PatientID"), resultSet.getString("AppDate"), resultSet.getString("AppTime"));
                appList.add(appointmentToDisplay);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return appList;
    }
}
