package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.cate.studentprogresstracker.R;

import java.util.List;

import database.Repository;
import entities.Course;

public class CourseList extends AppCompatActivity {
    private CourseAdapter courseAdapter;
    private List<Course> allCourses;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        RecyclerView recyclerView = findViewById(R.id.courseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        repository = new Repository(getApplication());
        allCourses = repository.getAllCourses();
        courseAdapter.setCourses(allCourses);
    }

    @Override
    protected void onResume() {
        super.onResume();
        allCourses = repository.getAllCourses();
        courseAdapter.setCourses(allCourses);
    }
}