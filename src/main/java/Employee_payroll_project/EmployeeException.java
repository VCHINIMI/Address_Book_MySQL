package Employee_payroll_project;

public class EmployeeException extends Exception {
	private static final long serialVersionUID = 5659569326670024185L;

	public enum ExceptionType {
		CONNECTION_FAULT, PATH_FAULT, DATABASE_NOT_EXIST, SQL_FAULT;
	}

	ExceptionType type;

	public EmployeeException(String message, ExceptionType type) {
		super(message);
		this.type = type;
	}

	public EmployeeException(String message, ExceptionType type, Throwable cause) {
		super(message, cause);
		this.type = type;
	}

}
