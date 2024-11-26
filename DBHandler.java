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
