package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cate.studentprogresstracker.R;

import java.util.ArrayList;
import java.util.List;

import database.Repository;
import entities.Assessment;
import entities.Course;
import entities.Term;

public class CourseDetails extends AppCompatActivity {

    private EditText   editTitle;
    private EditText   editEndDate;
    private EditText   editStartDate;
    private EditText   editStatus;
    private EditText   editInstructorFirstName;
    private EditText   editInstructorLastName;
    private EditText   editInstructorEmail;
    private EditText   editInstructorPhone;
    private EditText   editNote;
    private int        id;
    private Repository repository;
    private Course     course;
    private int termId; // TODO need to implement

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        RecyclerView recyclerView;
        String       title;
        String       startDate;
        String       endDate;
        String       status;
        String       instructorFirstName;
        String       instructorLastName;
        String       instructorPhone;
        String       instructorEmail;
        String       note;

        editTitle               = findViewById(R.id.courseEditTitle);
        editStartDate           = findViewById(R.id.courseEditStartDate);
        editEndDate             = findViewById(R.id.courseEditEndDate);
        editStatus              = findViewById(R.id.courseEditStatus);
        editInstructorFirstName = findViewById(R.id.courseEditInstructorFirstName);
        editInstructorLastName  = findViewById(R.id.courseEditInstructorLastName);
        editInstructorEmail     = findViewById(R.id.courseEditInstructorEmail);
        editInstructorPhone     = findViewById(R.id.courseEditInstructorPhone);
        editNote                = findViewById(R.id.courseEditNotes);

        id                  = getIntent().getIntExtra("id", -1);
        title               = getIntent().getStringExtra("title");
        startDate           = getIntent().getStringExtra("startDate");
        endDate             = getIntent().getStringExtra("endDate");
        status              = getIntent().getStringExtra("status");
        instructorFirstName = getIntent().getStringExtra("instructorLastName");
        instructorLastName  = getIntent().getStringExtra("instructorFirstName");
        instructorPhone     = getIntent().getStringExtra("instructorPhoneNumber");
        instructorEmail     = getIntent().getStringExtra("instructorEmail");
        note                = getIntent().getStringExtra("note");
        // TODO get term id

        repository   = new Repository(getApplication());
        recyclerView = findViewById(R.id.course_assessmentRecyclerView);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);

        editTitle.setText(title);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);
        editStatus.setText(status);
        editInstructorFirstName.setText(instructorFirstName);
        editInstructorLastName.setText(instructorLastName);
        editInstructorEmail.setText(instructorEmail);
        editInstructorPhone.setText(instructorPhone);
        editNote.setText(note);

        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Assessment> filteredAssessments = new ArrayList<>();
        for (Assessment assessment : repository.getAllAssessments()) {
            if (assessment.getCourseId() == id) {
                filteredAssessments.add(assessment);
            }
        }

        assessmentAdapter.setAssessments(filteredAssessments);

        Button button = findViewById(R.id.courseSaveDetailsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create new Course object
                if (id == -1) {
                    course = new Course(
                            0,
                            editTitle.getText().toString(),
                            editStartDate.getText().toString(),
                            editEndDate.getText().toString(),
                            editStatus.getText().toString(),
                            editInstructorLastName.getText().toString(),
                            editInstructorFirstName.getText().toString(),
                            editInstructorPhone.getText().toString(),
                            editInstructorEmail.getText().toString(),
                            editNote.getText().toString(),
                            1   // FIXME need to get term id
                    );
                    repository.insert(course);
                }
                else {  // Update existing Course object data
                    course = new Course(
                            id,
                            editTitle.getText().toString(),
                            editStartDate.getText().toString(),
                            editEndDate.getText().toString(),
                            editStatus.getText().toString(),
                            editInstructorLastName.getText().toString(),
                            editInstructorFirstName.getText().toString(),
                            editInstructorPhone.getText().toString(),
                            editInstructorEmail.getText().toString(),
                            editNote.getText().toString(),
                            1   // FIXME need to get term id
                    );
                    repository.update(course);
                }
            }
        });
    }
}