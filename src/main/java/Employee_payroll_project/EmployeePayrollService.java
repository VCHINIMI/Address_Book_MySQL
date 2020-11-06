package Employee_payroll_project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
		this.employeePayRollList = new ArrayList<>(employeePayrollList);
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
		else if (ioService.equals(IOService.DB_IO))
			entries = this.employeePayRollList.size();
		return this.employeePayRollList.size();
	}
	
	public void updateEmployeeSalary(String name, double salary) throws EmployeeException {		
		int result =  employeePayrollDBService.updateEmployeeData(name,salary);
		if(result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if(employeePayrollData != null) employeePayrollData.employeeSalary = salary;
	}
	
	@SuppressWarnings("static-access") public EmployeePayrollData getEmployeePayrollData(String name) {
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

	
	public void addEmployeeToPayroll(String name, String gender, double salary, LocalDate date)  {
		try {
			employeePayRollList.add(employeePayrollDBService.addEmployeeToPayroll(name,gender,salary,date));
		} catch (EmployeeException e) {
			e.printStackTrace();
		}
	}
	
	public List<EmployeePayrollData> deleteEmployee(String name, boolean isActive) throws EmployeeException {
		int update = employeePayrollDBService.deleteEmployee(name, isActive);
		if(update == 1) {
			Iterator<EmployeePayrollData> itr = employeePayRollList.iterator();
			while(itr.hasNext()) {
				EmployeePayrollData employee = itr.next();
				if(employee.employeeName.equals(name)) {
					itr.remove();
				}
			}
		}
		return employeePayRollList;
	}

	public void addEmployeesToPayroll(List<EmployeePayrollData> employeePayrollList) {
		for(int i =0;i<employeePayrollList.size();i++) {
			System.out.println("Employee being added : "+employeePayrollList.get(i).employeeName);
			addEmployeeToPayroll(employeePayrollList.get(i).employeeName, employeePayrollList.get(i).gender, employeePayrollList.get(i).employeeSalary, employeePayrollList.get(i).start);
			System.out.println("Employee Added : "+employeePayrollList.get(i).employeeName);
		}
		System.out.println(employeePayrollList);
	}

	public void addEmployeesToPayrollByThreads(List<EmployeePayrollData> asList) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer,Boolean>();
		employeePayRollList.forEach(emp -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(emp.hashCode(), false);
				System.out.println("Employee Being Added : "+ Thread.currentThread().getName());
				this.addEmployeeToPayroll(emp.employeeName, emp.gender, emp.employeeSalary, emp.start);
				employeeAdditionStatus.put(emp.hashCode(), true);
				System.out.println("Emp Added" + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, emp.employeeName);
			thread.start();
			
		});
		while(employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			}
			catch(InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void updateSalary(Map<Integer, Double> nameSalaryMap) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
		nameSalaryMap.forEach((id , salary) -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(id.hashCode(), false);
				try {
					int result = employeePayrollDBService.updateUsingPreparedStatement(id, salary);
					if(result == 0)
						return;
					employeePayRollList.forEach(employee -> {
						if(employee.id == id) 
							employee.employeeSalary = salary;
					});
				} catch (EmployeeException e) {
					e.printStackTrace();
				}
				employeeAdditionStatus.put(id.hashCode(), true);
				//System.out.println("Employee added: "+ Thread.currentThread().getName());
			};
			Thread thread = new Thread(task);
			thread.start();
		});
		while(employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(20);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void addEmployeesToPayroll(EmployeePayrollData employeePayrollData, IOService ioService) {
		if(ioService.equals(IOService.DB_IO))	{
			try {
				employeePayRollList.add(employeePayrollDBService.addEmployeeToPayroll(employeePayrollData.employeeName,employeePayrollData.gender,employeePayrollData.employeeSalary,employeePayrollData.start));
			} catch (EmployeeException e) {
				e.printStackTrace();
			}
		}
		else 
			employeePayRollList.add(employeePayrollData);
	}

	public void updateEmployeeSalary(String name, int salary, IOService ioService) {
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if(employeePayrollData !=null) employeePayrollData.employeeSalary = salary;
	}

	public void deletePayrollData(String name) {
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		employeePayRollList.remove(employeePayrollData);
	}
}