package com.bridgelabz.Service;

import com.bridgelabz.Connection.DBConnection;
import com.bridgelabz.Model.EmployeePayrollData;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {
    public enum StatementType{
        PREPARED_STATEMENT,STATEMENT
    }
    DBConnection dbConnection = new DBConnection();
    private PreparedStatement employeePayrollDataStatement;
    private static EmployeePayrollDBService employeePayrollDBService;
    EmployeePayrollDBService() {

    }

    public static EmployeePayrollDBService getInstance() {
        if(employeePayrollDBService==null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }
    /**
     * @return employee data list which is read from database
     */
    public List<EmployeePayrollData> readData() {
        String sql = "SELECT * FROM employeepayroll1;";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
        try (Connection connection = dbConnection.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollList = null;
        if(this.employeePayrollDataStatement == null)
            this.preparedStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id,name,salary,startDate));
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }
    public int updateEmployeeData(String name, double salary,StatementType type) {
        switch(type) {
            case STATEMENT:
                return this.updateDataUsingStatement(name, salary);
            case PREPARED_STATEMENT:
                return this.updateDataUsingPreparedStatement(name, salary);
            default :
                return 0;
        }
    }

    private void preparedStatementForEmployeeData() {
        try {
            Connection connection = dbConnection.getConnection();
            String sql = "Select * from employeepayroll1 WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

    }

    public int updateEmployeeData(String name, double salary) {
        return this.updateDataUsingStatement(name, salary);
    }

    private int updateDataUsingStatement(String name, double salary) {
        String sql = String.format("UPDATE employeepayroll1 SET salary = %.2f where name = '%s';", salary,name);
        try(Connection connection = dbConnection.getConnection();) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private int updateDataUsingPreparedStatement(String name,double salary) {
        String sql = "UPDATE employeepayroll1 SET salary = ? WHERE NAME = ?";
        try(Connection connection = dbConnection.getConnection();) {
            PreparedStatement preparedStatementUpdate = connection.prepareStatement(sql);
            preparedStatementUpdate.setDouble(1, salary);
            preparedStatementUpdate.setString(2, name);
            return preparedStatementUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
