package sms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * The class that holds the front-end table-management part of the application
 * and manages the actions performed out there
 * 
 * @author Artiom
 *
 */
public class ManagementView {

	/**
	 * The contents of the management window where you read and write students data
	 */
	static JFrame managementFrame;

	/**
	 * The table containing all students
	 */
	static JTable table;

	/**
	 * The field where user should write the student's name
	 */
	static JTextField nameField;

	/**
	 * The field where user should write the student's surname
	 */
	static JTextField surnameField;

	/**
	 * The field where user should write the student's age
	 */
	static JTextField ageField;

	/**
	 * The field where user should write the date when the student started attending
	 * the course
	 */
	static JTextField startedDateField;

	/**
	 * The box that user uses in order to select student's gender
	 */
	static JComboBox genderSelectionBox;

	/**
	 * The box that allows user to select a course for a student
	 */
	static JComboBox courseSelectionBox;

	/**
	 * Launch the application.
	 */
