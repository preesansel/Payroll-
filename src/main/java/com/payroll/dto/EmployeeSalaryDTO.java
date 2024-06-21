package com.payroll.dto;

import java.sql.Date;

public class EmployeeSalaryDTO {
	private Long employeeId;
	private Date doj;
	private double basicPay;
	private double houseRentAllowance;
	private double specialAllowance;
	private double epf;

	public Long getEmployeeId() {
		return employeeId;
	}

	public Date getDoj() {
		return doj;
	}

	public double getBasicPay() {
		return basicPay;
	}

	public double getHouseRentAllowance() {
		return houseRentAllowance;
	}

	public double getSpecialAllowance() {
		return specialAllowance;
	}

	public double getEpf() {
		return epf;
	}

	public EmployeeSalaryDTO(Long employeeId, Date doj, double basicPay, double houseRentAllowance,
			double specialAllowance, double epf) {
		super();
		this.employeeId = employeeId;
		this.doj = doj;
		this.basicPay = basicPay;
		this.houseRentAllowance = houseRentAllowance;
		this.specialAllowance = specialAllowance;
		this.epf = epf;
	}

}
