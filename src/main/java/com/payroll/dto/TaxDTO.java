package com.payroll.dto;

public class TaxDTO {
    private Double incomeTax;
    private Double professionalTax;
	public Double getIncomeTax() {
		return incomeTax;
	}

	public Double getProfessionalTax() {
		return professionalTax;
	}

	public TaxDTO(Double incomeTax, Double professionalTax) {
		super();
		this.incomeTax = incomeTax;
		this.professionalTax = professionalTax;
	}

    
}
