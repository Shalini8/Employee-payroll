package com.bridgelabz;

import com.bridgelabz.Exceptions.EmployeePayrollException;
import com.bridgelabz.Model.EmployeePayrollData;
import com.bridgelabz.Service.EmployeePayrollDBService;
import com.bridgelabz.Service.EmployeePayrollService;
import org.junit.jupiter.api.Test;

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
        employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
        employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
         assertEquals(3,entries);
    }
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO);
        assertEquals(5, employeePayrollData.size());
    }
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDatabase() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Meena",3000000.00, EmployeePayrollDBService.StatementType.STATEMENT);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Meena");
        assertTrue(result);
    }
    @Test
    public void givenNewSalaryForEmployee_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDatabase() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Meena",3000000.00, EmployeePayrollDBService.StatementType.PREPARED_STATEMENT);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Meena");
        assertTrue(result);
    }
    @Test
    public void givenDateRangeForEmployee_WhenRetrievedUsingStatement_ShouldReturnProperData() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO);
        List<EmployeePayrollData> employeeDataInGivenDateRange = employeePayrollService.getEmployeesInDateRange("2020-03-04","2021-05-19");
        assertEquals(5, employeeDataInGivenDateRange.size());
    }
    @Test
    public void givenPayrollData_WhenAverageSalaryRetrievedByGender_ShouldReturnProperValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO);
        Map<String,Double> averageSalaryByGender  = employeePayrollService.readAverageSalaryByGender(EmployeePayrollService.IOService.DB_IO);
        System.out.println(averageSalaryByGender);
        assertTrue(averageSalaryByGender.get("M").equals( 32250.0000)&&
                averageSalaryByGender.get("F").equals( 1416666.6667));
    }
    @Test
    public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.addEmployeeToPayroll("Zoya",50000000.00, LocalDate.now(),"F");
        boolean result =  employeePayrollService.checkEmployeePayrollInSyncWithDB("Zoya");
        assertTrue(result);
    }


}
