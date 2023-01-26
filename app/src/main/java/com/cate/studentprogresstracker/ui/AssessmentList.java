package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cate.studentprogresstracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import database.Repository;
import entities.Assessment;

public class AssessmentList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Assessment> allAssessments;
        RecyclerView recyclerView;
        Repository repository;
        AssessmentAdapter assessmentAdapter;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        recyclerView = findViewById(R.id.assessmentRecyclerView);
        assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repository = new Repository(getApplication());
        allAssessments = repository.getAllAssessments();
        assessmentAdapter.setAssessments(allAssessments);
    }
}