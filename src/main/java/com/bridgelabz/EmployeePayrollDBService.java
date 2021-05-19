package com.bridgelabz;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {
    DBConnection dbConnection = new DBConnection();

    public List<EmployeePayrollData> readData() {
        String sql = "SELECT * FROM employeepayroll1;";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
        try(Connection connection = dbConnection.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary,startDate));
            }
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }
}
