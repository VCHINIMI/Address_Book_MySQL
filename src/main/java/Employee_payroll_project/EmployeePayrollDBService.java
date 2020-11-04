package Employee_payroll_project;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import com.mysql.jdbc.Statement;

import Employee_payroll_project.EmployeeException.ExceptionType;

public class EmployeePayrollDBService {
	private static EmployeePayrollDBService employeePayrollDBService;
	private java.sql.PreparedStatement employeePayrollDataStatement;
	private List<EmployeePayrollData> employeePayrollList;
	private Map<String, Integer> operationMap;

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

	public int updateEmployeeData(String name, double salary) throws EmployeeException {
		return this.updateEmployeeDetailsUsingStatement(name, salary);
	}

	private int updateEmployeeDetailsUsingStatement(String name, double salary) throws EmployeeException {
		String sql = String.format("update employee_payroll_1 set salary = %.2f where name = '%s';", salary, name);
		try (Connection connection = this.getConnection()) {
			java.sql.Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new EmployeeException(e.getMessage(), EmployeeException.ExceptionType.CONNECTION_FAULT);
		} catch (EmployeeException e) {
			throw new EmployeeException(e.getMessage(), e.type);
		}
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				double salary = resultSet.getDouble("salary");
				LocalDate start = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, start));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return employeePayrollList;
	}

	public List<EmployeePayrollData> getEmployeePayrollData(String name) throws EmployeeException {
		employeePayrollList = new ArrayList<EmployeePayrollData>();
		if (this.employeePayrollDataStatement == null)
			this.preparedStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = this.getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return employeePayrollList;
	}

	private void preparedStatementForEmployeeData() throws EmployeeException {
		try {
			Connection connection = this.getConnection();
			String sql = "SELECT * FROM employee_payroll_1 WHERE name = ?";
			employeePayrollDataStatement = connection.prepareStatement(sql);
		} catch (EmployeeException e) {
			throw new EmployeeException(e.getMessage(), e.type);
		} catch (SQLException e) {
			throw new EmployeeException(e.getMessage(), EmployeeException.ExceptionType.CONNECTION_FAULT);
		}
	}

	public List<EmployeePayrollData> getDataWithinDates(String start, String end) throws EmployeeException {
		String sql = String.format(
				"SELECT * FROM employee_payroll_1 WHERE start BETWEEN CAST('%s' AS DATE) AND CAST('%s' AS DATE)", start,
				end);
		return getDataFromDatabaseBySQL(sql);
	}

	public Map<String, Integer> getCountByGender() throws EmployeeException {
		String sql = "SELECT gender, COUNT(name) from employee_payroll_1 GROUP BY gender;";
		operationMap = new HashMap<String, Integer>();
		try (Connection connection = this.getConnection()) {
			java.sql.Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String gender = resultSet.getString("gender");
				int count = resultSet.getInt("COUNT(name)");
				operationMap.put(gender, count);
			}
			return operationMap;
		} catch (EmployeeException e) {
			throw new EmployeeException(e.getMessage(), e.type);
		} catch (SQLException e) {
			throw new EmployeeException(e.getMessage(), EmployeeException.ExceptionType.SQL_FAULT);
		}
	}

	public Map<String, Integer> getLeastSalaryByGender() throws EmployeeException {
		String sql = "SELECT gender, MIN(salary) from employee_payroll_1 GROUP BY gender;";
		operationMap = new HashMap<String, Integer>();
		try (Connection connection = this.getConnection()) {
			java.sql.Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String gender = resultSet.getString("gender");
				int count = resultSet.getInt("MIN(salary)");
				operationMap.put(gender, count);
			}
			return operationMap;
		} catch (EmployeeException e) {
			throw new EmployeeException(e.getMessage(), e.type);
		} catch (SQLException e) {
			throw new EmployeeException(e.getMessage(), EmployeeException.ExceptionType.SQL_FAULT);
		}
	}

	public Map<String, Integer> getAverageSalaryByGender() throws EmployeeException {
		String sql = "SELECT gender, AVG(salary) from employee_payroll_1 GROUP BY gender;";
		operationMap = new HashMap<String, Integer>();
		try (Connection connection = this.getConnection()) {
			java.sql.Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String gender = resultSet.getString("gender");
				int count = resultSet.getInt("AVG(salary)");
				operationMap.put(gender, count);
			}
			return operationMap;
		} catch (EmployeeException e) {
			throw new EmployeeException(e.getMessage(), e.type);
		} catch (SQLException e) {
			throw new EmployeeException(e.getMessage(), EmployeeException.ExceptionType.SQL_FAULT);
		}
	}

	public EmployeePayrollData addEmployeeToPayrollUC7(String name, String gender, int salary, LocalDate date) throws EmployeeException {
		int employeeId = -1;
		EmployeePayrollData employeePayrollData = null ;
		String sql = String.format("INSERT INTO employee_payroll_1(name,gender,salary,start) VALUES ('%s','%s','%s','%s')",name,gender,salary,Date.valueOf(date));
		try (Connection connection = this.getConnection()) {
			java.sql.Statement statement = connection.createStatement();			
			int rowAffected = statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
			if(rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if(resultSet.next()) employeeId = resultSet.getInt(1);
			}
			employeePayrollData = new EmployeePayrollData(employeeId, name, gender, salary, date);
		} catch(SQLException e) {
			throw new EmployeeException(e.getMessage(), EmployeeException.ExceptionType.SQL_FAULT);
		}
		return employeePayrollData ;
	}

	public EmployeePayrollData addEmployeeToPayroll(String name, String gender, double salary, LocalDate date) throws EmployeeException {
		int employeeId = -1;
		Connection connection = null ;
		EmployeePayrollData employeePayrollData = null;
		try {
			connection = this.getConnection();
			try {
				connection.setAutoCommit(false);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (EmployeeException e) {
			throw new EmployeeException(e.getMessage(), EmployeeException.ExceptionType.SQL_FAULT);
		}
		try(java.sql.Statement statement = connection.createStatement()) {
			String sql = String.format("INSERT INTO employee_payroll_1(name,gender,salary,start) VALUES ('%s','%s','%s','%s')",name,gender,salary,Date.valueOf(date));
			int rowAffected = statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
			if(rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if(resultSet.next()) employeeId = resultSet.getInt(1);
			}
		}  catch (SQLException e) {
				try {
					connection.rollback();
					return employeePayrollData;
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				throw new EmployeeException(e.getMessage(), EmployeeException.ExceptionType.SQL_FAULT);
		}
		
		try(java.sql.Statement statement = connection.createStatement()) {
			double deductions = salary * 0.2;
			double taxablePay = salary -deductions;
			double tax = taxablePay * 0.1;
			double netPay = salary - tax ;
			String sql = String.format("INSERT INTO payroll_details_1 "+
												"(employee_id,basic_pay,deductions,taxable_pay,tax,net_pay) Values"+
												"(%s,%s,%s,%s,%s,%s)",employeeId,salary,deductions,taxablePay,tax,netPay);
			int rowAffected = statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
			if(rowAffected == 1) {
				employeePayrollData = new EmployeePayrollData(employeeId, name, gender, salary, date);
			}
		} catch(SQLException e) {
			throw new EmployeeException(e.getMessage(), EmployeeException.ExceptionType.SQL_FAULT);
		} 
		
		try {
			connection.commit();
		}	catch(SQLException e) {
			e.printStackTrace();
		}	finally {
			if(connection!=null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return employeePayrollData;
	}
	
	public int deleteEmployee(String name, boolean isActive) throws EmployeeException {
		String sql = String.format("update employee_payroll_1 set is_active =  %s where name = '%s';", isActive, name);
		try (Connection connection = this.getConnection()) {
			java.sql.Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new EmployeeException(e.getMessage(), EmployeeException.ExceptionType.SQL_FAULT);
		}
	}

	public long countEntries() {
		return employeePayrollList.size();
	}
}