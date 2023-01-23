package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cate.studentprogresstracker.R;

import java.util.ArrayList;
import java.util.List;

import database.Repository;
import entities.Course;
import entities.Term;

public class TermDetails extends AppCompatActivity {

    private EditText     editEndDate;
    private EditText     editStartDate;
    private EditText     editTitle;
    private int          id;
    private Repository   repository;
    private Term         term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        String       endDate;
        RecyclerView recyclerView;
        String       startDate;
        String       title;

        editTitle     = findViewById(R.id.termEditTitle);
        editStartDate = findViewById(R.id.termEditStartDate);
        editEndDate   = findViewById(R.id.termEditEndDate);
        id            = getIntent().getIntExtra("id", -1);
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
            if (course.getTermId() == id) {
                filteredCourses.add(course);
            }
        }

        courseAdapter.setCourses(filteredCourses);

        Button button = findViewById(R.id.termSaveDetailsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create new Term object
                if (id == -1) {
                    term = new Term(0, editTitle.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                    repository.insert(term);
                }
                else {  // Update existing Term object data
                    term = new Term(id, editTitle.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                    repository.update(term);
                }
            }
        });
    }
}