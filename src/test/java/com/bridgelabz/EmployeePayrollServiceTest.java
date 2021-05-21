package com.bridgelabz;

import com.bridgelabz.Exceptions.EmployeePayrollException;
import com.bridgelabz.Model.EmployeePayrollData;
import com.bridgelabz.Service.EmployeePayrollService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeePayrollServiceTest {
    @Test
   public void given3EmployeeWhenWrittenToFileShouldMatch() {
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
        employeePayrollService.updateEmployeeSalary("Meena",3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Meena");
        assertTrue(result);
    }
}
