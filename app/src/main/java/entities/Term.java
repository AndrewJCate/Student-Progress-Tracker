package entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "terms")
public class Term {

    @PrimaryKey(autoGenerate = true)
    private int    termId;
    private String termTitle;
    private String termStartDate;   // mm-dd-yyyy
    private String termEndDate;     // mm-dd-yyyy

    public Term(int termId, String termTitle, String termStartDate, String termEndDate) {
        this.termId        = termId;
        this.termTitle     = termTitle;
        this.termStartDate = termStartDate;
        this.termEndDate   = termEndDate;
    }

    public Term() {
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getTermTitle() {
        return termTitle;
    }

    public void setTermTitle(String termTitle) {
        this.termTitle = termTitle;
    }

    public String getTermStartDate() {
        return termStartDate;
    }

    public void setTermStartDate(String termStartDate) {
        this.termStartDate = termStartDate;
    }

    public String getTermEndDate() {
        return termEndDate;
    }

    public void setTermEndDate(String termEndDate) {
        this.termEndDate = termEndDate;
    }
}
