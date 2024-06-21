package com.payroll.dto;

public class PayrollDetailsDTO {
	private double basicPay;
	private double houseRentAllowance;
	private double specialAllowance;
	private double epf;
	private double incomeTax;
	private double professionalTax;
	private int lop;

	private double grossEarning;
	private double grossDeduction;
	private double netPay;

	private double ytdBasicPay;
	private double ytdHouseRentAllowance;
	private double ytdSpecialAllowance;
	private double ytdEpf;
	private double ytdIncomeTax;
	private double ytdProfessionalTax;
	private double ytdGrossEarnings;
	private double ytdGrossDeductions;

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

	public double getIncomeTax() {
		return incomeTax;
	}

	public double getProfessionalTax() {
		return professionalTax;
	}

	public int getLop() {
		return lop;
	}

	public double getGrossEarning() {
		return grossEarning;
	}

	public double getGrossDeduction() {
		return grossDeduction;
	}

	public double getNetPay() {
		return netPay;
	}

	public double getYtdBasicPay() {
		return ytdBasicPay;
	}

	public double getYtdHouseRentAllowance() {
		return ytdHouseRentAllowance;
	}

	public double getYtdSpecialAllowance() {
		return ytdSpecialAllowance;
	}

	public double getYtdEpf() {
		return ytdEpf;
	}

	public double getYtdIncomeTax() {
		return ytdIncomeTax;
	}

	public double getYtdProfessionalTax() {
		return ytdProfessionalTax;
	}

	public double getYtdGrossEarnings() {
		return ytdGrossEarnings;
	}

	public double getYtdGrossDeductions() {
		return ytdGrossDeductions;
	}

	public PayrollDetailsDTO(double basicPay, double houseRentAllowance, double specialAllowance, double epf,
			double incomeTax, double professionalTax, int lop, double grossEarning, double grossDeduction,
			double netPay, double ytdBasicPay, double ytdHouseRentAllowance, double ytdSpecialAllowance, double ytdEpf,
			double ytdIncomeTax, double ytdProfessionalTax, double ytdGrossEarnings, double ytdGrossDeductions) {
		super();
		this.basicPay = basicPay;
		this.houseRentAllowance = houseRentAllowance;
		this.specialAllowance = specialAllowance;
		this.epf = epf;
		this.incomeTax = incomeTax;
		this.professionalTax = professionalTax;
		this.lop = lop;
		this.grossEarning = grossEarning;
		this.grossDeduction = grossDeduction;
		this.netPay = netPay;
		this.ytdBasicPay = ytdBasicPay;
		this.ytdHouseRentAllowance = ytdHouseRentAllowance;
		this.ytdSpecialAllowance = ytdSpecialAllowance;
		this.ytdEpf = ytdEpf;
		this.ytdIncomeTax = ytdIncomeTax;
		this.ytdProfessionalTax = ytdProfessionalTax;
		this.ytdGrossEarnings = ytdGrossEarnings;
		this.ytdGrossDeductions = ytdGrossDeductions;
	}

}
