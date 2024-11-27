package sms;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * The home page for Faculty after successful login.
 * Displays students associated with the faculty's courses and provides action buttons.
 */
public class FacultyHomePage {
    JFrame facultyHomeFrame;
    private JTable studentsTable;
    private String facultyUsername;

    /**
     * Launch the application.
     * @param args Pass the faculty's username as args[0].
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    String facultyUsername = "";
                    if (args.length > 0) {
                        facultyUsername = args[0];
                    }
                    FacultyHomePage window = new FacultyHomePage(facultyUsername);
                    window.facultyHomeFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     * @param facultyUsername The username of the logged-in faculty.
     */
    public FacultyHomePage(String facultyUsername) {
        this.facultyUsername = facultyUsername;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        facultyHomeFrame = new JFrame();
        facultyHomeFrame.setBounds(100, 100, 860, 540);
        facultyHomeFrame.setResizable(false);
        facultyHomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        facultyHomeFrame.setTitle("Faculty Home Page");
        facultyHomeFrame.getContentPane().setLayout(null);

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(SystemColor.textHighlight);
        topPanel.setBounds(0, 0, 844, 60);
        facultyHomeFrame.getContentPane().add(topPanel);
        topPanel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + facultyUsername);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        welcomeLabel.setBounds(20, 10, 400, 40);
        topPanel.add(welcomeLabel);

        // Scroll Pane for Table
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 80, 800, 300);
        facultyHomeFrame.getContentPane().add(scrollPane);

        // Table Model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[] { "ID", "Name", "Surname", "Age", "Gender", "Course", "Started",
                "Graduation", "Grade" });

        // JTable
        studentsTable = new JTable(tableModel);
        studentsTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
        studentsTable.setRowHeight(20);
        scrollPane.setViewportView(studentsTable);

        // Fetch and display students
        loadFacultyStudents(tableModel);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(new LineBorder(new Color(0, 120, 215), 5));
        buttonsPanel.setBackground(UIManager.getColor("Button.background"));
        buttonsPanel.setBounds(20, 400, 800, 80);
        facultyHomeFrame.getContentPane().add(buttonsPanel);

        // Set FlowLayout with horizontal and vertical gaps
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Take Attendance Button
        JButton takeAttendanceButton = new JButton("Take Attendance");
        takeAttendanceButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        takeAttendanceButton.setPreferredSize(new Dimension(200, 40));
        buttonsPanel.add(takeAttendanceButton);

        // Grade Students Button
        JButton gradeStudentsButton = new JButton("Grade Students");
        gradeStudentsButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gradeStudentsButton.setPreferredSize(new Dimension(200, 40));
        buttonsPanel.add(gradeStudentsButton);

        // Download Reports Button
        JButton downloadReportsButton = new JButton("Download Reports");
        downloadReportsButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        downloadReportsButton.setPreferredSize(new Dimension(200, 40));
        buttonsPanel.add(downloadReportsButton);

        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        logoutButton.setPreferredSize(new Dimension(130, 40));
        buttonsPanel.add(logoutButton);

        // Action Listener for Take Attendance Button
        takeAttendanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the Take Attendance screen and pass 'this' reference
                TakeAttendanceView attendanceView = new TakeAttendanceView(FacultyHomePage.this);
                attendanceView.attendanceFrame.setVisible(true);
                facultyHomeFrame.dispose();
            }
        });

        // Action Listener for Grade Students Button
        gradeStudentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the Grade Students screen and pass 'this' reference
                GradeStudentsView gradeView = new GradeStudentsView(FacultyHomePage.this);
                gradeView.gradeFrame.setVisible(true);
                facultyHomeFrame.dispose();
            }
        });

        // Action Listener for Download Reports Button
        downloadReportsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the Download Reports screen (dummy page)
                DownloadReportsView.main(null);
                // Optionally, keep the home page open
            }
        });

        // Action Listener for Logout Button
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(facultyHomeFrame,
                        "Are you sure you want to logout?", "Confirm Logout",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Close the FacultyHomePage frame
                    facultyHomeFrame.dispose();

                    // Open the main selection view (e.g., MainSelectionView)
                    MainSelectionView.main(null);
                }
            }
        });
    }

    /**
     * Fetches and loads the students associated with the faculty into the table.
     * 
     * @param tableModel The table model to populate.
     */
    private void loadFacultyStudents(DefaultTableModel tableModel) {
        // Fetch students from the database based on facultyUsername
        List<Student> students = DBHandler.getStudentsByFaculty(facultyUsername);

        if (students != null && !students.isEmpty()) {
            for (Student student : students) {
                tableModel.addRow(new Object[] { 
                    student.getId(), 
                    student.getName(), 
                    student.getSurname(),
                    student.getAge(), 
                    student.getGender(), 
                    student.getCourse(), 
                    student.getStarted(),
                    student.getGraduation(), 
                    student.getGrade() 
                });
            }
        } else {
            JOptionPane.showMessageDialog(facultyHomeFrame, "No students found.",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Refreshes the student table with the latest data.
     * Should be called after grading or attendance updates.
     */
    public void refreshStudentTable() {
        try {
            DefaultTableModel tableModel = (DefaultTableModel) studentsTable.getModel();
            tableModel.setRowCount(0); // Clear existing rows
            loadFacultyStudents(tableModel); // Reload students
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
