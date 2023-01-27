package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.cate.studentprogresstracker.R;

import java.util.List;

import database.Repository;
import entities.Assessment;

public class AssessmentList extends AppCompatActivity {
    AssessmentAdapter assessmentAdapter;
    List<Assessment> allAssessments;
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        RecyclerView recyclerView = findViewById(R.id.assessmentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        repository = new Repository(getApplication());
        allAssessments = repository.getAllAssessments();
        assessmentAdapter.setAssessments(allAssessments);
    }

    @Override
    protected void onResume() {
        super.onResume();
        allAssessments = repository.getAllAssessments();
        assessmentAdapter.setAssessments(allAssessments);
    }
}