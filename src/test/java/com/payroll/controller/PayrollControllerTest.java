package com.payroll.controller;

import com.payroll.dto.EmployeeDetailsDTO;
import com.payroll.dto.PayrollDetailsDTO;
import com.payroll.model.Payroll;
import com.payroll.service.PayrollService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class PayrollControllerTest {

    @Mock
    private PayrollService payrollService;

    @InjectMocks
    private PayrollController payrollController;

    @Test
    void testCalculatePayrolls() {
        // Given
        Payroll payroll1 = new Payroll();
        Payroll payroll2 = new Payroll();
        List<Payroll> payrolls = Arrays.asList(payroll1, payroll2);

        // When
        when(payrollService.calculatePayrolls()).thenReturn(payrolls);

        // Then
        ResponseEntity<List<Payroll>> response = payrollController.calculatePayrolls();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(payrolls, response.getBody());
    }

    @Test
     void testGetPayrollDetails() {
        // Given
        Long employeeId = 1L;
        int year = 2023;
        int month = 6;
        PayrollDetailsDTO payrollDetailsDTO = new PayrollDetailsDTO(
            10000.00, 2000.00, 1500.00, 1200.00, 500.00, 200.00, 2, 
            13500.00, 1900.00, 11600.00, 60000.00, 12000.00, 9000.00, 
            7200.00, 3000.00, 1200.00, 81000.00, 11300.00
        );

        // When
        when(payrollService.getPayrollDetails(employeeId, year, month)).thenReturn(payrollDetailsDTO);

        // Then
        ResponseEntity<PayrollDetailsDTO> response = payrollController.getPayrollDetails(employeeId, year, month);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(payrollDetailsDTO, response.getBody());
    }

    @Test
     void testGetEmployeeDetails() {
        // Given
        Long employeeId = 1L;
        EmployeeDetailsDTO employeeDetailsDTO = new EmployeeDetailsDTO(
            employeeId, "Anu", "Software Engineer", new Date(), "ABCDE1234F", 
            "Engineering", "Chennai", "123456789012", "PF1234567", "Bank of Baroda", 
            "1234567890"
        );

        // When
        when(payrollService.getEmployeeDetails(employeeId)).thenReturn(employeeDetailsDTO);

        // Then
        ResponseEntity<EmployeeDetailsDTO> response = payrollController.getEmployeeDetails(employeeId);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(employeeDetailsDTO, response.getBody());
    }
}
