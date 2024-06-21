
package com.payroll.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payroll")

public class Payroll {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pay_id")
	private Long payId;

	@Column(name = "employee_id", nullable = false)
	private Long employeeId;

	@Column(name = "pay_date", nullable = false)
	private Instant payDate = Instant.now();

	@Column(name = "basic_pay", nullable = false)
	private Double basicPay;

	@Column(name = "house_rent_allowance", nullable = false)
	private Double houseRentAllowance;

	@Column(name = "special_allowance", nullable = false)
	private Double specialAllowance;

	@Column(name = "epf", nullable = false)
	private Double epf;

	@Column(name = "income_tax", nullable = false)
	private Double incomeTax;

	@Column(name = "professional_tax", nullable = false)
	private Double professionalTax;

	@Column(name = "lop", nullable = false)
	private int lop;

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public int getLop() {
		return lop;
	}

	public void setLop(int lop) {
		this.lop = lop;
	}

	public Double getBasicPay() {
		return basicPay;
	}

	public Double getHouseRentAllowance() {
		return houseRentAllowance;
	}

	public Double getSpecialAllowance() {
		return specialAllowance;
	}

	public Double getEpf() {
		return epf;
	}

	public Double getIncomeTax() {
		return incomeTax;
	}

	public Double getProfessionalTax() {
		return professionalTax;
	}

	public Payroll(Long payId, Long employeeId, Instant payDate, Double basicPay, Double houseRentAllowance,
			Double specialAllowance, Double epf, Double incomeTax, Double professionalTax, int lop) {
		super();
		this.payId = payId;
		this.employeeId = employeeId;
		this.payDate = payDate;
		this.basicPay = basicPay;
		this.houseRentAllowance = houseRentAllowance;
		this.specialAllowance = specialAllowance;
		this.epf = epf;
		this.incomeTax = incomeTax;
		this.professionalTax = professionalTax;
		this.lop = lop;
	}

	public Payroll() {
		super();
	}

	// Utility method to round to two decimal places
	private Double round(Double value) {
		return BigDecimal.valueOf(value).setScale(0, RoundingMode.HALF_UP).doubleValue();
	}

	public void setBasicPay(Double basicPay) {
		this.basicPay = round(basicPay);
	}

	public void setHouseRentAllowance(Double houseRentAllowance) {
		this.houseRentAllowance = round(houseRentAllowance);
	}

	public void setSpecialAllowance(Double specialAllowance) {
		this.specialAllowance = round(specialAllowance);
	}

	public void setEpf(Double epf) {
		this.epf = round(epf);
	}

	public void setIncomeTax(Double incomeTax) {
		this.incomeTax = round(incomeTax);
	}

	public void setProfessionalTax(Double professionalTax) {
		this.professionalTax = round(professionalTax);
	}

}
