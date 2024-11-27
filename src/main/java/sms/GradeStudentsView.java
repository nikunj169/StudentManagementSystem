package sms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * The grading screen where faculty can assign grades to students.
 */
public class GradeStudentsView {
    static JFrame gradeFrame;
    private JComboBox<String> studentIdBox;
    private JTextField studentNameField;
    private JTextField gradeField;
    private FacultyHomePage facultyHomePage;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GradeStudentsView window = new GradeStudentsView();
                    window.gradeFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public GradeStudentsView() {
        // Default constructor used when called from main method
        initialize();
    }

    /**
     * Create the application with FacultyHomePage reference.
     */
    public GradeStudentsView(FacultyHomePage facultyHomePage) {
        this.facultyHomePage = facultyHomePage;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        gradeFrame = new JFrame();
        gradeFrame.setBounds(100, 100, 500, 400);
        gradeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gradeFrame.setTitle("Grade Students");
        gradeFrame.getContentPane().setLayout(null);

        JLabel lblStudentId = new JLabel("Select Student ID:");
        lblStudentId.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblStudentId.setBounds(50, 50, 150, 25);
        gradeFrame.getContentPane().add(lblStudentId);

        studentIdBox = new JComboBox<>();
        populateStudentIds();
        studentIdBox.setBounds(220, 50, 200, 25);
        gradeFrame.getContentPane().add(studentIdBox);

        JLabel lblStudentName = new JLabel("Student Name:");
        lblStudentName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblStudentName.setBounds(50, 100, 150, 25);
        gradeFrame.getContentPane().add(lblStudentName);

        studentNameField = new JTextField();
        studentNameField.setBounds(220, 100, 200, 25);
        gradeFrame.getContentPane().add(studentNameField);
        studentNameField.setColumns(10);
        studentNameField.setEditable(false);

        JLabel lblGrade = new JLabel("Grade (A, A-, B+, B, B-, C, D, F):");
        lblGrade.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblGrade.setBounds(50, 150, 220, 25);
        gradeFrame.getContentPane().add(lblGrade);

        gradeField = new JTextField();
        gradeField.setBounds(280, 150, 140, 25);
        gradeFrame.getContentPane().add(gradeField);
        gradeField.setColumns(10);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnSubmit.setBounds(150, 220, 100, 30);
        gradeFrame.getContentPane().add(btnSubmit);
        
        // Back Button
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnBack.setBounds(270, 220, 90, 30);
        gradeFrame.getContentPane().add(btnBack);

        // Action Listener for Submit Button
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedId = (String) studentIdBox.getSelectedItem();
                String grade = gradeField.getText().trim();

                if (selectedId == null || selectedId.isEmpty() || grade.isEmpty()) {
                    JOptionPane.showMessageDialog(gradeFrame, "Please select a student and enter a grade.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validate Grade Format
                if (!grade.matches("A-|A|B\\+|B|B-|C|D|F")) {
                    JOptionPane.showMessageDialog(gradeFrame, "Invalid grade format. Use A, A-, B+, B, B-, C, D, or F.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Call the C++ Grade Processor
                try {
                    // Path to the C++ executable
                    String cppExecutable = "F:\\PROJECT-CPP-JAVA\\student-management-system-master (1)\\student-management-system-master\\GradeProcessor.exe";

                    // Command with arguments
                    String[] command = { cppExecutable, selectedId, grade };

                    // Create a ProcessBuilder instance
                    ProcessBuilder pb = new ProcessBuilder(command);
                    pb.redirectErrorStream(true); // Merge error and output streams

                    // Start the process
                    Process process = pb.start();

                    // Read the output from C++ program
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    StringBuilder output = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }

                    // Wait for the process to complete
                    int exitCode = process.waitFor();

                    reader.close();

                    if (exitCode == 0) {
                        // Success
                        JOptionPane.showMessageDialog(gradeFrame, "Grade Processed:\n" + output.toString(),
                                "Success", JOptionPane.INFORMATION_MESSAGE);

                        // Update the database with the grade
                        boolean isUpdated = DBHandler.updateStudentGrade(Integer.parseInt(selectedId), grade);
                        if (isUpdated) {
                            JOptionPane.showMessageDialog(gradeFrame, "Grade updated in the database successfully.",
                                    "Database Update", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(gradeFrame, "Failed to update grade in the database.",
                                    "Database Update Error", JOptionPane.ERROR_MESSAGE);
                        }

                        // Refresh the Faculty Home Page to display updated grades
                        if (facultyHomePage != null) {
                            facultyHomePage.refreshStudentTable();
                        }

                        // Clear the input fields
                        gradeField.setText("");
                    } else {
                        // Failure
                        JOptionPane.showMessageDialog(gradeFrame, "Error processing grade:\n" + output.toString(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(gradeFrame, "Error processing grade.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Action Listener for Back Button
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open FacultyHomePage and close this frame
                if (facultyHomePage != null) {
                    facultyHomePage.facultyHomeFrame.setVisible(true);
                }
                gradeFrame.dispose();
            }
        });

        // Listener to populate student name when a student ID is selected
        studentIdBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedId = (String) studentIdBox.getSelectedItem();
                if (selectedId != null && !selectedId.isEmpty()) {
                    Student student = DBHandler.getStudentById(Integer.parseInt(selectedId));
                    if (student != null) {
                        studentNameField.setText(student.getName() + " " + student.getSurname());
                    } else {
                        studentNameField.setText("");
                    }
                } else {
                    studentNameField.setText("");
                }
            }
        });
    }

    /**
     * Populates the student ID combo box with IDs from the database.
     */
    private void populateStudentIds() {
        String[] studentIds = DBHandler.getStudentIds();
        for (String id : studentIds) {
            studentIdBox.addItem(id);
        }
    }
}
