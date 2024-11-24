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
