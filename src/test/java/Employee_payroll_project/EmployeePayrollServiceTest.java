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
}
