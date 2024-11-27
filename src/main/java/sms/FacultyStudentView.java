package sms;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FacultyStudentView extends JFrame {
    private String courseName;
    private JTable studentTable;

    public FacultyStudentView(String courseName) {
        this.courseName = courseName;
        initialize();
    }

    private void initialize() {
        setTitle("Students in " + courseName);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        List<Student> students = DBHandler.getStudentsByCourse(courseName);

        String[] columnNames = {"ID", "Name", "Surname", "Age", "Gender", "Started", "Graduation", "Grade"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Student s : students) {
            Object[] rowData = {
                s.getId(),
                s.getName(),
                s.getSurname(),
                s.getAge(),
                s.getGender(),
                s.getStarted(),
                s.getGraduation(),
                s.getGrade()
            };
            tableModel.addRow(rowData);
        }

        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
}
