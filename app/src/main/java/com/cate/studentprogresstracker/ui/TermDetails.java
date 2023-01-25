package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.cate.studentprogresstracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import database.Repository;
import entities.Course;
import entities.Term;

public class TermDetails extends AppCompatActivity {

    private final Calendar CALENDAR_START = Calendar.getInstance();
    private final Calendar CALENDAR_END = Calendar.getInstance();

    private EditText editEndDate;
    private EditText editStartDate;
    private EditText editTitle;
    private int termId;
    private Repository repository;
    private DatePickerDialog.OnDateSetListener startDateDialog;
    private DatePickerDialog.OnDateSetListener endDateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        String endDate;
        RecyclerView recyclerView;
        String startDate;
        String title;
        FloatingActionButton fab;
        String dateFormat;
        SimpleDateFormat sdf;

        editTitle = findViewById(R.id.termEditTitle);
        editStartDate = findViewById(R.id.termEditStartDate);
        editEndDate = findViewById(R.id.termEditEndDate);

        dateFormat = "MM/dd/yy";
        sdf = new SimpleDateFormat(dateFormat, Locale.US);

        // Get current values of selected term if any
        termId = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");

        // Set existing values to display in text areas
        editTitle.setText(title);

        // Set date fields to current date if adding new term
        if (termId == - 1) {
            editStartDate.setText(sdf.format(new Date()));
            editEndDate.setText(sdf.format(new Date()));
        }
        else {
            editStartDate.setText(startDate);
            editEndDate.setText(endDate);
        }

        // Set list of courses
        repository = new Repository(getApplication());
        recyclerView = findViewById(R.id.term_courseRecyclerView);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
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
                } else {  // Update existing Term object data
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

        // Display calendar when clicking on start date text view
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CALENDAR_START.setTime(sdf.parse(editStartDate.getText().toString()));
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(
                        TermDetails.this,
                        startDateDialog,
                        CALENDAR_START.get(Calendar.YEAR),
                        CALENDAR_START.get(Calendar.MONTH),
                        CALENDAR_START.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        // Saves selected start date info from calendar
        startDateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                CALENDAR_START.set(Calendar.YEAR, year);
                CALENDAR_START.set(Calendar.MONTH, month);
                CALENDAR_START.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel(editStartDate, CALENDAR_START);
            }
        };

        // Display calendar when clicking on end date text view
        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CALENDAR_END.setTime(sdf.parse(editEndDate.getText().toString()));
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(
                        TermDetails.this,
                        endDateDialog,
                        CALENDAR_END.get(Calendar.YEAR),
                        CALENDAR_END.get(Calendar.MONTH),
                        CALENDAR_END.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        // Saves selected end date info from calendar
        endDateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                CALENDAR_END.set(Calendar.YEAR, year);
                CALENDAR_END.set(Calendar.MONTH, month);
                CALENDAR_END.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel(editEndDate, CALENDAR_END);
            }
        };

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

    private void updateLabel(EditText editText, Calendar calendar) {
        String dateFormat;
        SimpleDateFormat sdf;

        dateFormat = "MM/dd/yy";
        sdf = new SimpleDateFormat(dateFormat, Locale.US);

        editText.setText(sdf.format(calendar.getTime()));
    }
}