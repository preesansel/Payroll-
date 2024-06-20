package com.payroll.service;

import com.payroll.dto.EmployeeDetailsDTO;
import com.payroll.dto.EmployeeSalaryDTO;
import com.payroll.dto.PayrollDetailsDTO;
import com.payroll.dto.TaxDTO;
import com.payroll.exceptions.EmployeeNotFoundException;
import com.payroll.exceptions.PayrollNotFoundException;
import com.payroll.exceptions.TaxServiceException;
import com.payroll.model.Payroll;
import com.payroll.repository.EmployeeRepository;
import com.payroll.repository.PayrollRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PayrollServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PayrollRepository payrollRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PayrollService payrollService;

    @Test
    public void testCalculatePayrolls() {
        // Given
        EmployeeSalaryDTO salaryDTO1 = new EmployeeSalaryDTO();
        salaryDTO1.setEmployeeId(1L);
        salaryDTO1.setDoj(2023-02-12);
        salaryDTO1.setBasicPay(60000.00);
        salaryDTO1.setHouseRentAllowance(12000.00);
        salaryDTO1.setSpecialAllowance(10000.00);
        salaryDTO1.setEpf(6000.00);

        EmployeeSalaryDTO salaryDTO2 = new EmployeeSalaryDTO();
        salaryDTO2.setEmployeeId(2L);
        salaryDTO2.setBasicPay(72000.00);
        salaryDTO2.setHouseRentAllowance(14400.00);
        salaryDTO2.setSpecialAllowance(12000.00);
        salaryDTO2.setEpf(7200.00);

        List<EmployeeSalaryDTO> employeeSalaries = Arrays.asList(salaryDTO1, salaryDTO2);

        TaxDTO taxDTO1 = new TaxDTO();
        taxDTO1.setEmployeeId(1L);
        taxDTO1.setIncomeTax(15000.00);
        taxDTO1.setProfessionalTax(500.00);

        TaxDTO taxDTO2 = new TaxDTO();
        taxDTO2.setEmployeeId(2L);
        taxDTO2.setIncomeTax(18000.00);
        taxDTO2.setProfessionalTax(600.00);

        Map<Long, TaxDTO> taxResponse = new HashMap<>();
        taxResponse.put(1L, taxDTO1);
        taxResponse.put(2L, taxDTO2);

        when(employeeRepository.findEmployeeSalaryDetails()).thenReturn(employeeSalaries);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(taxResponse));
        when(payrollRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

        // When
        List<Payroll> payrolls = payrollService.calculatePayrolls();

        // Then
        assertNotNull(payrolls);
        verify(employeeRepository, times(1)).findEmployeeSalaryDetails();
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class));
        verify(payrollRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testCalculatePayrolls_TaxServiceException() {
        // Given
        EmployeeSalaryDTO salaryDTO1 = new EmployeeSalaryDTO();
        salaryDTO1.setEmployeeId(1L);
        salaryDTO1.setEmployeeName("John Doe");
        salaryDTO1.setBasicPay(60000.00);
        salaryDTO1.setHouseRentAllowance(12000.00);
        salaryDTO1.setSpecialAllowance(10000.00);
        salaryDTO1.setEpf(6000.00);
        salaryDTO1.setIncomeTax(5000.00);
        salaryDTO1.setProfessionalTax(200.00);

        EmployeeSalaryDTO salaryDTO2 = new EmployeeSalaryDTO();
        salaryDTO2.setEmployeeId(2L);
        salaryDTO2.setEmployeeName("Jane Doe");
        salaryDTO2.setBasicPay(72000.00);
        salaryDTO2.setHouseRentAllowance(14400.00);
        salaryDTO2.setSpecialAllowance(12000.00);
        salaryDTO2.setEpf(7200.00);
        salaryDTO2.setIncomeTax(6000.00);
        salaryDTO2.setProfessionalTax(300.00);

        List<EmployeeSalaryDTO> employeeSalaries = Arrays.asList(salaryDTO1, salaryDTO2);

        when(employeeRepository.findEmployeeSalaryDetails()).thenReturn(employeeSalaries);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException());

        // Then
        assertThrows(TaxServiceException.class, () -> payrollService.calculatePayrolls());
    }

    @Test
    public void testGetPayrollDetails() {
        // Given
        Long employeeId = 1L;
        int year = 2023;
        int month = 6;
        Payroll payroll = new Payroll();
        payroll.setBasicPay(5000.00);
        payroll.setHouseRentAllowance(1000.00);
        payroll.setSpecialAllowance(800.00);
        payroll.setEpf(600.00);
        payroll.setIncomeTax(500.00);
        payroll.setProfessionalTax(200.00);
        payroll.setLop(2);

        when(payrollRepository.findPayrollByEmployeeIdAndMonthYear(employeeId, year, month)).thenReturn(payroll);
        when(payrollRepository.findYearToDateValues(eq(employeeId), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(new Object[]{30000.00, 6000.00, 4800.00, 3600.00, 3000.00, 1200.00}));

        // When
        PayrollDetailsDTO payrollDetailsDTO = payrollService.getPayrollDetails(employeeId, year, month);

        // Then
        assertNotNull(payrollDetailsDTO);
        assertEquals(5000.00, payrollDetailsDTO.getBasicPay());
        assertEquals(1000.00, payrollDetailsDTO.getHouseRentAllowance());
        assertEquals(800.00, payrollDetailsDTO.getSpecialAllowance());
        assertEquals(600.00, payrollDetailsDTO.getEpf());
        assertEquals(500.00, payrollDetailsDTO.getIncomeTax());
        assertEquals(200.00, payrollDetailsDTO.getProfessionalTax());
        assertEquals(2, payrollDetailsDTO.getLop());
        assertEquals(30000.00, payrollDetailsDTO.getYtdBasicPay());
        assertEquals(6000.00, payrollDetailsDTO.getYtdHouseRentAllowance());
        assertEquals(4800.00, payrollDetailsDTO.getYtdSpecialAllowance());
        assertEquals(3600.00, payrollDetailsDTO.getYtdEpf());
        assertEquals(3000.00, payrollDetailsDTO.getYtdIncomeTax());
        assertEquals(1200.00, payrollDetailsDTO.getYtdProfessionalTax());
        verify(payrollRepository, times(1)).findPayrollByEmployeeIdAndMonthYear(employeeId, year, month);
    }

    @Test
    public void testGetPayrollDetails_PayrollNotFoundException() {
        // Given
        Long employeeId = 1L;
        int year = 2023;
        int month = 6;

        when(payrollRepository.findPayrollByEmployeeIdAndMonthYear(employeeId, year, month)).thenReturn(null);

        // Then
        assertThrows(PayrollNotFoundException.class, () -> payrollService.getPayrollDetails(employeeId, year, month));
    }

    @Test
    public void testGetEmployeeDetails() {
        // Given
        Long employeeId = 1L;
        EmployeeDetailsDTO employeeDetailsDTO = new EmployeeDetailsDTO(
            employeeId, "John Doe", "Software Engineer", new Date(), "ABCDE1234F", 
            "Engineering", "New York", "123456789012", "PF1234567", "Bank of America", 
            "1234567890"
        );

        when(employeeRepository.findEmployeeDetailsById(employeeId)).thenReturn(employeeDetailsDTO);

        // When
        EmployeeDetailsDTO result = payrollService.getEmployeeDetails(employeeId);

        // Then
        assertNotNull(result);
        assertEquals(employeeDetailsDTO, result);
        verify(employeeRepository, times(1)).findEmployeeDetailsById(employeeId);
    }

    @Test
    public void testGetEmployeeDetails_EmployeeNotFoundException() {
        // Given
        Long employeeId = 1L;

        when(employeeRepository.findEmployeeDetailsById(employeeId)).thenReturn(null);

        // Then
        assertThrows(EmployeeNotFoundException.class, () -> payrollService.getEmployeeDetails(employeeId));
    }
}
