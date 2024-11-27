package sms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * The class that holds the front-end table-management part of the application
 * and manages the actions performed out there
 *
 * @author Artiom
 *
 */
public class ManagementView {

    /**
     * The contents of the management window where you read and write students data
     */
    static JFrame managementFrame;

    /**
     * The table containing all students
     */
    static JTable table;

    /**
     * The field where user should write the student's name
     */
    static JTextField nameField;

    /**
     * The field where user should write the student's surname
     */
    static JTextField surnameField;

    /**
     * The field where user should write the student's age
     */
    static JTextField ageField;

    /**
     * The field where user should write the date when the student started attending
     * the course
     */
    static JTextField startedDateField;

    /**
     * The box that user uses in order to select student's gender
     */
    static JComboBox<String> genderSelectionBox; // Changed to JComboBox<String>

    /**
     * The box that allows user to select a course for a student
     */
    static JComboBox<String> courseSelectionBox;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Reading messages in dependence of the selected language (by default ENG)
                Translator.getMessagesFromXML();

                try {
                    ManagementView window = new ManagementView();
                    window.managementFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public ManagementView() {
        initialize();
        // Clear the selection in the table, to avoid issues with updateDatabase method
        // when cells are selected
        table.clearSelection();
        // Make it visible in constructor, in order to make tests in
        // ManagementViewTest.java work
        managementFrame.setVisible(true);
        DBHandler.updateStudents();
    }

    /**
     * Updates the list of courses
     */
    private void updateCourses() {
        // Get the lists of courses
        DefaultComboBoxModel<String> courses = new DefaultComboBoxModel<>(DBHandler.getCourses());
        courseSelectionBox.setModel(courses);
    }

    /**
     * Refreshes the student table with the latest data
     */
    public void refreshStudentTable() {
        DBHandler.updateStudents();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        managementFrame = new JFrame();
        managementFrame.setBounds(100, 100, 860, 540);
        managementFrame.setResizable(false);
        managementFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managementFrame.setTitle("Student Management System"); // Consider using Translator for localization
        managementFrame.getContentPane().setLayout(null);

        // The panel where students table is located
        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(new LineBorder(SystemColor.textHighlight, 5));
        tablePanel.setBounds(260, 10, 575, 395);
        managementFrame.getContentPane().add(tablePanel);
        tablePanel.setLayout(null);

        // The scroll pane that allows navigation through table
        JScrollPane tableScrollPane = new JScrollPane();
        tableScrollPane.setBounds(10, 10, 555, 375);
        tablePanel.add(tableScrollPane);

        // Initializing the table and setting its model
        table = new JTable();
        tableScrollPane.setViewportView(table);
        table.setColumnSelectionAllowed(true);
        table.setModel(new DefaultTableModel(new Object[][] {},
                new String[] { "ID", "Name", "Surname", "Age", "Gender", "Course", "Started", "Graduation", "Grade" }) {
            boolean[] columnEditables = new boolean[] { false, true, true, true, true, false, false, false, false };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });

        // Creating a sorter for the table
        TableRowSorter<DefaultTableModel> tableSorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(tableSorter);

        // Creating a Table Listener to detect cell modifications
        table.getModel().addTableModelListener(new TableModelListener() {

            // Actions to perform when a cell has been edited
            public void tableChanged(TableModelEvent e) {
                if (!DBHandler.updateDatabase()) {
                    JOptionPane.showMessageDialog(managementFrame, "Check your input.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // The panel where all buttons are located
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(new LineBorder(new Color(0, 120, 215), 5));
        buttonsPanel.setBackground(UIManager.getColor("Button.background"));
        buttonsPanel.setBounds(10, 415, 825, 80);
        managementFrame.getContentPane().add(buttonsPanel);

        // The button to press to delete an information from the table
        JButton deleteButton = new JButton("Delete"); // Use Translator.getValue("delete") if defined
        deleteButton.setName("deleteButton");

        // Actions to perform when "delete" button clicked
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // If no row has been selected
                if (table.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(managementFrame, "No student selected.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Asking the user if they are sure about that
                    if (JOptionPane.showConfirmDialog(managementFrame, "Are you sure you want to delete the selected student?",
                            "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        if (DBHandler.deleteStudent()) {
                            JOptionPane.showMessageDialog(managementFrame,
                                    "Student successfully deleted.", "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(managementFrame,
                                    "Something went wrong. Please try again.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        deleteButton.setFont(new Font("Tahoma", Font.PLAIN, 16));

        // The button to press to add a student to the table
        JButton addButton = new JButton("Add"); // Use Translator.getValue("add") if defined
        addButton.setName("addButton");

        // Inside addButton's ActionListener
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                table.clearSelection();

                if (DBHandler.getFacultiesList().isEmpty()) {
                    JOptionPane.showMessageDialog(managementFrame, "Cannot add student. No faculties available.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validate fields
                if (nameField.getText().trim().isEmpty() || surnameField.getText().trim().isEmpty() ||
                        ageField.getText().trim().isEmpty() || startedDateField.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(managementFrame, "Please fill in all required fields.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        // Validate date format
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        format.setLenient(false);
                        format.parse(startedDateField.getText());
                    } catch (ParseException ex) {
                        ex.printStackTrace();

                        JOptionPane.showMessageDialog(managementFrame, "Invalid date format. Use YYYY-MM-DD.",
                                "Error", JOptionPane.ERROR_MESSAGE);

                        return;
                    }

                    // Fetch course duration from DB
                    String selectedCourse = (String) courseSelectionBox.getSelectedItem();
                    int courseDuration = DBHandler.getCourseDuration(selectedCourse); // Implemented above

                    // Calculate graduation date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date startedDate = null;
                    try {
                        startedDate = sdf.parse(startedDateField.getText());
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(managementFrame, "Invalid start date.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Add months to startedDate to get graduationDate
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(startedDate);
                    cal.add(Calendar.MONTH, courseDuration);
                    Date graduationDate = cal.getTime();

                    // Add student via DBHandler
                    boolean success = DBHandler.addStudent(
                            nameField.getText().trim(),
                            surnameField.getText().trim(),
                            Integer.parseInt(ageField.getText().trim()),
                            (String) genderSelectionBox.getSelectedItem(), // Treated as String
                            selectedCourse,
                            startedDate,
                            graduationDate
                    );

                    if (success) {
                        JOptionPane.showMessageDialog(managementFrame, "Student successfully added.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        DBHandler.updateStudents();
                    } else {
                        JOptionPane.showMessageDialog(managementFrame, "Something went wrong while adding the student.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        buttonsPanel.setLayout(new GridLayout(0, 5, 0, 0));

        addButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        buttonsPanel.add(addButton);

        // The button to press to update an information in the table
        JButton updateButton = new JButton("Update"); // Use Translator.getValue("update") if defined

        // Actions to perform when "update" button clicked
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                table.clearSelection();
                DBHandler.updateStudents();
            }
        });

        updateButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);

        // The button to press to exit the application
        JButton exitButton = new JButton("Exit"); // Use Translator.getValue("exit") if defined
        exitButton.setName("exitButton");

        // The button that user have to press in order to disconnect from the current
        // database
        JButton disconnectButton = new JButton("Disconnect"); // Use Translator.getValue("disconnect") if defined
        disconnectButton.setName("disconnectButton");

        // Actions to perform when "disconnect" button has been clicked
        disconnectButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(managementFrame, "Are you sure you want to disconnect?",
                        "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    // Return back to the connection window
                    ConnectionView.main(null);
                    managementFrame.dispose();
                }
            }
        });

        disconnectButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        buttonsPanel.add(disconnectButton);

        // Actions to perform when "exit" button clicked
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(managementFrame, "Are you sure you want to exit?",
                        "Confirm Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    managementFrame.dispose();
                    System.exit(0);
                }
            }
        });

        JButton logoutButton = new JButton("Logout"); // You can use Translator.getValue("logout") if defined
        logoutButton.setName("logoutButton");
        logoutButton.setFont(new Font("Tahoma", Font.PLAIN, 16));

        // Action listener for Logout button
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(managementFrame,
                        "Are you sure you want to logout?", "Confirm Logout",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Close the ManagementView frame
                    managementFrame.dispose();

                    // Open the main selection view (ConnectionView)
                    ConnectionView.main(null);
                }
            }
        });

        // Add Logout button to the buttons panel
        buttonsPanel.add(logoutButton);

        exitButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        buttonsPanel.add(exitButton);

        // The panel where user writes information about a student
        JPanel studentPanel = new JPanel();
        studentPanel.setBorder(new LineBorder(SystemColor.textHighlight, 5));
        // Removed setting bounds here; we'll set it when adding to scroll pane
        studentPanel.setLayout(null);

        // The text that informs the user where they have to write the student's name
        JLabel nameText = new JLabel("Name:"); // Use Translator.getValue("name") if defined
        nameText.setFont(new Font("Tahoma", Font.PLAIN, 16));
        nameText.setBounds(10, 22, 67, 19);
        studentPanel.add(nameText);

        // Initializing name text field
        nameField = new JTextField();
        nameField.setName("nameField");
        nameField.setBounds(85, 23, 143, 22);
        studentPanel.add(nameField);
        nameField.setColumns(10);

        // The text that informs the user where they have to write the student's surname
        JLabel surnameText = new JLabel("Surname:"); // Use Translator.getValue("surname") if defined
        surnameText.setFont(new Font("Tahoma", Font.PLAIN, 16));
        surnameText.setBounds(10, 54, 67, 19);
        studentPanel.add(surnameText);

        // Initializing surname text field
        surnameField = new JTextField();
        surnameField.setName("surnameField");
        surnameField.setColumns(10);
        surnameField.setBounds(85, 51, 143, 22);
        studentPanel.add(surnameField);

        // The text that informs the user where they have to write the student's age
        JLabel ageText = new JLabel("Age:"); // Use Translator.getValue("age") if defined
        ageText.setFont(new Font("Tahoma", Font.PLAIN, 16));
        ageText.setBounds(10, 86, 67, 19);
        studentPanel.add(ageText);

        // Initializing age text field
        ageField = new JTextField();
        ageField.setName("ageField");
        ageField.setColumns(10);
        ageField.setBounds(85, 83, 143, 22);
        studentPanel.add(ageField);

        // The text that informs the user where they have to select student's gender
        JLabel genderText = new JLabel("Gender:"); // Use Translator.getValue("gender") if defined
        genderText.setFont(new Font("Tahoma", Font.PLAIN, 16));
        genderText.setBounds(10, 120, 67, 19);
        studentPanel.add(genderText);

        // Initialize the box where user selects the student's gender
        genderSelectionBox = new JComboBox<>();
        genderSelectionBox.setName("genderSelectionBox");
        genderSelectionBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
        genderSelectionBox.setModel(new DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        genderSelectionBox.setBounds(85, 120, 143, 22);
        studentPanel.add(genderSelectionBox);

        // The text that informs the user where they have to write the student's
        // attended course
        JLabel courseText = new JLabel("Course:"); // Use Translator.getValue("course") if defined
        courseText.setFont(new Font("Tahoma", Font.PLAIN, 16));
        courseText.setBounds(10, 156, 67, 19);
        studentPanel.add(courseText);

        // Initializing the course selection box
        courseSelectionBox = new JComboBox<>();
        courseSelectionBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
        courseSelectionBox.setBounds(85, 154, 143, 22);
        updateCourses();
        studentPanel.add(courseSelectionBox);

        // The text that informs the user where they have to write the date when student
        // started attending the course
        JLabel startedDateText = new JLabel("Started:"); // Use Translator.getValue("started") if defined
        startedDateText.setFont(new Font("Tahoma", Font.PLAIN, 16));
        startedDateText.setBounds(10, 188, 67, 19);
        studentPanel.add(startedDateText);

        // Initializing startedDate text field
        startedDateField = new JTextField();
        startedDateField.setName("startedDateField");
        startedDateField.setColumns(10);
        startedDateField.setBounds(85, 185, 143, 22);
        startedDateField.setText("YYYY-MM-DD"); // Use Translator.getValue("dateFormat") if defined
        studentPanel.add(startedDateField);

        // Button that adds a new faculty
        JButton addFacultyButton = new JButton("Add Faculty"); // Use Translator.getValue("addFaculty") if defined
        addFacultyButton.setName("addFacultyButton");

        // Actions to perform when "add faculty" button clicked
        addFacultyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String facultyName = "";

                facultyName = JOptionPane.showInputDialog(managementFrame, "Enter Faculty Name:"); // Use Translator

                if (facultyName == null || facultyName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(managementFrame, "Faculty name cannot be empty.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (DBHandler.checkIfElementExists(DBHandler.getFacultiesTable(), facultyName)) {
                        JOptionPane.showMessageDialog(managementFrame, "Faculty already exists.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (DBHandler.addFaculty(facultyName)) {
                            JOptionPane.showMessageDialog(managementFrame,
                                    "Faculty successfully added.", "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                            // The credentials are already shown in DBHandler.addFaculty method
                        } else {
                            JOptionPane.showMessageDialog(managementFrame, "Faculty not added.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        addFacultyButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        addFacultyButton.setBounds(10, 220, 220, 30);
        studentPanel.add(addFacultyButton);

        // Button that adds a new course
        JButton addCourseButton = new JButton("Add Course"); // Use Translator.getValue("addCourse") if defined
        addCourseButton.setName("addCourseButton");
        addCourseButton.addActionListener(new ActionListener() {

            // Actions to perform when "add course" button clicked
            public void actionPerformed(ActionEvent e) {
                // If there are no faculties there is no way to add a course
                if (DBHandler.getFacultiesList().isEmpty()) {
                    JOptionPane.showMessageDialog(managementFrame, "Cannot add course. No faculties available.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String courseName = "", faculty = "";
                int duration = 0;

                courseName = JOptionPane.showInputDialog(managementFrame, "Enter Course Name:"); // Use Translator

                // If no name has been written for the course
                if (courseName == null || courseName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(managementFrame, "Course name cannot be empty.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    List<String> facultiesList = DBHandler.getFacultiesList();
                    faculty = (String) JOptionPane.showInputDialog(null, "Select Faculty:",
                            "Course Faculty", JOptionPane.QUESTION_MESSAGE, null,
                            facultiesList.toArray(), facultiesList.get(0));

                    // If no faculty has been selected for the course
                    if (faculty == null || faculty.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(managementFrame, "Course not added. No faculty selected.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        // In case the user types letters for the duration
                        try {
                            String durationStr = JOptionPane.showInputDialog(managementFrame,
                                    "Enter Course Duration (in months):"); // Use Translator
                            if (durationStr == null || durationStr.trim().isEmpty()) {
                                throw new NumberFormatException("Duration cannot be empty.");
                            }
                            duration = Integer.parseInt(durationStr.trim());
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();

                            JOptionPane.showMessageDialog(managementFrame,
                                    "Invalid duration entered.", "Error",
                                    JOptionPane.ERROR_MESSAGE);

                            return;
                        }

                        if (DBHandler.checkIfElementExists(DBHandler.getCoursesTable(), courseName)) {
                            JOptionPane.showMessageDialog(managementFrame, "Course already exists.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (DBHandler.addCourse(courseName, faculty, duration)) {
                                JOptionPane.showMessageDialog(managementFrame,
                                        "Course successfully added.", "Success",
                                        JOptionPane.INFORMATION_MESSAGE);

                                updateCourses();
                            } else {
                                JOptionPane.showMessageDialog(managementFrame, "Course not added.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        addCourseButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        addCourseButton.setBounds(10, 260, 220, 30);
        studentPanel.add(addCourseButton);

        // Button that allows to delete a faculty
        JButton deleteFacultyButton = new JButton("Delete Faculty"); // Use Translator.getValue("deleteFaculty") if defined
        deleteFacultyButton.setName("deleteFacultyButton");

        // Actions to perform when "Delete Faculty" button clicked
        deleteFacultyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                table.clearSelection();

                List<String> facultiesList = DBHandler.getFacultiesList();
                if (facultiesList.isEmpty()) {
                    JOptionPane.showMessageDialog(managementFrame, "No faculties available to delete.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String faculty = (String) JOptionPane.showInputDialog(null, "Select Faculty to Delete:",
                        "Delete Faculty", JOptionPane.QUESTION_MESSAGE, null, facultiesList.toArray(),
                        facultiesList.get(0));

                // If no faculty has been selected
                if (faculty == null) {
                    return;
                }

                // If there are courses in this faculty
                // Assuming number of courses equals the 'Courses' field in faculties table
                int numberOfCourses = DBHandler.getNumberOfCourses(faculty);
                if (numberOfCourses > 0) {
                    if (JOptionPane.showConfirmDialog(managementFrame, "Deleting this faculty will also delete its courses and associated students. Proceed?",
                            "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        if (DBHandler.deleteFacultyCourses(faculty)) {
                            JOptionPane.showMessageDialog(managementFrame,
                                    "Courses and associated students successfully deleted.",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);

                            if (DBHandler.deleteFaculty(faculty)) {
                                JOptionPane.showMessageDialog(managementFrame, "Faculty successfully deleted.",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(managementFrame,
                                        "Failed to delete faculty. Please try again.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }

                        } else {
                            JOptionPane.showMessageDialog(managementFrame,
                                    "Failed to delete courses. Please try again.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    if (DBHandler.deleteFaculty(faculty)) {
                        JOptionPane.showMessageDialog(managementFrame, "Faculty successfully deleted.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(managementFrame, "Failed to delete faculty. Please try again.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                updateCourses();
            }
        });

        deleteFacultyButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        deleteFacultyButton.setBounds(10, 300, 220, 30);
        studentPanel.add(deleteFacultyButton);

        // Button that allows to delete a course
        JButton deleteCourseButton = new JButton("Delete Course"); // Use Translator.getValue("deleteCourse") if defined
        deleteCourseButton.setName("deleteCourseButton");

        // Actions to perform when "Delete Course" button clicked
        deleteCourseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                table.clearSelection();

                List<String> coursesList = DBHandler.getCoursesByFacultyName("dummyFacultyUsername"); // Replace with actual facultyUsername if needed
                if (coursesList.isEmpty()) {
                    JOptionPane.showMessageDialog(managementFrame, "No courses available to delete.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String course = (String) JOptionPane.showInputDialog(null, "Select Course to Delete:",
                        "Delete Course", JOptionPane.QUESTION_MESSAGE, null, DBHandler.getCourses(),
                        DBHandler.getCourses()[0]);

                // If no course has been selected
                if (course == null) {
                    return;
                }

                // If there are students attending the course
                int numberOfAttendees = DBHandler.getNumberOfAttendees(DBHandler.getCoursesTable(), course);
                if (numberOfAttendees > 0) {
                    if (JOptionPane.showConfirmDialog(managementFrame, "Deleting this course will also delete its associated students. Proceed?",
                            "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        if (DBHandler.deleteCourseAttendees(course)) {
                            JOptionPane.showMessageDialog(managementFrame,
                                    "Students attending the course successfully deleted.",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);

                            if (DBHandler.deleteCourse(course)) {
                                JOptionPane.showMessageDialog(managementFrame, "Course successfully deleted.",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(managementFrame,
                                        "Failed to delete course. Please try again.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(managementFrame,
                                    "Failed to delete students. Please try again.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    if (DBHandler.deleteCourse(course)) {
                        JOptionPane.showMessageDialog(managementFrame, "Course successfully deleted.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(managementFrame, "Failed to delete course. Please try again.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                updateCourses();
            }
        });

        deleteCourseButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        deleteCourseButton.setBounds(10, 340, 220, 30);
        studentPanel.add(deleteCourseButton);

        // Wrap studentPanel in a JScrollPane
        JScrollPane studentScrollPane = new JScrollPane(studentPanel);
        studentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        studentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        studentScrollPane.setBounds(10, 10, 240, 395);
        managementFrame.getContentPane().add(studentScrollPane);
    }
}
