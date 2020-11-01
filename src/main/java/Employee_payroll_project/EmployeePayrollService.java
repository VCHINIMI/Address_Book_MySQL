package Employee_payroll_project;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EmployeePayrollService {
	public static Scanner userInputScanner = new Scanner(System.in);
	public EmployeePayrollFileIOService employeePayrollFileIOService = new EmployeePayrollFileIOService();
	private EmployeePayrollDBService employeePayrollDBService ;
	public static List<EmployeePayrollData> employeePayRollList;
	
	public EmployeePayrollService() {
		employeePayrollDBService = EmployeePayrollDBService.getInstance();
	}

	public enum IOService {
		CONSOLE_IO, FILE_1O, DB_IO, REST_IO
	}

	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this.employeePayRollList = employeePayrollList;
	}

	public void readEmployeePayrollData_UNUSE(IOService ioService) throws Exception {
		if (ioService.equals(IOService.CONSOLE_IO)) {
			System.out.println("Enter Employee Id : ");
			int id = userInputScanner.nextInt();
			userInputScanner.nextLine();
			System.out.println("Enter Employee Name : ");
			String name = userInputScanner.nextLine();
			System.out.println("Enter salary");
			int salary = userInputScanner.nextInt();
			employeePayRollList.add(new EmployeePayrollData(id, name, salary));
		} else if (ioService.equals(IOService.FILE_1O)) {
			employeePayRollList = employeePayrollFileIOService.readFile();
		}
	}

	public void writeEmployeePayrollData(IOService ioService) {
		if (ioService.equals(IOService.CONSOLE_IO))
			System.out.println("Writing Employee Payroll Roaster to console :" + employeePayRollList);
		else if (ioService.equals(IOService.FILE_1O)) {
			employeePayrollFileIOService.writeData(employeePayRollList);
		}
	}

	public void printData(IOService iOService) throws Exception {
		if (iOService.equals(IOService.CONSOLE_IO))
			System.out.println(this.employeePayRollList);
		else if (iOService.equals(IOService.FILE_1O))
			employeePayrollFileIOService.printData();
	}

	public long countEntries(IOService ioService) {
		long entries = 0;
		if (ioService.equals(IOService.CONSOLE_IO))
			entries = employeePayRollList.size();
		else if (ioService.equals(IOService.FILE_1O))
			entries = employeePayrollFileIOService.countEntries();
		return entries;
	}
	
	public void updateEmployeeSalary(String name, double salary) throws EmployeeException {		
		int result =  employeePayrollDBService.updateEmployeeData(name,salary);
		if(result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if(employeePayrollData != null) employeePayrollData.employeeSalary = salary;
	}
	
	@SuppressWarnings("static-access")
	private EmployeePayrollData getEmployeePayrollData(String name) {
		return this.employeePayRollList.stream()
					.filter(employeePayrollDataItem -> employeePayrollDataItem.employeeName.equals(name))
					.findFirst()
					.orElse(null);
	}
	
	public boolean checkEmployeePayrollInSyncWithDB(String name) throws EmployeeException {
		List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}

//Read Employee	Payroll Data
	public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) throws EmployeeException {
		if (ioService.equals(IOService.DB_IO))
			this.employeePayRollList = employeePayrollDBService.readData();
		return this.employeePayRollList;
	}
	
	public List<EmployeePayrollData> readEmployeePayrollDataByDate(IOService ioService, String start, String end) throws EmployeeException {
		if(ioService.equals(IOService.DB_IO))
			this.employeePayRollList = employeePayrollDBService.getDataWithinDates(start, end);
		return this.employeePayRollList;
	}
	
	public Map<String, Integer> getCountByGender(IOService dbIo) throws EmployeeException {
		Map<String, Integer> employeeCountByGenderMap = employeePayrollDBService.getCountByGender();
		return employeeCountByGenderMap;
	}

	public Map<String, Integer> getLeastSalaryByGender() throws EmployeeException {
		Map<String, Integer> employeeLeastSalaryMap = employeePayrollDBService.getLeastSalaryByGender();
		return employeeLeastSalaryMap;
	}

	public Map<String, Integer> getAverageSalaryByGender() throws EmployeeException {
		Map<String, Integer> employeeAverageSalaryMap = employeePayrollDBService.getAverageSalaryByGender();
		return employeeAverageSalaryMap;
	}
}