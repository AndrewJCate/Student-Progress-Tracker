package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cate.studentprogresstracker.R;

import database.Repository;
import entities.Assessment;
import entities.Course;
import entities.Term;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button termsButton = findViewById(R.id.button_terms);
        termsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TermsList.class);
                startActivity(intent);
            }
        });

        Button coursesButton = findViewById(R.id.button_courses);
        coursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CoursesList.class);
                startActivity(intent);
            }
        });

        Button assessmentsButton = findViewById(R.id.button_assessments);
        assessmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AssessmentsList.class);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.addSampleData:
                // Sample data for db
                Term term = new Term(0, "Current semester", "01-09-2022", "28-02-2023");
                Course course = new Course(0, "Mobile App Development", "01-03-2023",
                        "01-31-2023", "in progress", "Rogers",
                        "Fred", "1234567890", "example@school.edu",
                        1, "notes go here");
                Assessment assessment = new Assessment(0, "ASMT 24-B",
                        "objective", "01-20-23", "01-31-2023", 1);
                Repository repo = new Repository(getApplication());
                repo.insert(term);
                repo.insert(course);
                repo.insert(assessment);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}