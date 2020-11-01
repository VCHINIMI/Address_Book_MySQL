package Employee_payroll_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import Employee_payroll_project.EmployeeException.ExceptionType;

public class EmployeePayrollDBService {
	private static EmployeePayrollDBService employeePayrollDBService;
	private EmployeePayrollDBService() {
	}

	private Connection getConnection() throws EmployeeException {
		try {
			String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
			String userName = "root";
			String password = "root";
			Connection connection;
			System.out.println("Connecting to Database" + jdbcURL);
			connection = DriverManager.getConnection(jdbcURL, userName, password);
			System.out.println("successfull" + connection);
			return connection;
		} catch (SQLException e) {
			throw new EmployeeException(e.getMessage(), EmployeeException.ExceptionType.DATABASE_NOT_EXIST);
		}
	}
	
//Read Employee Data
	public List<EmployeePayrollData> readData() throws EmployeeException {
		String sql = "select * from employee_payroll_1;";
		return getDataFromDatabaseBySQL(sql);
	}

//Connect to Database and Retrieve	
	public List<EmployeePayrollData> getDataFromDatabaseBySQL(String sql) throws EmployeeException {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			java.sql.Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				double salary = resultSet.getDouble("salary");
				LocalDate start = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, start));
			}
			return employeePayrollList;
		} catch (EmployeeException e) {
			throw new EmployeeException(e.getMessage(), e.type);
		} catch (SQLException e) {
			throw new EmployeeException(e.getMessage(), EmployeeException.ExceptionType.SQL_FAULT);
		}
	}
	
//To get Instance of object
	public static EmployeePayrollDBService getInstance() {
		if (employeePayrollDBService == null)
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;
	}
}