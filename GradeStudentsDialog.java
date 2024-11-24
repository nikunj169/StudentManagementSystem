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
