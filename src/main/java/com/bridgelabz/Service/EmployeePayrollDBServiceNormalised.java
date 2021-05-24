package com.bridgelabz.Service;

import com.bridgelabz.Connection.DBConnection;
import com.bridgelabz.Model.EmployeePayrollData;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBServiceNormalised {
    DBConnection dbConnection = new DBConnection();

    private static EmployeePayrollDBServiceNormalised employeePayrollDBServiceNormalised;

    private PreparedStatement employeePayrollDataStatementNormalised;



    public static EmployeePayrollDBServiceNormalised getInstance() {
        if (employeePayrollDBServiceNormalised == null)
            employeePayrollDBServiceNormalised = new EmployeePayrollDBServiceNormalised();
        return employeePayrollDBServiceNormalised;
    }


    public List<EmployeePayrollData> readData() {
        String sql = "SELECT e.id,e.company_id,e.employee_name,e.gender,e.start,c.company_name,d.dept_name,p.basic_pay "
                + "FROM employee1 e JOIN company1 c" + " ON e.company_id = c.company_id " + "JOIN employee_department1 d2 "
                + "ON e.id = d2.emp_id " + "JOIN department1 d " + "ON d2.dept_id = d.dept_id " + "JOIN payrolldetails1 p "
                + "ON e.id = p.emp_id;";
        return this.getEmployeePayrollDataUsingSQLQuery(sql);
    }

    private List<EmployeePayrollData> getEmployeePayrollDataUsingSQLQuery(String sql) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
        try (Connection connection = dbConnection.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        List<String> department = new ArrayList<String>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int companyId = resultSet.getInt("company_id");
                String name = resultSet.getString("employee_name");
                String gender = resultSet.getString("gender");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                String companyName = resultSet.getString("company_name");
                String dept = resultSet.getString("dept_name");
                double salary = resultSet.getDouble("basic_pay");
                System.out.println(dept);
                department.add(dept);
                System.out.println(id);
                String[] departmentArray = new String[department.size()];
                EmployeePayrollData employee = new EmployeePayrollData(id, name, salary, startDate, gender, companyName,
                        companyId, department.toArray(departmentArray));
                if (employeePayrollList.stream().anyMatch(emp -> emp.equals(employee)))
                    employee.setDepartment(department.toArray(departmentArray));
                else {
                    employeePayrollList.add(employee);
                    department = new ArrayList<String>();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollList = null;
        if (this.employeePayrollDataStatementNormalised == null)
            this.preparedStatementForEmployeeData();
        try {
            employeePayrollDataStatementNormalised.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatementNormalised.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private void preparedStatementForEmployeeData() {
        try {
            Connection connection = dbConnection.getConnection();
            String sql = "SELECT e.id,e.company_id,e.employee_name,e.gender,e.start,c.company_name,d.dept_name,p.basic_pay "
                    + "FROM employee1 e JOIN company1 c" + " ON e.company_id = c.company_id " + "JOIN employee_department1 d2 "
                    + "ON e.id = d2.emp_id " + "JOIN department1 d " + "ON d2.dept_id = d.dept_id " + "JOIN payrolldetails1 p "
                    + "ON e.id = p.emp_id WHERE e.employee_name = ?";
            employeePayrollDataStatementNormalised = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public int updateEmployeeData(String name, double salary, EmployeePayrollDBService.StatementType type) {
        switch (type) {
            case STATEMENT:
                return this.updateDataUsingStatement(name, salary);
            case PREPARED_STATEMENT:
                return this.updateDataUsingPreparedStatement(name, salary);
            default:
                return 0;
        }
    }

    private int updateDataUsingPreparedStatement(String name, double salary) {
        return 0;
    }

    private int updateDataUsingStatement(String name, double salary) {
        String sql = String.format("UPDATE payrolldetails1 SET basic_pay = %.2f WHERE emp_id = "
                + "(SELECT id from employee1 WHERE employee_name = '%s');", salary, name);
        try (Connection connection = dbConnection.getConnection();) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
