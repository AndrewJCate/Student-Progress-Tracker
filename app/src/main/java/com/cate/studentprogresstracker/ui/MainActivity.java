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
    public static int alertNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button termsButton = findViewById(R.id.button_terms);
        termsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TermList.class);
                startActivity(intent);
            }
        });

        Button coursesButton = findViewById(R.id.button_courses);
        coursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CourseList.class);
                startActivity(intent);
            }
        });

        Button assessmentsButton = findViewById(R.id.button_assessments);
        assessmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AssessmentList.class);
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
                Term term = new Term(0, "Current semester", "01/09/22", "02/28/23");
                Course course = new Course(0, "Mobile App Development", "01/03/23",
                        "01/31/23", "in progress", "Rogers",
                        "Fred", "1234567890", "example@school.edu", "notes go here",
                        1);
                Assessment assessment = new Assessment(0, "ASMT 24-B",
                        "objective", "01/20/23", "01/31/23", 1);
                Repository repo = new Repository(getApplication());
                repo.insert(term);
                repo.insert(course);
                repo.insert(assessment);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}