package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        termsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TermList.class);
            startActivity(intent);
        });

        Button coursesButton = findViewById(R.id.button_courses);
        coursesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CourseList.class);
            startActivity(intent);
        });

        Button assessmentsButton = findViewById(R.id.button_assessments);
        assessmentsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AssessmentList.class);
            startActivity(intent);
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        // Sample data for db
        if (item.getItemId() == R.id.addSampleData) {
            Term term = new Term(0, "Fall semester", "09/01/22", "02/28/23");
            Course course1 = new Course(0, "Picking Perfect Pumpkins", "09/01/22",
                    "10/31/22", "Completed", "Rogers",
                    "Fred", "1234567890", "frogers@school.edu", "Visit pumpkin patch.",
                    1);
            Assessment assessment1 = new Assessment(0, "Pumpkin Patch Picking",
                    "Performance", "10/21/22", "10/21/22", 1);
            Course course2 = new Course(0, "Pumpkin Pie Perfection", "11/01/22",
                    "11/30/22", "Completed", "Rogers",
                    "Steve", "2345678901", "srogers@school.edu", "Buy flour.",
                    1);
            Assessment assessment2 = new Assessment(0, "Preparing Pumpkin Pie",
                    "Performance", "11/21/22", "11/21/22", 2);

            Repository repo = new Repository(getApplication());
            repo.insert(term);
            repo.insert(course1);
            repo.insert(assessment1);
            repo.insert(course2);
            repo.insert(assessment2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}