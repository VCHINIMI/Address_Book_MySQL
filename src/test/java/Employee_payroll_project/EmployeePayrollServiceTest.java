package Employee_payroll_project;

import static org.junit.Assert.*;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import Employee_payroll_project.EmployeePayrollService.IOService;

public class EmployeePayrollServiceTest {

	@Test
	public void givenEmployeePayrollInDB_ShouldMatchEmployeeCount() throws EmployeeException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		assertEquals(3, employeePayrollData.size());
	}
	
	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatch() throws EmployeeException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.updateEmployeeSalary("Terisa", 3000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
		assertTrue(result);
	}
	
	@Test
	public void givenStarAndEndDates_ShouldReturnEmployeeJoinedBetweenDates() throws EmployeeException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollDataByDate(IOService.DB_IO, "2016-01-01","2020-01-01");
		System.out.println(employeePayrollData);
		assertEquals(2, employeePayrollData.size());
	}
}
