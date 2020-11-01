package Employee_payroll_project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollFileIOService {
	public static String pAYROLL_FILE_NAME = "payroll-file.txt";

	public void writeData(List<EmployeePayrollData> employeePayrollList) {
		StringBuffer empBuffer = new StringBuffer();
		employeePayrollList.forEach(employee -> {
			String employeeDataString = employee.toString().concat("\n");
			empBuffer.append(employeeDataString);
		});

		try {
			Files.write(Paths.get(pAYROLL_FILE_NAME), empBuffer.toString().getBytes());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void printData() throws Exception {
		try {
			Files.lines(new File(pAYROLL_FILE_NAME).toPath()).forEach(System.out::println);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public long countEntries() {
		long entries = 0;
		try {
			entries = Files.lines(new File(pAYROLL_FILE_NAME).toPath()).count();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return entries;
	}

	public List<EmployeePayrollData> readFile() throws IOException {
		List<EmployeePayrollData> employeePayrollDataArrayList = new ArrayList<>();
		try {
			Files.lines(Paths.get(pAYROLL_FILE_NAME)).forEach(line -> {
				line = line.trim();
				String[] arr = line.split(" ");
				employeePayrollDataArrayList
						.add(new EmployeePayrollData(Integer.parseInt(arr[0]), arr[1], Integer.parseInt(arr[2])));
			});
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return employeePayrollDataArrayList;
	}
}