package com.payroll.dto;

import java.util.Date;

public class EmployeeDetailsDTO {
	private Long employeeId;
	private String employeeName;
	private String designation;
	private Date doj;
	private String panId;
	private String department;
	private String location;
	private String uanNo;
	private String pfNo;
	private String bankName;
	private String accountNo;

	public Long getEmployeeId() {
		return employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public String getDesignation() {
		return designation;
	}

	public Date getDoj() {
		return doj;
	}

	public String getPanId() {
		return panId;
	}

	public String getDepartment() {
		return department;
	}

	public String getLocation() {
		return location;
	}

	public String getUanNo() {
		return uanNo;
	}

	public String getPfNo() {
		return pfNo;
	}

	public String getBankName() {
		return bankName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public EmployeeDetailsDTO(Long employeeId, String employeeName, String designation, Date doj, String panId,
			String department, String location, String uanNo, String pfNo, String bankName, String accountNo) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.designation = designation;
		this.doj = doj;
		this.panId = panId;
		this.department = department;
		this.location = location;
		this.uanNo = uanNo;
		this.pfNo = pfNo;
		this.bankName = bankName;
		this.accountNo = accountNo;
	}

}
