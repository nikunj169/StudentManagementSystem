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
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Reading messages in dependance of the selected language(by default ENG)
				Translator.getMessagesFromXML();

				try {
					ManagementView window = new ManagementView();
					window.managementFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ManagementView() {
		initialize();
		// Clear the selection in the table, to avoid issues with updateDatabase method
		// when cells are selected
		table.clearSelection();
		// Make it visible in constructor, in order to make tests in
		// ManagementViewTest.java work
		managementFrame.setVisible(true);
		DBHandler.updateStudents();
	}

	/**
	 * Updates the list of courses
	 */
	private void updateCourses() {
		// Get the lists of courses
		DefaultComboBoxModel courses = new DefaultComboBoxModel(DBHandler.getCourses());
		courseSelectionBox.setModel(courses);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		managementFrame = new JFrame();
		managementFrame.setBounds(100, 100, 860, 540);
		managementFrame.setResizable(false);
		managementFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		managementFrame.setTitle(Translator.getValue("sms"));
		managementFrame.getContentPane().setLayout(null);

		// The panel where students table is located
		JPanel tablePanel = new JPanel();
		tablePanel.setBorder(new LineBorder(SystemColor.textHighlight, 5));
		tablePanel.setBounds(260, 10, 575, 395);
		managementFrame.getContentPane().add(tablePanel);
		tablePanel.setLayout(null);

		// The scroll pane that allows navigation through table
		JScrollPane tableScrollPane = new JScrollPane();
		tableScrollPane.setBounds(10, 10, 555, 375);
		tablePanel.add(tableScrollPane);

		// Initializing the table and setting its model
		table = new JTable();
		tableScrollPane.setViewportView(table);
		table.setColumnSelectionAllowed(true);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { Translator.getValue("ID"), Translator.getValue("name"), Translator.getValue("surname"),
						Translator.getValue("age"), Translator.getValue("gender"), Translator.getValue("course"),
						Translator.getValue("started"), Translator.getValue("graduation") }) {
			boolean[] columnEditables = new boolean[] { false, true, true, true, true, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});

		// Creating a sorter for the table
		TableRowSorter tableSorter = new TableRowSorter(table.getModel());
		table.setRowSorter(tableSorter);

		// Creating a Table Listener to detect cell modifications
		table.getModel().addTableModelListener(new TableModelListener() {

			// Actions to perform when a cell has been edited
