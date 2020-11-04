package Employee_payroll_project;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayrollData {
	public int employeeId;
	public String employeeName;
	public double employeeSalary;
	public LocalDate start;
	public String gender;

	public EmployeePayrollData(int employeeId, String employeeName, double employeeSalary) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.employeeSalary = employeeSalary;
	}

	public EmployeePayrollData(int employeeId, String employeeName, double employeeSalary, LocalDate start) {
		this(employeeId, employeeName, employeeSalary);
		this.start = start;
	}

	public EmployeePayrollData(int employeeId, String employeeName, String gender, double employeeSalary,
			LocalDate start) {
		this(employeeId, employeeName, employeeSalary, start);
		this.gender = gender;
	}

	@Override
	public String toString() {
		return (employeeId + " " + employeeName + " " + employeeSalary + " " + start);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		EmployeePayrollData that = (EmployeePayrollData) obj;
		return employeeId == that.employeeId && Double.compare(that.employeeSalary, employeeSalary) == 0
				&& employeeName.equals(that.employeeName);
	}

	public int hashCode() {
		return Objects.hash(employeeName, gender, employeeSalary, start);
	}
}
