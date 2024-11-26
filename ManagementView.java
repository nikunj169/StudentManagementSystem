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
			public void tableChanged(TableModelEvent e) {
				if (!DBHandler.updateDatabase()) {
					JOptionPane.showMessageDialog(managementFrame, Translator.getValue("checkInput"),
							Translator.getValue("sms"), JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// The panel where all buttons are located
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBorder(new LineBorder(new Color(0, 120, 215), 5));
		buttonsPanel.setBackground(UIManager.getColor("Button.background"));
		buttonsPanel.setBounds(10, 415, 825, 80);
		managementFrame.getContentPane().add(buttonsPanel);

		// The button to press to delete an information from the table
		JButton deleteButton = new JButton(Translator.getValue("delete"));
		deleteButton.setName("deleteButton");

		// Actions to perform when "delete" button clicked
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// If no row has been selected
				if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(managementFrame, Translator.getValue("noStudentSelected"),
							Translator.getValue("sms"), JOptionPane.ERROR_MESSAGE);
				} else {
					// Asking the user if they are sure about that
					if (JOptionPane.showConfirmDialog(managementFrame, Translator.getValue("warningDelete"),
							Translator.getValue("sms"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						if (DBHandler.deleteStudent()) {
							JOptionPane.showMessageDialog(managementFrame,
									Translator.getValue("studentSuccessfullyDeleted"), Translator.getValue("sms"),
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(managementFrame,
									Translator.getValue("somethingWrongUnexpected"), Translator.getValue("sms"),
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
		deleteButton.setFont(new Font("Tahoma", Font.PLAIN, 16));

		// The button to press to add a student to the table
		JButton addButton = new JButton(Translator.getValue("add"));
		addButton.setName("addButton");

		// Actions to perform when "add" button clicked
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.clearSelection();

				if (DBHandler.getFaculties().length == 0) {
					JOptionPane.showMessageDialog(managementFrame, Translator.getValue("cannotAddStudent"),
							Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
					return;
				}

				// If one of the fields are empty warn user about that
				if (nameField.getText().equals("") || surnameField.getText().equals("") || ageField.getText().equals("")
						|| startedDateField.getText().equals("")) {

					JOptionPane.showMessageDialog(managementFrame, Translator.getValue("fillEmptyFields"),
							Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						// Check if the written data is written correctly according to the format
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						format.setLenient(false);
						format.parse(startedDateField.getText());
					} catch (ParseException ex) {
						ex.printStackTrace();

						JOptionPane.showMessageDialog(managementFrame, Translator.getValue("dateFormatError"),
								Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);

						return;
					}

					if (DBHandler.addStudent()) {
						JOptionPane.showMessageDialog(managementFrame, Translator.getValue("studentSuccessfullyAdded"),
								Translator.getValue("success"), JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(managementFrame, Translator.getValue("somethingWrongInput"),
								Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		});
		buttonsPanel.setLayout(new GridLayout(0, 5, 0, 0));

		addButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		buttonsPanel.add(addButton);

		// The button to press to update an information in the table
		JButton updateButton = new JButton(Translator.getValue("update"));

		// Actions to perform when "update" button clicked
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.clearSelection();
				DBHandler.updateStudents();
			}
		});

		updateButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		buttonsPanel.add(updateButton);
		buttonsPanel.add(deleteButton);

		// The button to press to exit the application
		JButton exitButton = new JButton(Translator.getValue("exit"));
		exitButton.setName("exitButton");

		// Actions to perform when "exit" button clicked
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(managementFrame, Translator.getValue("confirmDialog"),
						Translator.getValue("sms"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					managementFrame.dispose();
					System.exit(0);
				}
			}
		});

		// The button that user have to press in order to disconnect from the current
		// database
