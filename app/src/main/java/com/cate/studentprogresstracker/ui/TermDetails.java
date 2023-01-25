package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cate.studentprogresstracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import database.Repository;
import entities.Course;
import entities.Term;

public class TermDetails extends AppCompatActivity {

    private EditText   editEndDate;
    private EditText   editStartDate;
    private EditText   editTitle;
    private int termId;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        String       endDate;
        RecyclerView recyclerView;
        String       startDate;
        String       title;
        FloatingActionButton fab;

        editTitle     = findViewById(R.id.termEditTitle);
        editStartDate = findViewById(R.id.termEditStartDate);
        editEndDate   = findViewById(R.id.termEditEndDate);

        termId        = getIntent().getIntExtra("id", -1);
        title         = getIntent().getStringExtra("title");
        startDate     = getIntent().getStringExtra("startDate");
        endDate       = getIntent().getStringExtra("endDate");

        repository    = new Repository(getApplication());
        recyclerView  = findViewById(R.id.term_courseRecyclerView);
        final CourseAdapter courseAdapter = new CourseAdapter(this);

        editTitle.setText(title);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);

        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Course> filteredCourses = new ArrayList<>();
        for (Course course : repository.getAllCourses()) {
            if (course.getTermId() == termId) {
                filteredCourses.add(course);
            }
        }

        courseAdapter.setCourses(filteredCourses);

        Button saveButton = findViewById(R.id.termSaveDetailsButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Term term;

                // Create new Term object
                if (termId == -1) {
                    term = new Term(
                            0,
                            editTitle.getText().toString(),
                            editStartDate.getText().toString(),
                            editEndDate.getText().toString()
                    );
                    repository.insert(term);
                }
                else {  // Update existing Term object data
                    term = new Term(
                            termId,
                            editTitle.getText().toString(),
                            editStartDate.getText().toString(),
                            editEndDate.getText().toString()
                    );
                    repository.update(term);
                }
            }
        });

        fab = findViewById(R.id.termDetailsFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermDetails.this, CourseDetails.class);
                intent.putExtra("termId", termId);
                startActivity(intent);
            }
        });
    }
}