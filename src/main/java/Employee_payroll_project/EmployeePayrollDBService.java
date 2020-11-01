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
	
}