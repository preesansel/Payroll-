package com.payroll.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salary {

	@Id
	@Column(name = "employee_id", nullable = false)
	private Long employeeId;

	@Column(name = "basic_pay", nullable = false)
	private double basicPay;

	@Column(name = "house_rent_allowance", nullable = false)
	private double houseRentAllowance;

	@Column(name = "special_allowance", nullable = false)
	private double specialAllowance;

	@Column(name = "epf", nullable = false)
	private double epf;

	@Column(name = "gross_salary", nullable = false)
	private double grossSalary;

	@Column(name = "gratuity", nullable = true)
	private double gratuity;

	@Column(name = "pay_band", nullable = true)
	private String payBand;
}
