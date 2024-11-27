package sms;

import java.util.Date;

public class Student {
    private int id;
    private String name;
    private String surname;
    private int age;
    private String gender; // Changed from sms.Gender to String
    private String course;
    private Date started;
    private Date graduation;
    private String grade;

    // Getters and Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() { // Changed return type to String
        return gender;
    }
    public void setGender(String gender) { // Changed parameter type to String
        this.gender = gender;
    }

    public String getCourse() {
        return course;
    }
    public void setCourse(String course) {
        this.course = course;
    }

    public Date getStarted() {
        return started;
    }
    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getGraduation() {
        return graduation;
    }
    public void setGraduation(Date graduation) {
        this.graduation = graduation;
    }

    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
}
