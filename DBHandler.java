package sms;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * The class that allows access to a database for reading and writing data
 * purposes
 * 
 * @author Artiom
 *
 */
public class DBHandler {
	/**
	 * Login to connect to the database
	 */
	private static String login;

	/**
	 * Password to connect to the database
	 */
	private static String password;

	/**
	 * Database URL
	 */
	static String databaseUrl;

	/**
	 * The var that stores students table's name
	 */
	private final static String studentsTable;

	/**
	 * The var that stores courses table's name
	 */
	private final static String coursesTable;

	/**
	 * The var that stores faculties table's name
	 */
	private final static String facultiesTable;

	/**
	 * Default constructor
	 */
	public DBHandler() {

	}
	/**
	 * Static initializers
	 */
	static {
		login = "root";
		databaseUrl = "jdbc:mysql://localhost:3306/studentsdb";

		studentsTable = "students";
		coursesTable = "courses";
		facultiesTable = "faculties";
	}

	/**
	 * @return The login to connect to the database
	 */
	public static String getLogin() {
		return login;
	}

	/**
	 * @param login - The login to set to connect to the database
	 */
	public static void setLogin(final String login) {
		DBHandler.login = login;
	}

	/**
	 * @return The password to connect to the database
	 */
	public static String getPassword() {
		return password;
	}

	/**
	 * @param password - The password to set to connect to the database
	 */
	public static void setPassword(final String password) {
		DBHandler.password = password;
	}
	/**
	 * @param databaseUrl - the database url to set
	 */
	public static void setDatabaseUrl(final String databaseUrl) {
		DBHandler.databaseUrl = databaseUrl;
	}

	/**
	 * @return The database URL
	 */
	public static String getDatabaseUrl() {
		return databaseUrl;
	}

	/**
	 * @return The students table's name
	 */
	public static String getStudentsTable() {
		return studentsTable;
	}

	/**
	 * @return The faculties table's name
	 */
	public static String getFacultiesTable() {
		return facultiesTable;
	}

	/**
	 * @return The courses table's name
	 */
	public static String getCoursesTable() {
		return coursesTable;
	}

	/**
	 * Checks if a certain table already exists in the database
	 * 
	 * @param tableName - Table's name that is wanted to be checked
	 * @return True if table exists, false otherwise
	 */
	public static boolean checkIfTableExists(final String tableName) {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);

			// Check if a table with tableName name already exists
			DatabaseMetaData dbmData = connection.getMetaData();
			ResultSet resultSet = dbmData.getTables(null, null, tableName, null);
			while (resultSet.next()) {
				if (resultSet.getString(3).equals(tableName)) {
					// Return true if the table has been found
					return true;
				}
			}

			connection.close();
			resultSet.close();

			// Return false if no table has been found
			return false;
		} catch (SQLException e) {
			e.printStackTrace();

			// Return false if an exception has been thrown
			return false;
		}
	}
	/**
	 * Creates a table of students, courses and faculties
	 * 
	 * @return True if no exception has been thrown, false otherwise
	 */
	public static boolean createTables() {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			Statement statement = connection.createStatement();

			if (!checkIfTableExists(studentsTable)) {
				// Creating a table of students
				statement.executeUpdate("create table " + studentsTable + "(ID INTEGER not NULL AUTO_INCREMENT, "
						+ " Name varchar(50), " + "Surname varchar(50), " + "Age INTEGER, " + "Gender varchar(6), "
						+ "Course varchar(50), " + "Started varchar(25),  " + "Graduation varchar(25), "
						+ "PRIMARY KEY ( id ))");
			}

			if (!checkIfTableExists(coursesTable)) {
				// Creating a table of courses
				statement.executeUpdate("create table " + coursesTable + "(ID INTEGER not NULL AUTO_INCREMENT, "
						+ " Name varchar(50), " + "Faculty varchar(50), " + "Duration INTEGER, " + "Attendees INTEGER, "
						+ "PRIMARY KEY ( id ))");
			}

			if (!checkIfTableExists(facultiesTable)) {
				// Creating a table of faculties
				statement.executeUpdate("create table " + facultiesTable + "(ID INTEGER not NULL AUTO_INCREMENT, "
						+ " Name varchar(50), " + "Courses INTEGER, " + "Attendees INTEGER, " + "PRIMARY KEY ( id ))");
			}

			connection.close();
			statement.close();

			// Return true if no exception has been thrown
			return true;

		} catch (SQLException e) {
			e.printStackTrace();

			// Return false if an exception has been thrown
			return false;
		}
	}

	/**
	 * Adds a new student to the table
	 * 
	 * @return True if no exception has been thrown, false otherwise
	 */
	public static boolean addStudent() {
		try {
			Connection connection = DriverManager.getConnection(databaseUrl, login, password);
			PreparedStatement preparedStatement = connection.prepareStatement("insert into " + studentsTable
					+ " (Name, Surname, Age, Gender, Course, Started, Graduation) values " + "(?, ?, ?, ?, ?, ?, ?)");

			// Getting the duration of the course in order to calculate Graduation date
			// field
			PreparedStatement preparedStatement2 = connection
					.prepareStatement("select Duration from Courses where Name = " + "\""
							+ ManagementView.courseSelectionBox.getSelectedItem().toString() + "\"");
			ResultSet resultSet = preparedStatement2.executeQuery();
			resultSet.next();
			final int courseDuration = resultSet.getInt("Duration");

			preparedStatement.setString(1, ManagementView.nameField.getText());
			preparedStatement.setString(2, ManagementView.surnameField.getText());
			preparedStatement.setInt(3, Integer.parseInt(ManagementView.ageField.getText()));
			preparedStatement.setString(4, ManagementView.genderSelectionBox.getSelectedItem().toString());
			preparedStatement.setString(5, ManagementView.courseSelectionBox.getSelectedItem().toString());

			final String inputDate = ManagementView.startedDateField.getText();
			LocalDate startedDate = LocalDate.of(Integer.parseInt(inputDate.substring(0, 4)),
					Integer.parseInt(inputDate.substring(5, 7)), Integer.parseInt(inputDate.substring(8, 10)));
			preparedStatement.setString(6, startedDate.toString());

			LocalDate graduationDate = startedDate.plusMonths(courseDuration);
			preparedStatement.setString(7, graduationDate.toString());

			preparedStatement.executeUpdate();

			connection.close();
			preparedStatement.close();

			updateStudents();

			// Return true if no exception has been thrown
			return true;
		} catch (SQLException e) {
			e.printStackTrace();

			// Return false if an exception has been thrown
			return false;
		} catch (Exception e) {
			e.printStackTrace();

			// Return false if an exception has been thrown
			return false;
		}
	}
