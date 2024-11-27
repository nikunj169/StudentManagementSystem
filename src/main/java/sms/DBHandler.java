package sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import javax.swing.JOptionPane;
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
     * Database connection object
     */
    public static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

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
        password = "chirayu@1111"; // **IMPORTANT:** Replace with your actual database password
        databaseUrl = "jdbc:mysql://localhost:3306/studentsdb?useSSL=false";

        studentsTable = "students";
        coursesTable = "courses";
        facultiesTable = "faculties";

        // Initialize the database connection
        if (connectToDatabase()) {
            System.out.println("Database connection established successfully.");
            // Optionally, create tables if they don't exist
            if (createTables()) {
                System.out.println("Tables are ready.");
            } else {
                System.err.println("Failed to create tables.");
            }
        } else {
            System.err.println("Failed to establish database connection.");
        }

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
     * Establishes a connection to the database
     * 
     * @return True if connection is successful, false otherwise
     */
    public static boolean connectToDatabase() {
        try {
            // Loading the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establishing the connection
            connection = DriverManager.getConnection(databaseUrl, login, password);

            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Closes the connection to the database
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a certain table already exists in the database
     * 
     * @param tableName - Table's name that is wanted to be checked
     * @return True if table exists, false otherwise
     */
    public static boolean checkIfTableExists(final String tableName) {
        try {
            // Check if a table with tableName name already exists
            DatabaseMetaData dbmData = connection.getMetaData();
            ResultSet resultSet = dbmData.getTables(null, null, tableName, null);
            while (resultSet.next()) {
                if (resultSet.getString(3).equals(tableName)) {
                    // Return true if the table has been found
                    resultSet.close();
                    return true;
                }
            }
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
     * Creates a table of students, courses, faculties, marking schemes, and grades
     * 
     * @return True if no exception has been thrown, false otherwise
     */
    public static boolean createTables() {
        try {
            Statement statement = connection.createStatement();

            if (!checkIfTableExists(studentsTable)) {
                // Creating a table of students
                statement.executeUpdate("CREATE TABLE " + studentsTable + " (ID INTEGER not NULL AUTO_INCREMENT, "
                        + " Name VARCHAR(50), " + "Surname VARCHAR(50), " + "Age INTEGER, " + "Gender VARCHAR(6), "
                        + "Course VARCHAR(50), " + "Started DATE,  " + "Graduation DATE, " + "Grade CHAR(2), "
                        + "PRIMARY KEY ( ID ))");
            }

            if (!checkIfTableExists(coursesTable)) {
                // Creating a table of courses
                statement.executeUpdate("CREATE TABLE " + coursesTable + " (ID INTEGER not NULL AUTO_INCREMENT, "
                        + " Name VARCHAR(50), " + "Faculty VARCHAR(50), " + "Duration INTEGER, " + "Attendees INTEGER, "
                        + "PRIMARY KEY ( ID ))");
            }

            // Create faculties table
            if (!checkIfTableExists(facultiesTable)) {
                statement.executeUpdate("CREATE TABLE " + facultiesTable + " ("
                    + "ID INTEGER NOT NULL AUTO_INCREMENT, "
                    + "Name VARCHAR(30) NOT NULL UNIQUE, "
                    + "Username VARCHAR(50) NOT NULL UNIQUE, " // Added Username field
                    + "Courses INTEGER, "
                    + "Attendees INTEGER, "
                    + "PRIMARY KEY (ID))");
            }

            // Create marking_schemes table
            if (!checkIfTableExists("marking_schemes")) {
                statement.executeUpdate("CREATE TABLE marking_schemes (" + "id INTEGER not NULL AUTO_INCREMENT, "
                        + "course_id INTEGER NOT NULL, " + "grade_a DECIMAL(5,2) NOT NULL, "
                        + "grade_a_minus DECIMAL(5,2) NOT NULL, " + "grade_b_plus DECIMAL(5,2) NOT NULL, "
                        + "grade_b DECIMAL(5,2) NOT NULL, " + "grade_b_minus DECIMAL(5,2) NOT NULL, "
                        + "grade_c DECIMAL(5,2) NOT NULL, " + "grade_d DECIMAL(5,2) NOT NULL, "
                        + "grade_f DECIMAL(5,2) NOT NULL, " + "PRIMARY KEY (id), "
                        + "FOREIGN KEY (course_id) REFERENCES courses(ID) ON DELETE CASCADE)");
            }

            // Create grades table
            if (!checkIfTableExists("grades")) {
                statement.executeUpdate("CREATE TABLE grades (" + "student_id INTEGER NOT NULL, "
                        + "course_id INTEGER NOT NULL, " + "marks DECIMAL(5,2) NOT NULL, " + "grade CHAR(2), "
                        + "PRIMARY KEY (student_id, course_id), "
                        + "FOREIGN KEY (student_id) REFERENCES students(ID) ON DELETE CASCADE, "
                        + "FOREIGN KEY (course_id) REFERENCES courses(ID) ON DELETE CASCADE)");
            }

            if (!checkIfTableExists("faculty_logins")) {
                // Creating a table of faculty logins
                statement.executeUpdate("CREATE TABLE faculty_logins ("
                    + "username VARCHAR(50) NOT NULL, "
                    + "password VARCHAR(100) NOT NULL, "
                    + "faculty_name VARCHAR(30) NOT NULL, " // Ensuring data type and length match
                    + "PRIMARY KEY (username), "
                    + "FOREIGN KEY (faculty_name) REFERENCES faculties(Name) ON DELETE CASCADE)");
            }

            if (!checkIfTableExists("attendance")) {
                statement.executeUpdate("CREATE TABLE attendance ("
                        + "student_id INTEGER NOT NULL, "
                        + "date DATE NOT NULL, "
                        + "status VARCHAR(10) NOT NULL, "
                        + "PRIMARY KEY (student_id, date), "
                        + "FOREIGN KEY (student_id) REFERENCES students(ID) ON DELETE CASCADE)");
            }

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
    public static boolean addStudent(String name, String surname, int age, String gender, String course, Date started, Date graduation) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + studentsTable
                    + " (Name, Surname, Age, Gender, Course, Started, Graduation) VALUES " + "(?, ?, ?, ?, ?, ?, ?)");
    
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, gender); // Set as String
            preparedStatement.setString(5, course);
            preparedStatement.setDate(6, new java.sql.Date(started.getTime()));
            preparedStatement.setDate(7, new java.sql.Date(graduation.getTime()));
    
            preparedStatement.executeUpdate();
            preparedStatement.close();
    
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the contents of the table
     * 
     * @return True if no exception has been thrown, false otherwise
     */
    public static boolean updateStudents() {
        int howManyColumns = 0, currentColumn = 0;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT s.*, g.grade FROM "
                    + studentsTable + " s LEFT JOIN grades g ON s.ID = g.student_id");

            // Reading data from table
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData rsmData = resultSet.getMetaData();

            howManyColumns = rsmData.getColumnCount();

            DefaultTableModel recordTable = (DefaultTableModel) ManagementView.table.getModel();
            recordTable.setRowCount(0);

            while (resultSet.next()) {
                Vector<Object> columnData = new Vector<>();

                columnData.add(resultSet.getInt("ID"));
                columnData.add(resultSet.getString("Name"));
                columnData.add(resultSet.getString("Surname"));
                columnData.add(resultSet.getInt("Age"));
                columnData.add(resultSet.getString("Gender"));
                columnData.add(resultSet.getString("Course"));
                columnData.add(resultSet.getDate("Started"));
                columnData.add(resultSet.getDate("Graduation"));
                columnData.add(resultSet.getString("grade")); // Add grade column

                recordTable.addRow(columnData);
            }

            updateAttendees();

            preparedStatement.close();
            resultSet.close();

            // Return true if no exception has been thrown
            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            // Return false if exception has been thrown
            return false;
        }
    }

    /**
     * Deletes the selected student from the table
     * 
     * @return True if no exception has been thrown, false otherwise
     */
    public static boolean deleteStudent() {
        // Getting row that user selected
        DefaultTableModel recordTable = (DefaultTableModel) ManagementView.table.getModel();
        int selectedRow = ManagementView.table.getSelectedRow();
        ManagementView.table.clearSelection();

        try {
            // Getting the ID of the student in the selected row
            final int ID = Integer.parseInt(recordTable.getValueAt(selectedRow, 0).toString());

            PreparedStatement preparedStatement = connection
                    .prepareStatement("DELETE FROM " + studentsTable + " WHERE ID = ?");

            preparedStatement.setInt(1, ID);
            preparedStatement.executeUpdate();

            preparedStatement.close();

            updateStudents();

            // Return true if no exception has been thrown
            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            // Return false if exception has been thrown
            return false;
        }
    }

 /**
     * Adds a faculty to the faculties table
     * 
     * @return True if no exception has been thrown, false otherwise
     */
    public static boolean addFaculty(final String facultyName) {
        try {
            // Generate username, possibly based on facultyName or separate input
            String username = facultyName.toLowerCase().replaceAll("\\s+", "") + "@uni"; // Example
            String password = generateFacultyPassword(facultyName);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + facultiesTable + " (Name, Username, Courses, Attendees) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, facultyName);
            preparedStatement.setString(2, username); // Set Username
            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, 0);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Store username and password in faculty_logins table
            PreparedStatement loginStmt = connection.prepareStatement(
                    "INSERT INTO faculty_logins (username, password, faculty_name) VALUES (?, ?, ?)");
            loginStmt.setString(1, username);
            loginStmt.setString(2, password);
            loginStmt.setString(3, facultyName);
            loginStmt.executeUpdate();
            loginStmt.close();

            // Inform the admin about the credentials
            JOptionPane.showMessageDialog(null, "Faculty Username: " + username + "\nPassword: " + password,
                    "Faculty Credentials", JOptionPane.INFORMATION_MESSAGE);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a course to the courses table
     * 
     * @return True if no exception has been thrown, false otherwise
     */
    public static boolean addCourse(final String courseName, final String faculty, final int duration) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + coursesTable + " (Name, Faculty, Duration, Attendees) VALUES (?, ?, ?, ?)");

            preparedStatement.setString(1, courseName);
            preparedStatement.setString(2, faculty);
            preparedStatement.setInt(3, duration);
            preparedStatement.setInt(4, 0);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            updateAttendees();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void updateFacultyPassword(String facultyName) {
        try {
            // Generate the password using C++ code
            String password = generateFacultyPassword(facultyName);

            // Update the password in the database
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE faculty_logins SET password = ? WHERE faculty_name = ?");
            ps.setString(1, password);
            ps.setString(2, facultyName);
            ps.executeUpdate();
            ps.close();

            // Inform the admin about the updated password
            JOptionPane.showMessageDialog(null, "Updated Password for " + facultyName + ": " + password,
                    "Faculty Password Updated", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String generateFacultyPassword(String facultyName) {
        String password = null;

        try {
            // Build the command to run the C++ program
            String command = "PasswordGenerator " + facultyName;

            // Run the command
            Process process = Runtime.getRuntime().exec(command);

            // Read the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            password = reader.readLine();

            reader.close();

            // Wait for the process to complete
            process.waitFor();

            // Check if password is null or empty
            if (password == null || password.isEmpty()) {
                // Fallback to Java password generation
                password = facultyName.substring(0, Math.min(4, facultyName.length())) + "@1234";
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to Java password generation
            password = facultyName.substring(0, Math.min(4, facultyName.length())) + "@1234";
        }

        return password;
    }
    
    /**
     * Authenticates a faculty member using username and password.
     * 
     * @param username - The faculty's username
     * @param password - The faculty's password
     * @return True if authentication is successful, false otherwise
     */
    public static boolean authenticateFaculty(String username, String password) {
        if (connection == null) {
            System.err.println("Database connection is not initialized.");
            return false;
        }

        String query = "SELECT * FROM faculty_logins WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // Set as String

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Authentication successful
                rs.close();
                return true;
            } else {
                // Authentication failed
                rs.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

public static List<String> getCoursesByFacultyName(String facultyUsername) {
    List<String> courses = new ArrayList<>();
    try {
        // First, get the faculty name from username
        PreparedStatement ps = connection.prepareStatement("SELECT faculty_name FROM faculty_logins WHERE username = ?");
        ps.setString(1, facultyUsername);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String facultyName = rs.getString("faculty_name");
            rs.close();
            ps.close();

            // Now get courses for that faculty
            courses = getCoursesByFaculty(facultyName);
        } else {
            rs.close();
            ps.close();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return courses;
}

    /**
     * Gets all the faculties from the faculties table
     * 
     * @return An array with all the faculties
     */
    public static String[] getFaculties() {
        Vector<String> faculties = new Vector<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Name FROM " + facultiesTable);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Add every name of the faculty to the "faculties" vector
            while (resultSet.next()) {
                faculties.add(resultSet.getString("Name"));
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Convert "faculties" vector to String array and return it
        return faculties.toArray(new String[0]);
    }

    /**
     * Gets all the courses from the courses table
     * 
     * @return An array with all the courses
     */
    public static String[] getCourses() {
        Vector<String> courses = new Vector<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Name FROM " + coursesTable);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Add every name of the courses to the "courses" vector
            while (resultSet.next()) {
                courses.add(resultSet.getString("Name"));
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Convert "courses" vector to String array and return it
        return courses.toArray(new String[0]);
    }

    /**
     * Updates the number of attendees in faculties and courses tables
     */
    private static void updateAttendees() {
        updateCoursesAttendees();
        updateFacultiesAttendees();
    }

    /**
     * Updates the number of attendees in courses table
     * 
     * @return True if no exception has been thrown, false otherwise
     */
    private static boolean updateCoursesAttendees() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Course FROM " + studentsTable);
            Statement statement = connection.createStatement();

            // Setting number of attendees to 0 initially, in order to avoid wrong calculations
            statement.executeUpdate("UPDATE " + getCoursesTable() + " SET Attendees = 0");

            // Reading courses that students attend from the table
            ResultSet resultSet = preparedStatement.executeQuery();
            HashMap<String, Integer> coursesAttendees = new HashMap<>();

            // Calculating the number of attendees to the courses
            while (resultSet.next()) {
                String currentCourse = resultSet.getString("Course");
                coursesAttendees.put(currentCourse, coursesAttendees.getOrDefault(currentCourse, 0) + 1);
            }

            // Update the number of attendees to the courses in the courses table
            for (String key : coursesAttendees.keySet()) {
                PreparedStatement updateStatement = connection.prepareStatement(
                        "UPDATE " + coursesTable + " SET Attendees = ? WHERE Name = ?");
                updateStatement.setInt(1, coursesAttendees.get(key));
                updateStatement.setString(2, key);
                updateStatement.executeUpdate();
                updateStatement.close();
            }

            preparedStatement.close();
            resultSet.close();
            statement.close();

            // Return true if no exception has been thrown
            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            // Return false if exception has been thrown
            return false;
        }
    }

    /**
     * Updates the number of attendees in faculties table
     * 
     * @return True if no exception has been thrown, false otherwise
     */
    private static boolean updateFacultiesAttendees() {
        try {
            PreparedStatement preparedStatement = null, preparedStatement2 = null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = null, resultSet2 = null;

            // Setting number of courses and attendees to 0 initially, in order to avoid wrong calculations
            statement.executeUpdate("UPDATE " + facultiesTable + " SET Attendees = 0, Courses = 0");

            // Getting the faculties of courses and number of attendees
            preparedStatement = connection.prepareStatement("SELECT Faculty, Attendees FROM " + coursesTable);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final String faculty = resultSet.getString("Faculty");
                final int courseAttendees = resultSet.getInt("Attendees");

                preparedStatement2 = connection.prepareStatement(
                        "SELECT Attendees, Courses FROM " + facultiesTable + " WHERE Name = ?");
                preparedStatement2.setString(1, faculty);
                resultSet2 = preparedStatement2.executeQuery();

                resultSet2.next();
                final int currentNumberOfAttendees = resultSet2.getInt("Attendees");
                final int currentNumberOfCourses = resultSet2.getInt("Courses");

                PreparedStatement updateStatement = connection
                        .prepareStatement("UPDATE " + facultiesTable + " SET Attendees = ?, Courses = ? WHERE Name = ?");
                updateStatement.setInt(1, courseAttendees + currentNumberOfAttendees);
                updateStatement.setInt(2, currentNumberOfCourses + 1);
                updateStatement.setString(3, faculty);
                updateStatement.executeUpdate();
                updateStatement.close();

                resultSet2.close();
                preparedStatement2.close();
            }

            preparedStatement.close();
            resultSet.close();
            statement.close();

            // Return true if no exception has been thrown
            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            // Return false if exception has been thrown
            return false;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Searches if there is already an element with a certain name in a certain table
     * 
     * @param tableName - The table in which user wants to check if element already exists
     * @param name      - The name of the element user wants to check
     * @return true if the element has been found, false otherwise
     */
    public static boolean checkIfElementExists(final String tableName, final String name) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Name FROM " + tableName);

            // Get all the elements' name
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("Name").equals(name)) {
                    // Return true if an element has been found
                    preparedStatement.close();
                    resultSet.close();
                    return true;
                }
            }

            preparedStatement.close();
            resultSet.close();

            // Return false if no element has been found in the table
            return false;
        } catch (SQLException e) {
            e.printStackTrace();

            // Return false if an exception has been thrown
            return false;
        }
    }

    /**
     * Gets the number of attendees in a course or faculty
     * 
     * @param tableName - The table in which user wants to check the number of attendees (Faculties/Courses table)
     * @param element   - The course/faculty name in which user wants to check the number of attendees
     * @return The number of attendees in a faculty/course.
     */
    public static int getNumberOfAttendees(final String tableName, final String element) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT Attendees FROM " + tableName + " WHERE Name = ?");
            preparedStatement.setString(1, element);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int attendees = resultSet.getInt("Attendees");

            preparedStatement.close();
            resultSet.close();

            return attendees;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Deletes the students that attend a certain course
     * 
     * @param course - The course's name which attendees should be deleted
     * @return True if no exception has been thrown, false otherwise
     */
    public static boolean deleteCourseAttendees(final String course) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("DELETE FROM " + getStudentsTable() + " WHERE Course = ?");
            preparedStatement.setString(1, course);
            preparedStatement.executeUpdate();

            updateStudents();

            preparedStatement.close();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Deletes a course from the courses table
     * 
     * @param course - The course's name which should be deleted
     * @return True if no exception has been thrown, false otherwise
     */
    public static boolean deleteCourse(final String course) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("DELETE FROM " + getCoursesTable() + " WHERE Name = ?");
            preparedStatement.setString(1, course);
            preparedStatement.executeUpdate();

            updateStudents();

            preparedStatement.close();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Deletes a faculty from the faculties table
     * 
     * @param faculty - The faculty name which should be deleted
     * @return True if no exception has been thrown, false otherwise
     */
    public static boolean deleteFaculty(final String faculty) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("DELETE FROM " + getFacultiesTable() + " WHERE Name = ?");
            preparedStatement.setString(1, faculty);
            preparedStatement.executeUpdate();

            updateStudents();

            preparedStatement.close();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Deletes all the courses in a certain faculty
     * 
     * @param faculty - The faculty whose courses should be deleted
     * @return True if no exception has been thrown, false otherwise
     */
    public static boolean deleteFacultyCourses(final String faculty) {
        try {
            // Getting the courses in that faculty, in order to delete students attending them
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT Name FROM " + getCoursesTable() + " WHERE Faculty = ?");
            preparedStatement.setString(1, faculty);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                deleteCourseAttendees(resultSet.getString("Name"));
            }

            // Deleting the courses
            PreparedStatement deleteStatement = connection
                    .prepareStatement("DELETE FROM " + getCoursesTable() + " WHERE Faculty = ?");
            deleteStatement.setString(1, faculty);
            deleteStatement.executeUpdate();

            updateStudents();

            preparedStatement.close();
            resultSet.close();
            deleteStatement.close();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Gets the number of courses in a faculty
     * 
     * @param faculty - The faculty's name whose number of courses should be read
     * @return The number of courses in a faculty
     */
    public static int getNumberOfCourses(final String faculty) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT Courses FROM " + getFacultiesTable() + " WHERE Name = ?");
            preparedStatement.setString(1, faculty);

            // Get Courses field's value
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int courses = resultSet.getInt("Courses");

            preparedStatement.close();
            resultSet.close();

            return courses;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Updates the contents of the database, taking into account changes from table
     * 
     * @return True if no exception has been thrown, false otherwise
     */
    public static boolean updateDatabase() {
        // Getting row and column that user selected
        int selectedRow = ManagementView.table.getSelectedRow();
        int selectedColumn = ManagementView.table.getSelectedColumn();

        try {
            // If a cell has been selected
            if (selectedRow > -1 && selectedColumn > -1) {
                // Getting the selected field of the selected student and changing it in database
                String columnName = "";
                switch (selectedColumn) {
                    case 1:
                        columnName = "Name";
                        break;
                    case 2:
                        columnName = "Surname";
                        break;
                    case 3:
                        columnName = "Age";
                        break;
                    case 4:
                        columnName = "Gender";
                        break;
                    // Add cases for other columns if needed
                    default:
                        break;
                }

                if (!columnName.isEmpty()) {
                    PreparedStatement preparedStatement = connection.prepareStatement(
                            "UPDATE " + studentsTable + " SET " + columnName + " = ? WHERE ID = ?");
                    preparedStatement.setString(1, ManagementView.table.getValueAt(selectedRow, selectedColumn).toString());
                    preparedStatement.setInt(2, Integer.parseInt(ManagementView.table.getValueAt(selectedRow, 0).toString()));
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                }
            }

            // Return true if no exception has been thrown
            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            // Return false if exception has been thrown
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();

            return false;
        }
    }

/**
     * Retrieves a list of faculties
     * 
     * @return List of faculty names
     */
    public static List<String> getFacultiesList() {
        List<String> faculties = new ArrayList<>();
        String sql = "SELECT Name FROM " + facultiesTable;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                faculties.add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return faculties;
    }

    /**
     * Retrieves a list of courses by faculty name
     * 
     * @param facultyName - The faculty name
     * @return List of course names
     */
    public static List<String> getCoursesByFaculty(String facultyName) {
        List<String> courses = new ArrayList<>();
        String sql = "SELECT Name FROM " + coursesTable + " WHERE Faculty = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, facultyName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                courses.add(rs.getString("Name"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    /**
     * Retrieves the course ID by course name
     * 
     * @param courseName - The course name
     * @return The course ID, or -1 if not found
     */
    public static int getCourseIdByName(String courseName) {
        String sql = "SELECT ID FROM " + coursesTable + " WHERE Name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int courseId = rs.getInt("ID");
                rs.close();
                return courseId;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if not found
    }

    /**
     * Retrieves a list of students by course name
     * 
     * @param courseName - The course name
     * @return List of Student objects
     */
    public static List<Student> getStudentsByCourse(String courseName) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM " + studentsTable + " WHERE Course = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("ID"));
                student.setName(rs.getString("Name"));
                student.setSurname(rs.getString("Surname"));
                student.setAge(rs.getInt("Age"));
                String genderStr = rs.getString("Gender");
                // Removed enum usage
                student.setGender(genderStr);
                student.setCourse(rs.getString("Course"));
                student.setStarted(rs.getDate("Started"));
                student.setGraduation(rs.getDate("Graduation"));
                student.setGrade(rs.getString("Grade"));
                students.add(student);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Updates the grade of a student in the students table
     * 
     * @param studentId - The student ID
     * @param grade     - The grade to set
     */
    public static boolean updateStudentGrade(int studentId, String grade) {
        String sql = "UPDATE " + studentsTable + " SET Grade = ? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, grade);
            pstmt.setInt(2, studentId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Student> getStudentsByFaculty(String facultyUsername) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.* FROM " + studentsTable + " s " +
                     "JOIN courses c ON s.Course = c.Name " +
                     "JOIN " + facultiesTable + " f ON c.Faculty = f.Name " +
                     "WHERE f.Username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, facultyUsername);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("ID"));
                student.setName(rs.getString("Name"));
                student.setSurname(rs.getString("Surname"));
                student.setAge(rs.getInt("Age"));
                student.setGender(rs.getString("Gender"));
                student.setCourse(rs.getString("Course"));
                student.setStarted(rs.getDate("Started"));
                student.setGraduation(rs.getDate("Graduation"));
                student.setGrade(rs.getString("Grade"));
                students.add(student);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    /**
     * Gets the duration of a course by its name.
     * 
     * @param courseName The name of the course.
     * @return The duration in months, or -1 if not found.
     */
    public static int getCourseDuration(String courseName) {
        String sql = "SELECT Duration FROM " + coursesTable + " WHERE Name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int duration = rs.getInt("Duration");
                rs.close();
                return duration;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Indicates not found
    }

    /**
     * Updates the attendance record for a student on a specific date.
     * 
     * @param studentId The ID of the student.
     * @param date      The date of attendance (YYYY-MM-DD).
     * @param status    The attendance status ("Present" or "Absent").
     * @return True if update is successful, false otherwise.
     */
    public static boolean updateAttendance(int studentId, String date, String status) {
        try {
            // Assuming there's an 'attendance' table with columns: student_id, date, status
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO attendance (student_id, date, status) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE status = ?");
            pstmt.setInt(1, studentId);
            pstmt.setString(2, date);
            pstmt.setString(3, status);
            pstmt.setString(4, status);
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
     
    public static Student getStudentById(int studentId) {
        String sql = "SELECT * FROM " + studentsTable + " WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("ID"));
                student.setName(rs.getString("Name"));
                student.setSurname(rs.getString("Surname"));
                student.setAge(rs.getInt("Age"));
                student.setGender(rs.getString("Gender"));
                student.setCourse(rs.getString("Course"));
                student.setStarted(rs.getDate("Started"));
                student.setGraduation(rs.getDate("Graduation"));
                student.setGrade(rs.getString("Grade"));
                rs.close();
                return student;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all student IDs from the students table.
     * 
     * @return Array of student IDs as Strings.
     */
    public static String[] getStudentIds() {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT ID FROM " + studentsTable;
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ids.add(String.valueOf(rs.getInt("ID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids.toArray(new String[0]);
    }

}
