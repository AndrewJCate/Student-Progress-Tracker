package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cate.studentprogresstracker.R;

import database.Repository;
import entities.Assessment;

public class AssessmentDetails extends AppCompatActivity {

    private int        assessmentId;
    private EditText   editTitle;
    private EditText   editType;
    private EditText   editEndDate;
    private EditText   editStartDate;
    private Repository repository;
    private int        courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        String       title;
        String       type;
        String       startDate;
        String       endDate;

        editTitle     = findViewById(R.id.assessmentEditTitle);
        editType      = findViewById(R.id.assessmentEditType);
        editStartDate = findViewById(R.id.assessmentEditStartDate);
        editEndDate   = findViewById(R.id.assessmentEditEndDate);

        assessmentId = getIntent().getIntExtra("id", -1);
        title        = getIntent().getStringExtra("title");
        type         = getIntent().getStringExtra("type");
        startDate    = getIntent().getStringExtra("startDate");
        endDate      = getIntent().getStringExtra("endDate");
        courseId     = getIntent().getIntExtra("courseId", -1);

        repository   = new Repository(getApplication());

        editTitle.setText(title);
        editType.setText(type);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);

        Button button = findViewById(R.id.assessmentSaveDetailsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Assessment assessment;
                if (assessmentId == -1) {
                    assessment = new Assessment(
                            0,
                            editTitle.getText().toString(),
                            editType.getText().toString(),
                            editStartDate.getText().toString(),
                            editEndDate.getText().toString(),
                            courseId
                    );
                    repository.insert(assessment);
                }
                else {  // Update existing Assessment object data
                    assessment = new Assessment(
                            assessmentId,
                            editTitle.getText().toString(),
                            editType.getText().toString(),
                            editStartDate.getText().toString(),
                            editEndDate.getText().toString(),
                            courseId
                    );
                    repository.update(assessment);
                }
            }
        });
    }
}