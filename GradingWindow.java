package sms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
public class GradingWindow extends JFrame {
    private String courseName;
    private int courseId;
    private JPanel contentPanel;
    private JTable studentsTable;
    private JButton saveButton;
    private MarkingScheme markingScheme;
    private List<Student> students;
    private ManagementView parentView;
    public GradingWindow(ManagementView parentView, Frame parent, String courseName, int courseId, List<Student> students) {
        super("Grading - " + courseName);
        this.parentView = parentView;
        this.courseName = courseName;
        this.courseId = courseId;
        this.students = students;
        initialize();
        setLocationRelativeTo(parent);
    }
   private void initialize() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        setContentPane(contentPanel);
        // Check if marking scheme exists
        markingScheme = DBHandler.getMarkingSchemeByCourseId(courseId);
        if (markingScheme == null) {
            // Prompt to set marking scheme
            setMarkingScheme();
            if (markingScheme == null) {
                // User canceled marking scheme setup
                dispose();
                return;
            }
        }
       // Create table model
        String[] columnNames = {"Student ID", "Name", "Surname", "Marks"};
        Object[][] data = new Object[students.size()][4];
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            data[i][0] = s.getId();
            data[i][1] = s.getName();
            data[i][2] = s.getSurname();
            data[i][3] = ""; // Marks input
        }
       studentsTable = new JTable(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only Marks column is editable
            }
        };
        JScrollPane scrollPane = new JScrollPane(studentsTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        // Save button
        saveButton = new JButton("Save Grades");
        contentPanel.add(saveButton, BorderLayout.SOUTH);
        // Add action listener
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveGrades();
            }
        });
    }
    private void setMarkingScheme() {
        MarkingSchemeDialog dialog = new MarkingSchemeDialog(this, courseId);
        dialog.setVisible(true);
        markingScheme = dialog.getMarkingScheme();
        if (markingScheme == null) {
            JOptionPane.showMessageDialog(this, "Marking scheme is required.");
            dispose();
        }
    }
    private void saveGrades() {
        int rowCount = studentsTable.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            int studentId = (int) studentsTable.getValueAt(i, 0);
            String marksStr = studentsTable.getValueAt(i, 3).toString();
            if (marksStr == null || marksStr.isEmpty()) {
                continue; // Skip if no marks entered
            }
            double marks = Double.parseDouble(marksStr);
            String grade = calculateGrade(marks);
            Grade gradeObj = new Grade(studentId, courseId, marks, grade);
            DBHandler.saveGrade(gradeObj);
            // Update student's grade
            DBHandler.updateStudentGrade(studentId, grade);
        }
        JOptionPane.showMessageDialog(this, "Grades saved successfully.");
        // Refresh the main table
        if (parentView != null) {
            parentView.refreshStudentTable();
        }
        dispose();
    }
