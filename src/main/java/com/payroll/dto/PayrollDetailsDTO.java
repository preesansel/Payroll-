package com.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PayrollDetailsDTO{
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

}
