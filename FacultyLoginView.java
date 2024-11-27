package sms;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FacultyLoginView {
    public static JFrame facultyLoginFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Translator.setLanguage(Language.ENG);
                Translator.getMessagesFromXML();

                try {
                    FacultyLoginView window = new FacultyLoginView();
                    window.facultyLoginFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public FacultyLoginView() {
        initialize();
        facultyLoginFrame.setVisible(true);
    }

    private void initialize() {
        facultyLoginFrame = new JFrame();
        facultyLoginFrame.setBounds(100, 100, 400, 300);
        facultyLoginFrame.setResizable(false);
        facultyLoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        facultyLoginFrame.setTitle("Faculty Login");

        facultyLoginFrame.getContentPane().setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 80, 80, 25);
        facultyLoginFrame.getContentPane().add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(140, 80, 200, 25);
        facultyLoginFrame.getContentPane().add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 120, 80, 25);
        facultyLoginFrame.getContentPane().add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 120, 200, 25);
        facultyLoginFrame.getContentPane().add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(140, 170, 100, 30);
        facultyLoginFrame.getContentPane().add(loginButton);

        // Inside the loginButton's ActionListener
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals("") || password.equals("")) {
                    JOptionPane.showMessageDialog(new JFrame(), Translator.getValue("fillEmptyFields"),
                            Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
                } else {
                    if (DBHandler.authenticateFaculty(username, password)) {
                        // Open Faculty Home Page via main method
                        FacultyHomePage.main(new String[] { username });
                        facultyLoginFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(facultyLoginFrame, Translator.getValue("invalidCredentials"),
                                Translator.getValue("loginFailed"), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
    }
}
