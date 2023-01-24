package entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {

    @PrimaryKey(autoGenerate = true)
    private int    courseId;
    private String courseTitle;
    private String courseStartDate;     // mm-dd-yyyy simple date format
    private String courseEndDate;       // mm-dd-yyyy
    private String courseStatus;        // in progress, completed, dropped, plan to take
    private String courseInstructorLastName;
    private String courseInstructorFirstName;
    private String courseInstructorPhoneNumber;
    private String courseInstructorEmail;
    private String courseNote;
    private int    termId;

    public Course(int courseId, String courseTitle, String courseStartDate, String courseEndDate,
                  String courseStatus, String courseInstructorLastName, String courseInstructorFirstName,
                  String courseInstructorPhoneNumber, String courseInstructorEmail, String courseNote, int termId) {
        this.courseId                    = courseId;
        this.courseTitle                 = courseTitle;
        this.courseStartDate             = courseStartDate;
        this.courseEndDate               = courseEndDate;
        this.courseStatus                = courseStatus;
        this.courseInstructorLastName    = courseInstructorLastName;
        this.courseInstructorFirstName   = courseInstructorFirstName;
        this.courseInstructorPhoneNumber = courseInstructorPhoneNumber;
        this.courseInstructorEmail       = courseInstructorEmail;
        this.courseNote                  = courseNote;
        this.termId                      = termId;
    }

    public Course() {
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(String courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public String getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(String courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getCourseInstructorLastName() {
        return courseInstructorLastName;
    }

    public void setCourseInstructorLastName(String courseInstructorLastName) {
        this.courseInstructorLastName = courseInstructorLastName;
    }

    public String getCourseInstructorFirstName() {
        return courseInstructorFirstName;
    }

    public void setCourseInstructorFirstName(String courseInstructorFirstName) {
        this.courseInstructorFirstName = courseInstructorFirstName;
    }

    public String getCourseInstructorPhoneNumber() {
        return courseInstructorPhoneNumber;
    }

    public void setCourseInstructorPhoneNumber(String courseInstructorPhoneNumber) {
        this.courseInstructorPhoneNumber = courseInstructorPhoneNumber;
    }

    public String getCourseInstructorEmail() {
        return courseInstructorEmail;
    }

    public void setCourseInstructorEmail(String courseInstructorEmail) {
        this.courseInstructorEmail = courseInstructorEmail;
    }

    public String getCourseNote() {
        return courseNote;
    }

    public void setCourseNote(String courseNote) {
        this.courseNote = courseNote;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }
}
