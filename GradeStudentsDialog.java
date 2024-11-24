package sms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Arrays;
public class GradeStudentsDialog extends JDialog {
    private JComboBox<String> facultyComboBox;
    private JComboBox<String> courseComboBox;
    private JButton nextButton;
    private JPanel contentPanel;
    private ManagementView parentView;
public GradeStudentsDialog(ManagementView parentView) {
        super(ManagementView.managementFrame, "Grade Students", true);
        this.parentView = parentView;
        initialize();
    }
 private void initialize() {
        setSize(400, 200);
        setLocationRelativeTo(ManagementView.managementFrame);
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        add(contentPanel);
 JLabel facultyLabel = new JLabel("Select Faculty:");
        facultyLabel.setBounds(20, 20, 100, 25);
        contentPanel.add(facultyLabel);

        facultyComboBox = new JComboBox<>();
        facultyComboBox.setBounds(130, 20, 200, 25);
        contentPanel.add(facultyComboBox);
JLabel courseLabel = new JLabel("Select Course:");
        courseLabel.setBounds(20, 60, 100, 25);
        contentPanel.add(courseLabel);

        courseComboBox = new JComboBox<>();
        courseComboBox.setBounds(130, 60, 200, 25);
        contentPanel.add(courseComboBox);
        nextButton = new JButton("Next");
        nextButton.setBounds(150, 120, 80, 30);
        contentPanel.add(nextButton);

        // Load faculties
        List<String> faculties = Arrays.asList(DBHandler.getFaculties());
        if (faculties.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No faculties available.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        for (String faculty : faculties) {
            facultyComboBox.addItem(faculty);
        }

        // Add action listener to facultyComboBox
        facultyComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadCourses();
            }
        });
        // Add action listener to nextButton
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                proceedToGrading();
            }
        });

        // Initialize courses
        if (facultyComboBox.getItemCount() > 0) {
            facultyComboBox.setSelectedIndex(0);
        }
        loadCourses();
    }
    private void loadCourses() {
        courseComboBox.removeAllItems();
        String selectedFaculty = (String) facultyComboBox.getSelectedItem();
        if (selectedFaculty != null) {
            List<String> courses = DBHandler.getCoursesByFaculty(selectedFaculty);
            if (courses.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No courses available for the selected faculty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
