package com.bridgelabz.Service;

import com.bridgelabz.Exceptions.EmployeePayrollException;
import com.bridgelabz.Model.EmployeePayrollData;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {



    public enum IOService {CONSOLE_IO, FILE_IO, DB_IO, REST_IO}

    public List<EmployeePayrollData> employeePayrollList;
    private EmployeePayrollDBService employeePayrollDBService;

    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }


    public static void main(String[] args) {
        System.out.println("Welcome to Employee Payroll Problem Java IO");
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData(IOService.FILE_IO);
    }

    private void readEmployeePayrollData(Scanner consoleInputReader) {
        System.out.println("Enter Employee ID: ");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter  Employee Name: ");
        String name = consoleInputReader.next();
        System.out.println("Enter Employee Salary: ");
        double salary = consoleInputReader.nextDouble();
        employeePayrollList.add(new EmployeePayrollData(id, name, salary));
    }

    public void writeEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.CONSOLE_IO))
            System.out.println("\nWriting Employee Payroll Roaster to Console\n" + employeePayrollList);
        else if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().writeData(employeePayrollList);

    }

    public void printData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().printData();
    }

    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().countEntries();
        return 0;
    }

    public List<EmployeePayrollData> readData(IOService ioService) {
        if(ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().readData();
        else if(ioService.equals(IOService.DB_IO)) {
            employeePayrollList = employeePayrollDBService.readData();
            return employeePayrollList;
        }
        else
            return null;
    }
    public void updateEmployeeSalary(String name, double salary, EmployeePayrollDBService.StatementType type) throws EmployeePayrollException {
        int result = employeePayrollDBService.updateEmployeeData(name,salary,type);
        EmployeePayrollData employeePayrollData = null;
        if(result == 0)
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.UPDATE_FAIL, "Update Failed");

        else
            employeePayrollData = this.getEmployeePayrollData(name);
        if(employeePayrollData!=null) {
            employeePayrollData.salary = salary;
        }
    }

        private EmployeePayrollData getEmployeePayrollData(String name) {
        EmployeePayrollData employeePayrollData = this.employeePayrollList.stream()
                .filter(employee->employee.name.equals(name))
                .findFirst()
                .orElse(null);
        return employeePayrollData;
    }


    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> checkList = employeePayrollDBService.getEmployeePayrollData(name);
        return checkList.get(0).equals(getEmployeePayrollData(name));

    }
    public List<EmployeePayrollData> getEmployeesInDateRange(String start, String end) {
        return employeePayrollDBService.getEmployeesInGivenDateRangeDB(start,end);
    }

}



