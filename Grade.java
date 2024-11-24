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
 public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
 public int getCourseId() {
        return courseId;
    }
   public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
   public double getMarks() {
        return marks;
    }
public void setMarks(double marks) {
        this.marks = marks;
    }
   public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
}
