package sms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * The attendance screen where faculty can mark students as present or absent.
 */
public class TakeAttendanceView {
    static JFrame attendanceFrame;
    private JTextField studentIdField;
    private JTextField studentNameField;
    private JTextField dateField;
    private JComboBox<String> attendanceStatusBox;
    private FacultyHomePage facultyHomePage;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TakeAttendanceView window = new TakeAttendanceView();
                    window.attendanceFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public TakeAttendanceView() {
        // Default constructor used when called from main method
        initialize();
    }

    /**
     * Create the application with FacultyHomePage reference.
     */
    public TakeAttendanceView(FacultyHomePage facultyHomePage) {
        this.facultyHomePage = facultyHomePage;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        attendanceFrame = new JFrame();
        attendanceFrame.setBounds(100, 100, 500, 400);
        attendanceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        attendanceFrame.setTitle("Take Attendance");
        attendanceFrame.getContentPane().setLayout(null);

        JLabel lblStudentId = new JLabel("Student ID:");
        lblStudentId.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblStudentId.setBounds(50, 50, 100, 25);
        attendanceFrame.getContentPane().add(lblStudentId);

        studentIdField = new JTextField();
        studentIdField.setBounds(160, 50, 200, 25);
        attendanceFrame.getContentPane().add(studentIdField);
        studentIdField.setColumns(10);

        JLabel lblStudentName = new JLabel("Student Name:");
        lblStudentName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblStudentName.setBounds(50, 90, 120, 25);
        attendanceFrame.getContentPane().add(lblStudentName);

        studentNameField = new JTextField();
        studentNameField.setBounds(160, 90, 200, 25);
        attendanceFrame.getContentPane().add(studentNameField);
        studentNameField.setColumns(10);
        studentNameField.setEditable(false);

        JLabel lblDate = new JLabel("Date (YYYY-MM-DD):");
        lblDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblDate.setBounds(50, 130, 160, 25);
        attendanceFrame.getContentPane().add(lblDate);

        dateField = new JTextField();
        dateField.setBounds(220, 130, 140, 25);
        attendanceFrame.getContentPane().add(dateField);
        dateField.setColumns(10);
        dateField.setText("YYYY-MM-DD"); // Placeholder text

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblStatus.setBounds(50, 170, 100, 25);
        attendanceFrame.getContentPane().add(lblStatus);

        attendanceStatusBox = new JComboBox<>();
        attendanceStatusBox.setModel(new DefaultComboBoxModel<>(new String[] { "Present", "Absent" }));
        attendanceStatusBox.setBounds(160, 170, 200, 25);
        attendanceFrame.getContentPane().add(attendanceStatusBox);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnSubmit.setBounds(160, 220, 100, 30);
        attendanceFrame.getContentPane().add(btnSubmit);
        
        // Back Button
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnBack.setBounds(270, 220, 90, 30);
        attendanceFrame.getContentPane().add(btnBack);

        // Action Listener for Submit Button
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String studentId = studentIdField.getText().trim();
                String studentName = studentNameField.getText().trim();
                String date = dateField.getText().trim();
                String status = (String) attendanceStatusBox.getSelectedItem();

                if (studentId.isEmpty() || studentName.isEmpty() || date.isEmpty()) {
                    JOptionPane.showMessageDialog(attendanceFrame, "Please fill in all fields.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validate Date Format
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(attendanceFrame, "Invalid date format. Use YYYY-MM-DD.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Call the C++ Attendance Processor
                try {
                    // Path to the C++ executable (update as per your environment)
                    String cppExecutable = "F:\\PROJECT-CPP-JAVA\\student-management-system-master (1)\\student-management-system-master\\AttendanceProcessor.exe";

                    // Command with arguments
                    String[] command = { cppExecutable, studentId, date, status };

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
                        JOptionPane.showMessageDialog(attendanceFrame, "Attendance Processed:\n" + output.toString(),
                                "Success", JOptionPane.INFORMATION_MESSAGE);

                        // Update the database with attendance
                        DBHandler.updateAttendance(Integer.parseInt(studentId), date, status);

                        // Refresh the Faculty Home Page to display updated attendance
                        if (facultyHomePage != null) {
                            facultyHomePage.refreshStudentTable();
                        }

                        // Clear the input fields
                        studentIdField.setText("");
                        studentNameField.setText("");
                        dateField.setText("YYYY-MM-DD");
                    } else {
                        // Failure
                        JOptionPane.showMessageDialog(attendanceFrame, "Error processing attendance:\n" + output.toString(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(attendanceFrame, "Error processing attendance.",
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
                attendanceFrame.dispose();
            }
        });


    }
}
