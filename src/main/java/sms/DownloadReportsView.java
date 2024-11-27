package sms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;

public class DownloadReportsView {
    static JFrame downloadFrame;
    private JButton btnDownloadAttendance;
    private JButton btnDownloadGrades;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DownloadReportsView window = new DownloadReportsView();
                    window.downloadFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public DownloadReportsView() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        downloadFrame = new JFrame();
        downloadFrame.setBounds(100, 100, 400, 200);
        downloadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        downloadFrame.setTitle("Download Reports");
        downloadFrame.getContentPane().setLayout(null);

        btnDownloadAttendance = new JButton("Download Attendance Report");
        btnDownloadAttendance.setBounds(50, 30, 300, 40);
        downloadFrame.getContentPane().add(btnDownloadAttendance);

        btnDownloadGrades = new JButton("Download Grades Report");
        btnDownloadGrades.setBounds(50, 100, 300, 40);
        downloadFrame.getContentPane().add(btnDownloadGrades);

        // Action Listener for Attendance Report
        btnDownloadAttendance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String facultyUsername = JOptionPane.showInputDialog(downloadFrame, "Enter Faculty Username:");
                if (facultyUsername != null && !facultyUsername.trim().isEmpty()) {
                    List<Student> students = DBHandler.getStudentsByFaculty(facultyUsername);
                    if (students.isEmpty()) {
                        JOptionPane.showMessageDialog(downloadFrame, "No students found for this faculty.",
                                "Info", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    generateAttendancePDF(students, facultyUsername);
                } else {
                    JOptionPane.showMessageDialog(downloadFrame, "Faculty username is required.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action Listener for Grades Report
        btnDownloadGrades.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String facultyUsername = JOptionPane.showInputDialog(downloadFrame, "Enter Faculty Username:");
                if (facultyUsername != null && !facultyUsername.trim().isEmpty()) {
                    List<Student> students = DBHandler.getStudentsByFaculty(facultyUsername);
                    if (students.isEmpty()) {
                        JOptionPane.showMessageDialog(downloadFrame, "No students found for this faculty.",
                                "Info", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    generateGradesPDF(students, facultyUsername);
                } else {
                    JOptionPane.showMessageDialog(downloadFrame, "Faculty username is required.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Generates the Attendance Report PDF.
     */
    private void generateAttendancePDF(List<Student> students, String facultyUsername) {
        // Fetch faculty and course details
        // Assuming one faculty manages multiple courses
        // You might need to adjust based on your schema

        // Initialize PDF Document
        PDDocument document = new PDDocument();
        try {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);
            PDPageContentStream content = new PDPageContentStream(document, page);

            // Fonts
            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDFont fontRegular = PDType1Font.HELVETICA;

            // Title
            content.beginText();
            content.setFont(font, 18);
            content.newLineAtOffset(50, 750);
            content.showText("Attendance Report");
            content.endText();

            // Faculty Info
            content.beginText();
            content.setFont(fontRegular, 12);
            content.newLineAtOffset(50, 720);
            content.showText("Faculty Username: " + facultyUsername);
            content.endText();

            // Table Headers
            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, 700);
            content.showText(String.format("%-20s %-20s %-15s %-20s %-20s", "Subject", "Professor", "Student ID", "Name", "Attendance (%)"));
            content.endText();

            // Fetch data for each student
            int yPosition = 680;
            for (Student student : students) {
                String subject = student.getCourse();
                String professor = getProfessorNameByFaculty(facultyUsername); // Implement this method
                int studentId = student.getId();
                String name = student.getName() + " " + student.getSurname();
                double attendancePercentage = calculateAttendancePercentage(student, subject);

                content.beginText();
                content.setFont(fontRegular, 12);
                content.newLineAtOffset(50, yPosition);
                content.showText(String.format("%-20s %-20s %-15d %-20s %-20.2f", subject, professor, studentId, name, attendancePercentage));
                content.endText();

                yPosition -= 20;
                if (yPosition < 50) {
                    content.close();
                    page = new PDPage(PDRectangle.LETTER);
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    yPosition = 750;
                }
            }

            content.close();

            // Save the PDF
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Attendance Report");
            int userSelection = fileChooser.showSaveDialog(downloadFrame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                document.save(fileToSave.getAbsolutePath() + ".pdf");
                JOptionPane.showMessageDialog(downloadFrame, "Attendance report saved successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(downloadFrame, "Error generating PDF.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Generates the Grades Report PDF.
     */
    private void generateGradesPDF(List<Student> students, String facultyUsername) {
        // Fetch faculty and course details
        // Initialize PDF Document
        PDDocument document = new PDDocument();
        try {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);
            PDPageContentStream content = new PDPageContentStream(document, page);

            // Fonts
            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDFont fontRegular = PDType1Font.HELVETICA;

            // Title
            content.beginText();
            content.setFont(font, 18);
            content.newLineAtOffset(50, 750);
            content.showText("Grades Report");
            content.endText();

            // Faculty Info
            content.beginText();
            content.setFont(fontRegular, 12);
            content.newLineAtOffset(50, 720);
            content.showText("Faculty Username: " + facultyUsername);
            content.endText();

            // Table Headers
            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, 700);
            content.showText(String.format("%-20s %-20s %-15s %-20s %-10s", "Subject", "Professor", "Student ID", "Name", "Grade"));
            content.endText();

            // Fetch data for each student
            int yPosition = 680;
            for (Student student : students) {
                String subject = student.getCourse();
                String professor = getProfessorNameByFaculty(facultyUsername); // Implement this method
                int studentId = student.getId();
                String name = student.getName() + " " + student.getSurname();
                String grade = student.getGrade();

                content.beginText();
                content.setFont(fontRegular, 12);
                content.newLineAtOffset(50, yPosition);
                content.showText(String.format("%-20s %-20s %-15d %-20s %-10s", subject, professor, studentId, name, grade));
                content.endText();

                yPosition -= 20;
                if (yPosition < 50) {
                    content.close();
                    page = new PDPage(PDRectangle.LETTER);
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    yPosition = 750;
                }
            }

            content.close();

            // Save the PDF
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Grades Report");
            int userSelection = fileChooser.showSaveDialog(downloadFrame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                document.save(fileToSave.getAbsolutePath() + ".pdf");
                JOptionPane.showMessageDialog(downloadFrame, "Grades report saved successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(downloadFrame, "Error generating PDF.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves the professor's name based on faculty username.
     * Implement this method based on your database schema.
     */
    private String getProfessorNameByFaculty(String facultyUsername) {
        // Example implementation:
        String professorName = "";
        String sql = "SELECT Name FROM faculties WHERE Username = ?";
        try (PreparedStatement pstmt = DBHandler.getConnection().prepareStatement(sql);) {
            pstmt.setString(1, facultyUsername);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                professorName = rs.getString("Name");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professorName;
    }

    /**
     * Calculates the attendance percentage for a student.
     */
    private double calculateAttendancePercentage(Student student, String courseName) {
        // Get course start date
        java.sql.Date startDate = new java.sql.Date(student.getStarted().getTime());
        LocalDate start = startDate.toLocalDate();
        LocalDate today = LocalDate.now();

        // Calculate total working days excluding Saturdays and Sundays
        long totalDays = ChronoUnit.DAYS.between(start, today) + 1;
        long workingDays = 0;
        for (long i = 0; i < totalDays; i++) {
            DayOfWeek day = start.plusDays(i).getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                workingDays++;
            }
        }

        // Get total present days from attendance table
        String sql = "SELECT COUNT(*) AS PresentCount FROM attendance WHERE student_id = ? AND status = 'Present' AND date BETWEEN ? AND ?";
        long presentCount = 0;
        try (PreparedStatement pstmt = DBHandler.getConnection().prepareStatement(sql);) {
            pstmt.setInt(1, student.getId());
            pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                presentCount = rs.getLong("PresentCount");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (workingDays == 0) return 0.0;
        return ((double) presentCount / workingDays) * 100;
    }
}
