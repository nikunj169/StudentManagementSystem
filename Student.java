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
