package sms;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class FacultyCourseSelectionView extends JFrame {
    private String facultyUsername;
    private JComboBox<String> courseComboBox;
    private JButton proceedButton;

    public FacultyCourseSelectionView(String facultyUsername) {
        this.facultyUsername = facultyUsername;
        initialize();
    }

    private void initialize() {
        setTitle("Select Course");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel selectCourseLabel = new JLabel("Select Course:");
        selectCourseLabel.setBounds(50, 50, 100, 25);
        add(selectCourseLabel);

        courseComboBox = new JComboBox<>();
        courseComboBox.setBounds(160, 50, 180, 25);
        add(courseComboBox);

        // Load courses for the faculty
        List<String> courses = DBHandler.getCoursesByFacultyName(facultyUsername);
        for (String course : courses) {
            courseComboBox.addItem(course);
        }

        proceedButton = new JButton("Proceed");
        proceedButton.setBounds(150, 100, 100, 30);
        add(proceedButton);

        proceedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCourse = (String) courseComboBox.getSelectedItem();
                if (selectedCourse != null) {
                    FacultyStudentView facultyStudentView = new FacultyStudentView(selectedCourse);
                    facultyStudentView.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(FacultyCourseSelectionView.this, "Please select a course.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
