package sms;
public class Grade {
    private int studentId;
    private int courseId;
    private double marks;
    private String grade;
 public Grade() {}
 public Grade(int studentId, int courseId, double marks, String grade) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.marks = marks;
        this.grade = grade;
    }
 public int getStudentId() {
        return studentId;
    }
