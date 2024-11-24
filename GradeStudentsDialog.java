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
