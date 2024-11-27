package sms;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginView {
    public static JFrame loginFrame;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Initialize language settings
                Translator.setLanguage(Language.ENG);
                Translator.getMessagesFromXML();

                try {
                    LoginView window = new LoginView();
                    window.loginFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LoginView() {
        initialize();
        loginFrame.setVisible(true);
    }

    private void initialize() {
        loginFrame = new JFrame();
        loginFrame.setBounds(100, 100, 640, 480);
        loginFrame.setResizable(false);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setTitle(Translator.getValue("sms"));

        loginFrame.getContentPane().setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome to Student Management System");
        welcomeLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
        welcomeLabel.setBounds(100, 50, 450, 30);
        loginFrame.getContentPane().add(welcomeLabel);

        JButton adminButton = new JButton("Administrator");
        adminButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        adminButton.setBounds(220, 150, 200, 50);
        loginFrame.getContentPane().add(adminButton);

        JButton facultyButton = new JButton("Faculty Login");
        facultyButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        facultyButton.setBounds(220, 250, 200, 50);
        loginFrame.getContentPane().add(facultyButton);

        // Action listener for admin button
        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the connection view for admin login
                ConnectionView.main(null);
                loginFrame.dispose();
            }
        });

        // Action listener for faculty button
        facultyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the faculty login view
                FacultyLoginView.main(null);
                loginFrame.dispose();
            }
        });
    }
}
