package entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessments")
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    private int assessmentId;
    private String assessmentTitle;
    private String assessmentType;          // performance, objective
    private String assessmentStartDate;     // mm-dd-yyyy
    private String assessmentEndDate;       // mm-dd-yyyy
    private int courseId;

    public Assessment(int assessmentId, String assessmentTitle, String assessmentType, String assessmentStartDate, String assessmentEndDate, int courseId) {
        this.assessmentId = assessmentId;
        this.assessmentTitle = assessmentTitle;
        this.assessmentType = assessmentType;
        this.assessmentStartDate = assessmentStartDate;
        this.assessmentEndDate = assessmentEndDate;
        this.courseId = courseId;
    }

    public Assessment() {
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public void setAssessmentTitle(String assessmentTitle) {
        this.assessmentTitle = assessmentTitle;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getAssessmentStartDate() {
        return assessmentStartDate;
    }

    public void setAssessmentStartDate(String assessmentStartDate) {
        this.assessmentStartDate = assessmentStartDate;
    }

    public String getAssessmentEndDate() {
        return assessmentEndDate;
    }

    public void setAssessmentEndDate(String assessmentEndDate) {
        this.assessmentEndDate = assessmentEndDate;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
