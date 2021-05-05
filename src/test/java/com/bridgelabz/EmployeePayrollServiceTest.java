package com.bridgelabz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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

    }
}
