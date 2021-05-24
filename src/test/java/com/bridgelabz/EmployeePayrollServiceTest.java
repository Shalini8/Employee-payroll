package com.bridgelabz;

import com.bridgelabz.Exceptions.EmployeePayrollException;
import com.bridgelabz.Model.EmployeePayrollData;
import com.bridgelabz.Service.EmployeePayrollDBService;
import com.bridgelabz.Service.EmployeePayrollService;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeePayrollServiceTest {
    @Test
   public void given3EmployeeWhenWrittenToFileShouldMatchEmployeeEntries() {
        EmployeePayrollData[] arrayofEmps = {
                new EmployeePayrollData(1,"shalini",1000000.0),
                new EmployeePayrollData(2,"shyam",8000000.0),
                new EmployeePayrollData(3,"shatanu",6000000.0)

        };
        EmployeePayrollService employeePayrollService;
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayofEmps));
        employeePayrollService.writeEmployeeData(EmployeePayrollService.IOService.FILE_IO);
        employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
         assertEquals(3,entries);
    }
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO, EmployeePayrollService.NormalisationType.DENORMALISED);
        assertEquals(5, employeePayrollData.size());
    }
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDatabase() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO, EmployeePayrollService.NormalisationType.DENORMALISED);
        employeePayrollService.updateEmployeeSalary("Meena",3000000.00, EmployeePayrollDBService.StatementType.STATEMENT, EmployeePayrollService.NormalisationType.DENORMALISED);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Meena", EmployeePayrollService.NormalisationType.DENORMALISED);
        assertTrue(result);
    }
    @Test
    public void givenNewSalaryForEmployee_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDatabase() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO, EmployeePayrollService.NormalisationType.DENORMALISED);
        employeePayrollService.updateEmployeeSalary("Meena",3000000.00, EmployeePayrollDBService.StatementType.PREPARED_STATEMENT, EmployeePayrollService.NormalisationType.DENORMALISED);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Meena", EmployeePayrollService.NormalisationType.DENORMALISED);
        assertTrue(result);
    }
    @Test
    public void givenDateRangeForEmployee_WhenRetrievedUsingStatement_ShouldReturnProperData() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO, EmployeePayrollService.NormalisationType.DENORMALISED);
        List<EmployeePayrollData> employeeDataInGivenDateRange = employeePayrollService.getEmployeesInDateRange("2020-03-04","2021-05-19");
        assertEquals(5, employeeDataInGivenDateRange.size());
    }
    @Test
    public void givenPayrollData_WhenAverageSalaryRetrievedByGender_ShouldReturnProperValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO, EmployeePayrollService.NormalisationType.DENORMALISED);
        Map<String,Double> averageSalaryByGender  = employeePayrollService.readAverageSalaryByGender(EmployeePayrollService.IOService.DB_IO);
        System.out.println(averageSalaryByGender);
        assertTrue(averageSalaryByGender.get("M").equals( 32250.0000)&&
                averageSalaryByGender.get("F").equals( 1416666.6667));
    }
    @Test
    public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO, EmployeePayrollService.NormalisationType.DENORMALISED);
        employeePayrollService.addEmployeeToPayroll("Zoya",50000000.00, LocalDate.now(),"F");
        boolean result =  employeePayrollService.checkEmployeePayrollInSyncWithDB("Zoya", EmployeePayrollService.NormalisationType.DENORMALISED);
        assertTrue(result);
    }
    @Test
    public void given6Employees_WhenAddedToDB_ShouldMatchEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmps = {
                new EmployeePayrollData(0,"Jeff Bezos","M",100000.0,LocalDate.now()),
                new EmployeePayrollData(0,"Bill Gates","M",200000.0,LocalDate.now()),
                new EmployeePayrollData(0,"Mark Zuckerberg","M",300000.0,LocalDate.now()),
                new EmployeePayrollData(0,"Sunder","M",600000.0,LocalDate.now()),
                new EmployeePayrollData(0,"Mukesh","M",100000.0,LocalDate.now()),
                new EmployeePayrollData(0,"Anil","M",200000.0,LocalDate.now())
        };
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO, EmployeePayrollService.NormalisationType.DENORMALISED);
        Instant start = Instant.now();
        employeePayrollService.addEmployeesToPayroll(Arrays.asList(arrayOfEmps));
        Instant end = Instant.now();
        Instant threadStart = Instant.now();
        employeePayrollService.addEmployeesToPayrollWithThreads(Arrays.asList(arrayOfEmps));
        Instant threadEnd = Instant.now();
        System.out.println("Duration with thread: "+ Duration.between(threadStart, threadEnd));
        System.out.println("Duration without thread: "+Duration.between(start, end));
        employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO, EmployeePayrollService.NormalisationType.DENORMALISED);
        assertEquals(11, employeePayrollService.countEntries(EmployeePayrollService.IOService.DB_IO));
    }
    @Test
    public void givenEmployeePayrollInNormalisedDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO, EmployeePayrollService.NormalisationType.NORMALISED);
        System.out.println(employeePayrollData);
        for(EmployeePayrollData emp : employeePayrollData ) {
            emp.printDepartments();
        }
        assertEquals(3, employeePayrollData.size());
    }
}
