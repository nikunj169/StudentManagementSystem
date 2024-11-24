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
