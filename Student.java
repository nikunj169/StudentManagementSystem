package sms;

import java.util.Date;

enum Gender {
	Male, Female
}

/**
 * The class that holds and manages the information about students
 * 
 * @author Artiom
 *
 */
public class Student {
	/**
	 * The name of the student
	 */
	private String name;

	/**
	 * The surname of the student
	 */
	private String surname;

	/**
	 * The age of the student
	 */
	private int age;

	/**
	 * The gender of the student
	 */
	private Gender gender;

	/**
	 * The course that the student attends
	 */
	private String course;

	/**
	 * The date when the student started the course
	 */
	private Date started;

	/**
	 * The date when the student started the course
	 */
	private Date graduation;

	/**
	 * The unique id of the student
	 */
	int id;
	/**
	 * The counter that is increased by one everytime a new instance is created,
	 * used for ID system
	 */
	static int counter;

	/**
	 * Counter is set to 1 initially
	 */
	static {
		counter = 1;
	}
	/**
	 * Default constructor
	 */
	public Student() {
		// The unique id of the student is set to value of the counter
		this.id = counter;
		// The counter after that is increased by one
		counter++;
	}
	/**
	 * @return The name of the student
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name - The name to set to the student
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return The surname of the student
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname - The surname to set to the student
	 */
	public void setSurname(final String surname) {
		this.surname = surname;
	}
