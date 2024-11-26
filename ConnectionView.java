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
