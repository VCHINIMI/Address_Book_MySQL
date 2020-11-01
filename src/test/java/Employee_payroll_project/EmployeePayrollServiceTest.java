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
	

	@Test
	public void givenEmployeeDataInDB_ShouldReturnCountOfEmployee() throws EmployeeException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<String, Integer> map = employeePayrollService.getCountByGender(IOService.DB_IO);
		assertEquals((int) map.get("M"), 2);
		assertEquals((int) map.get("F"), 1);
	}	
	
	@Test
	public void givenEmployeeDataInDB_ShouldReturnEmployeeByGender_ByMinSalary() throws EmployeeException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<String, Integer> map = employeePayrollService.getLeastSalaryByGender();
		assertEquals((int) map.get("M"), 1000000);
		assertEquals((int) map.get("F"), 3000000);
	}
	
	@Test
	public void givenEmployeeDataInDB_ShouldReturnEmployeeByGender_ByAverageSalary() throws EmployeeException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<String, Integer> map = employeePayrollService.getAverageSalaryByGender();
		assertEquals((int) map.get("M"), 2000000);
		assertEquals((int) map.get("F"), 3000000);
	}
	
	@Test
	public void givenNewAddedShouldBeInSyncWithDB() throws EmployeeException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.addEmployeeToPayroll("Mark","M",5000000,LocalDate.now());
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
		assertTrue(result);
	}
	
	@Test
	public void givenEmployee_whenDeleted_shouldBeRemovedFromEmployeeList() throws EmployeeException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();;
		employeePayrollService .readEmployeePayrollData(IOService.DB_IO);
		List<EmployeePayrollData> list = employeePayrollService.deleteEmployee("Mark",false);
	//	assertEquals(4, list.size());
	}
	
}
