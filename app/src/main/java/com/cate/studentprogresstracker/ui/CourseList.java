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
import entities.Course;

public class CourseList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        FloatingActionButton fab;
        List<Course> allCourses;
        RecyclerView recyclerView;
        Repository repository;
        CourseAdapter courseAdapter;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        recyclerView = findViewById(R.id.courseRecyclerView);
        courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repository = new Repository(getApplication());
        allCourses = repository.getAllCourses();
        courseAdapter.setCourses(allCourses);

//        fab = findViewById(R.id.floatingActionAddCourseButton);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CourseList.this, CourseDetails.class);
//                startActivity(intent);
//            }
//        });
    }
}