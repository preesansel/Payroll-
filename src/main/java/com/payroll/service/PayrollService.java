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

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class PayrollService {

	private EmployeeRepository employeeRepository;
	private PayrollRepository payrollRepository;
	private RestTemplate restTemplate;
	private static Random random = new Random();

	public PayrollService(EmployeeRepository employeeRepository, PayrollRepository payrollRepository,
			RestTemplateBuilder restTemplateBuilder) {
		this.employeeRepository = employeeRepository;
		this.payrollRepository = payrollRepository;
		this.restTemplate = restTemplateBuilder.build();
	}

	protected static void setRandom(Random random) {
		PayrollService.random = random;
	}

	@Scheduled(cron = "0 0 0 L * ?")

	public List<Payroll> calculatePayrolls() {
		List<EmployeeSalaryDTO> employeeSalaries = employeeRepository.findEmployeeSalaryDetails();

		String taxServiceUrl = "http://localhost:8883/tax";
		Map<Long, TaxDTO> taxResponse;

		
		try {
			taxResponse = restTemplate
					.exchange(taxServiceUrl, HttpMethod.GET, null, new ParameterizedTypeReference<Map<Long, TaxDTO>>() {
					}).getBody();
		} catch (Exception e) {
			throw new TaxServiceException("Failed to fetch tax details from tax service.");
		}

		List<Payroll> payrolls = employeeSalaries.stream().map(employeeSalary -> {
			TaxDTO taxes = taxResponse.get(employeeSalary.getEmployeeId());

			if (taxes == null) {
				log.error("Taxes not found for {}", employeeSalary.getEmployeeId());
				return null;
			}

			Payroll payroll = new Payroll();
			payroll.setEmployeeId(employeeSalary.getEmployeeId());

			double basicPay = employeeSalary.getBasicPay() / 12;
			double houseRentAllowance = employeeSalary.getHouseRentAllowance() / 12;
			double specialAllowance = employeeSalary.getSpecialAllowance() / 12;
			double epf = employeeSalary.getEpf() / 12;

			YearMonth currentYearMonth = YearMonth.now();
			int currentMonthDayCount = currentYearMonth.lengthOfMonth();
			int workDays = currentMonthDayCount;

			LocalDate doj = employeeSalary.getDoj().toLocalDate();
			if (doj.getYear() == currentYearMonth.getYear() && doj.getMonth() == currentYearMonth.getMonth()) {
				int startingDate = doj.getDayOfMonth();
				workDays -= startingDate;
			}

			int lop = random.nextInt(10);
			workDays -= lop;

			basicPay = basicPay * workDays / currentMonthDayCount;
			houseRentAllowance = houseRentAllowance * workDays / currentMonthDayCount;
			specialAllowance = specialAllowance * workDays / currentMonthDayCount;
			epf = epf * workDays / currentMonthDayCount;

			payroll.setBasicPay(basicPay);
			payroll.setHouseRentAllowance(houseRentAllowance);
			payroll.setSpecialAllowance(specialAllowance);
			payroll.setEpf(epf);
			payroll.setIncomeTax(taxes.getIncomeTax());
			payroll.setProfessionalTax(taxes.getProfessionalTax());
			payroll.setLop(lop);

			return payroll;
		}).toList(); // Replaced Stream.collect(Collectors.toList()) with Stream.toList()

		payrolls = payrolls.stream().filter(p -> p != null).toList(); // Replaced Stream.collect(Collectors.toList())
																		// with Stream.toList()
		payrollRepository.saveAll(payrolls);

		return payrolls;
	}

	public PayrollDetailsDTO getPayrollDetails(Long employeeId, int year, int month) {
		LocalDate startDate;
		LocalDate endDate;
		
		if (month >= 4) {
			startDate = LocalDate.of(year, 4, 1);
			endDate = LocalDate.of(year, month, LocalDate.of(year, month, 1).lengthOfMonth());
		} else {
			startDate = LocalDate.of(year - 1, 4, 1);
			endDate = LocalDate.of(year, month, LocalDate.of(year, month, 1).lengthOfMonth());
		}
		
		
		List<Object[]> ytdResults = payrollRepository.findYearToDateValues(employeeId, startDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		if (ytdResults.isEmpty()) {
			throw new PayrollNotFoundException("No payroll found for the specified employee, month, and year");
		}

		Object[] ytdTotals = ytdResults.get(0);

		double ytdBasicPay = ytdTotals[0] != null ? (double) ytdTotals[0] : 0;
		double ytdHouseRentAllowance = ytdTotals[1] != null ? (double) ytdTotals[1] : 0;
		double ytdSpecialAllowance = ytdTotals[2] != null ? (double) ytdTotals[2] : 0;
		double ytdEpf = ytdTotals[3] != null ? (double) ytdTotals[3] : 0;
		double ytdIncomeTax = ytdTotals[4] != null ? (double) ytdTotals[4] : 0;
		double ytdProfessionalTax = ytdTotals[5] != null ? (double) ytdTotals[5] : 0;

		double ytdGrossEarnings = ytdBasicPay + ytdHouseRentAllowance + ytdSpecialAllowance;
		double ytdGrossDeductions = ytdEpf + ytdIncomeTax + ytdProfessionalTax;

		Payroll payroll = payrollRepository.findPayrollByEmployeeIdAndMonthYear(employeeId, year, month);
		if (payroll == null) {
			throw new PayrollNotFoundException("No payroll found for the specified employee, month, and year");
		}

		double basicPay = payroll.getBasicPay();
		double houseRentAllowance = payroll.getHouseRentAllowance();
		double specialAllowance = payroll.getSpecialAllowance();
		double epf = payroll.getEpf();
		double incomeTax = payroll.getIncomeTax();
		double professionalTax = payroll.getProfessionalTax();
		int lop = payroll.getLop();

		double grossEarning = basicPay + houseRentAllowance + specialAllowance;
		double grossDeduction = epf + incomeTax + professionalTax;
		double netPay = grossEarning - grossDeduction;

		return new PayrollDetailsDTO(basicPay, houseRentAllowance, specialAllowance, epf, incomeTax, professionalTax,
				lop, grossEarning, grossDeduction, netPay, ytdBasicPay, ytdHouseRentAllowance, ytdSpecialAllowance,
				ytdEpf, ytdIncomeTax, ytdProfessionalTax, ytdGrossEarnings, ytdGrossDeductions);
	}

	public EmployeeDetailsDTO getEmployeeDetails(Long employeeId) {
		EmployeeDetailsDTO employeeDetails = employeeRepository.findEmployeeDetailsById(employeeId);
		if (employeeDetails == null) {
			throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found.");
		}
		return employeeDetails;
	}
}
