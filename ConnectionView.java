package sms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * The class that holds the front-end connection part of the application and
 * manages the actions performed out there
 * 
 * @author Artiom
 *
 */
public class ConnectionView {
  
	/**
	 * The contents of the connection window where you have to connect to a database
	 */
	static JFrame connectionFrame;

	/**
	 * The text field that stores the login the user has written
	 */
	private JTextField loginField;
	private JPasswordField passwordField;
	private JTextField databaseUrlField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Reading messages in dependance of the selected language(by default ENG)
				Translator.setLanguage(Language.ENG);
				Translator.getMessagesFromXML();

				try {
					ConnectionView window = new ConnectionView();
					window.connectionFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public ConnectionView() {
		initialize();
		// Make it visible in constructor, in order to make tests in
		// ConnectionViewTest.java work
		connectionFrame.setVisible(true);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		connectionFrame = new JFrame();
		connectionFrame.setBounds(100, 100, 640, 480);
		connectionFrame.setResizable(false);
		connectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		connectionFrame.setTitle(Translator.getValue("sms"));
	// The blue-colored panel in the top part of the application
		JPanel topPanel = new JPanel();
		topPanel.setBackground(SystemColor.textHighlight);
		connectionFrame.getContentPane().add(topPanel, BorderLayout.NORTH);
	// The text that informs the user that they have to connect to a database
		JLabel connectText = new JLabel(Translator.getValue("connectText"));
		connectText.setForeground(new Color(255, 255, 255));
		connectText.setFont(new Font("Tahoma", Font.PLAIN, 25));
		topPanel.add(connectText);

