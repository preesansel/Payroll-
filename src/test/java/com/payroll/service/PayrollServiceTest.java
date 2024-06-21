package com.payroll.service;


import com.payroll.dto.EmployeeDetailsDTO;
import com.payroll.dto.EmployeeSalaryDTO;
import com.payroll.dto.PayrollDetailsDTO;
import com.payroll.dto.TaxDTO;
import com.payroll.exceptions.EmployeeNotFoundException;
import com.payroll.exceptions.PayrollNotFoundException;
import com.payroll.model.Payroll;
import com.payroll.repository.EmployeeRepository;
import com.payroll.repository.PayrollRepository;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class PayrollServiceTest {

	@Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PayrollRepository payrollRepository;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Random mockRandom;
    
  
    
    @InjectMocks
    private PayrollService payrollService;
    

   

    @BeforeEach
 void setup() {
       // when(mockRandom.nextInt(anyInt())).thenReturn(5);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        payrollService = new PayrollService(employeeRepository, payrollRepository, restTemplateBuilder);
        PayrollService.setRandom(mockRandom);
    }

    @Test
   void shouldBeAbleToCalculatePayrolls() {
        List<EmployeeSalaryDTO> employeeSalaryDTOs = List.of(
                new EmployeeSalaryDTO(1L, java.sql.Date.valueOf("2024-06-12"), 60000.0,
                        12000.0, 10000.0, 6000.0),
                new EmployeeSalaryDTO(2L, java.sql.Date.valueOf("2024-06-12"), 72000.0,
                        14400.0, 12000.0, 7200.0),
                new EmployeeSalaryDTO(2L, java.sql.Date.valueOf("2024-03-12"), 72000.0,
                        14400.0, 12000.0, 7200.0)
        );
        Map<Long, TaxDTO> taxResponse = Map.of(
                1L, new TaxDTO(15000.0, 500.0),
                2L, new TaxDTO(15000.0, 500.0)
        );

        when(employeeRepository.findEmployeeSalaryDetails()).thenReturn(employeeSalaryDTOs);
        when(restTemplate.exchange("http://localhost:8883/tax", HttpMethod.GET, null, new ParameterizedTypeReference<Map<Long, TaxDTO>>() {
        })).thenReturn(ResponseEntity.of(Optional.of(taxResponse)));

        List<Payroll> expectedPayrolls = List.of(
                new Payroll(null, 1L, Instant.now(), 4167.0, 833.0, 694.0, 417.0, 15000.0, 500.0, 5),
                new Payroll(null, 2L, Instant.now(), 5000.0, 1000.0, 833.0, 500.0, 15000.0, 500.0, 5)
        );


//        when(payrollRepository.saveAll(any(expectedPayrolls.getClass))).thenReturn(expectedPayrolls);



        List<Payroll> payrolls = payrollService.calculatePayrolls();
        System.out.println(payrolls.get(0).getBasicPay());

        assertNotNull(payrolls);
    }

    @BeforeEach
     void setup1() {
        payrollService = new PayrollService(employeeRepository, payrollRepository, restTemplateBuilder);
    }

    @Test
     void testGetEmployeeDetails_Found() {
    	EmployeeDetailsDTO employeeDetailsDTO = new EmployeeDetailsDTO(
    			1L,"Anu","SDE",java.sql.Date.valueOf("2023-01-01"),"ABCDE1234F","IT","Chennai","123456789012","PF123456","Bank of Baroda","1234567890");
      
      
        
        Long employeeId = 1L;
        when(employeeRepository.findEmployeeDetailsById(employeeId)).thenReturn(employeeDetailsDTO);

        // When
        EmployeeDetailsDTO result = payrollService.getEmployeeDetails(employeeId);

        // Then
        assertNotNull(result);
        assertEquals(employeeDetailsDTO.getEmployeeId(), result.getEmployeeId());
        assertEquals(employeeDetailsDTO.getEmployeeName(), result.getEmployeeName());
        assertEquals(employeeDetailsDTO.getDesignation(), result.getDesignation());
        assertEquals(employeeDetailsDTO.getDoj(), result.getDoj());
        assertEquals(employeeDetailsDTO.getPanId(), result.getPanId());
        assertEquals(employeeDetailsDTO.getDepartment(), result.getDepartment());
        assertEquals(employeeDetailsDTO.getLocation(), result.getLocation());
        assertEquals(employeeDetailsDTO.getUanNo(), result.getUanNo());
        assertEquals(employeeDetailsDTO.getPfNo(), result.getPfNo());
        assertEquals(employeeDetailsDTO.getBankName(), result.getBankName());
        assertEquals(employeeDetailsDTO.getAccountNo(), result.getAccountNo());
        
        verify(employeeRepository, times(1)).findEmployeeDetailsById(employeeId);
    }

    @Test
     void testGetEmployeeDetails_NotFound() {
        // Given
        Long employeeId = 2L;
        when(employeeRepository.findEmployeeDetailsById(employeeId)).thenReturn(null);

        // Then
        Exception exception = assertThrows(EmployeeNotFoundException.class, () -> {
            payrollService.getEmployeeDetails(employeeId);
        });

        String expectedMessage = "Employee with ID " + employeeId + " not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(employeeRepository, times(1)).findEmployeeDetailsById(employeeId);
    }
    
    
    @Test
     void testGetPayrollDetails() {
        long employeeId = 1L;
        int year = 2024;
        int month = 6;

        Object[] ytdResult = new Object[]{1000.0, 200.0, 300.0, 50.0, 20.0, 10.0};
        List<Object[]> ytdResults = Collections.singletonList(ytdResult);
        Payroll payroll = new Payroll(
        		 1L,
        		 employeeId,
        		 Instant.now(),
                 1000.0,
                 200.0,
                 300.0,
                 50.0,
                 20.0,
                 10.0,
                 1);

        when(payrollRepository.findYearToDateValues(any(long.class), any(Instant.class), any(Instant.class)))
            .thenReturn(ytdResults);
        when(payrollRepository.findPayrollByEmployeeIdAndMonthYear(any(long.class),any(int.class),any(int.class)))
            .thenReturn(payroll);

        PayrollDetailsDTO details = payrollService.getPayrollDetails(employeeId, year, month);

        assertEquals(1000.0, details.getBasicPay(), 0.01);
        assertEquals(200.0, details.getHouseRentAllowance(), 0.01);
        assertEquals(300.0, details.getSpecialAllowance(), 0.01);
        assertEquals(50.0, details.getEpf(), 0.01);
        assertEquals(20.0, details.getIncomeTax(), 0.01);
        assertEquals(10.0, details.getProfessionalTax(), 0.01);
        assertEquals(1, details.getLop());

        assertEquals(1500.0, details.getGrossEarning(), 0.01);
        assertEquals(80.0, details.getGrossDeduction(), 0.01);
        assertEquals(1420.0, details.getNetPay(), 0.01);

        assertEquals(1000.0, details.getYtdBasicPay(), 0.01);
        assertEquals(200.0, details.getYtdHouseRentAllowance(), 0.01);
        assertEquals(300.0, details.getYtdSpecialAllowance(), 0.01);
        assertEquals(50.0, details.getYtdEpf(), 0.01);
        assertEquals(20.0, details.getYtdIncomeTax(), 0.01);
        assertEquals(10.0, details.getYtdProfessionalTax(), 0.01);
        assertEquals(1500.0, details.getYtdGrossEarnings(), 0.01);
        assertEquals(80.0, details.getYtdGrossDeductions(), 0.01);

//        verify(payrollRepository).findYearToDateValues(employeeId, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 5, 31));
//        verify(payrollRepository).findPayrollByEmployeeIdAndMonthYear(employeeId, year, month);
    }


    
    @Test
     void testGetPayrollDetails_NoPayroll() {
        Long employeeId = 1L;
        int year = 2023;
        int month = 5;

        Object[] ytdResult = new Object[]{1000.0, 200.0, 300.0, 50.0, 20.0, 10.0};
        List<Object[]> ytdResults = Collections.singletonList(ytdResult);

        when(payrollRepository.findYearToDateValues(employeeId, LocalDate.of(2023, 4, 1).atStartOfDay(ZoneId.systemDefault()).toInstant(), LocalDate.of(2023, 5, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()))
            .thenReturn(ytdResults);
        when(payrollRepository.findPayrollByEmployeeIdAndMonthYear(employeeId, year, month))
            .thenReturn(null);

        PayrollNotFoundException exception = assertThrows(PayrollNotFoundException.class, () -> {
            payrollService.getPayrollDetails(employeeId, year, month);
        });

        assertEquals("No payroll found for the specified employee, month, and year", exception.getMessage());
    }
    
  
    
    @Test
     void testGetPayrollDetails_NoYtdResults() {
        Long employeeId = 1L;
        int year = 2023;
        int month = 5;

        when(payrollRepository.findYearToDateValues(employeeId, LocalDate.of(2023, 4, 1).atStartOfDay(ZoneId.systemDefault()).toInstant(), LocalDate.of(2023, 5, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()))
            .thenReturn(Collections.emptyList());

        PayrollNotFoundException exception = assertThrows(PayrollNotFoundException.class, () -> {
            payrollService.getPayrollDetails(employeeId, year, month);
        });

        assertEquals("No payroll found for the specified employee, month, and year", exception.getMessage());
    }
    
}
