package sms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The main selection screen allowing users to navigate to Faculty Login.
 */
public class MainSelectionView {
    static JFrame mainFrame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainSelectionView window = new MainSelectionView();
                    window.mainFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MainSelectionView() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        mainFrame = new JFrame();
        mainFrame.setBounds(100, 100, 400, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setTitle("Welcome to SMS");
        mainFrame.getContentPane().setLayout(null);

        JButton btnFacultyLogin = new JButton("Faculty Login");
        btnFacultyLogin.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnFacultyLogin.setBounds(100, 100, 200, 50);
        mainFrame.getContentPane().add(btnFacultyLogin);

        // Action Listener for Faculty Login Button
        btnFacultyLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the login view
                LoginView.main(null);
                mainFrame.dispose();
            }
        });
    }
}
