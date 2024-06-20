
package com.payroll.controller;

import com.payroll.dto.EmployeeDetailsDTO;
import com.payroll.dto.PayrollDetailsDTO;
import com.payroll.model.Payroll;
import com.payroll.service.PayrollService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/payroll")
public class PayrollController {

	private PayrollService payrollService;

	public PayrollController(PayrollService payrollService) {
		this.payrollService = payrollService;
	}

	@GetMapping("/calculate")
	public ResponseEntity<List<Payroll>> calculatePayrolls() {
		List<Payroll> payrolls = payrollService.calculatePayrolls();
		return ResponseEntity.ok(payrolls);
	}

	@GetMapping("/details")
	public ResponseEntity<PayrollDetailsDTO> getPayrollDetails(@RequestParam Long employeeId, @RequestParam int year,
			@RequestParam int month) {
		PayrollDetailsDTO payrollDetails = payrollService.getPayrollDetails(employeeId, year, month);
		return ResponseEntity.ok(payrollDetails);
	}

	@GetMapping("/employeeDetails/{employeeId}")
	public ResponseEntity<EmployeeDetailsDTO> getEmployeeDetails(@PathVariable Long employeeId) {
		EmployeeDetailsDTO employeeDTO = payrollService.getEmployeeDetails(employeeId);
		return ResponseEntity.ok(employeeDTO);
	}
}
