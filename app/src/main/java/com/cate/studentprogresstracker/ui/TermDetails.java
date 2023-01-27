package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cate.studentprogresstracker.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import database.Repository;
import entities.Assessment;
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
        if (termId == -1) {
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

        // Save term details button
        Button saveButton = findViewById(R.id.termSaveDetailsButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Term term;

                // Set default title if left blank
                if (editTitle.getText().toString().equals("")) {
                    editTitle.setText("*blank*");
                }

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

        // Add delete button
        LinearLayout deleteButtonLayout = findViewById(R.id.termDeleteButtonLayout);
        Button deleteButton = new MaterialButton(this);
        deleteButton.setText(R.string.delete);
        deleteButton.setBackgroundColor(getResources().getColor(R.color.dark_red, this.getTheme()));
        deleteButtonLayout.addView(deleteButton);

        // Delete button clicked
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Term currentTerm = null;

                // Find current term
                for (Term term : repository.getAllTerms()) {

                    // Current term found
                    if (term.getTermId() == termId) {
                        currentTerm = term;

                        // Find any associated courses
                        for (Course course : repository.getAllCourses()) {

                            // Term has associated courses
                            if (course.getTermId() == termId) {
                                Toast.makeText(TermDetails.this, "Cannot delete term with courses. Remove courses before deleting term.", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }
                }

                // Term not found
                if (currentTerm == null) {
                    Toast.makeText(TermDetails.this, "Term not found.", Toast.LENGTH_LONG).show();
                }
                else {  // Term found and no associated courses
                    // Delete confirmation dialog
                    Term finalCurrentTerm = currentTerm;
                    new AlertDialog.Builder(TermDetails.this)
                            .setTitle("Delete Term")
                            .setMessage("Are you sure you want to delete this term?")
                            .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Delete approved
                                    repository.delete(finalCurrentTerm);
                                    Toast.makeText(TermDetails.this, title + " deleted.", Toast.LENGTH_LONG).show();
                                    //TODO: return to previous screen
                                }
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .show();
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
        LinearLayout coursesLayout = findViewById(R.id.termCoursesLayout);
        // Hides fab and Assessments views if creating new course
        if (termId == -1) {
            fab.setVisibility(View.GONE);
            coursesLayout.setVisibility(View.GONE);
            deleteButtonLayout.setVisibility(View.INVISIBLE);
        }
        else {  // Set click listener for fab if visible
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

    private void updateLabel(EditText editText, Calendar calendar) {
        String dateFormat;
        SimpleDateFormat sdf;

        dateFormat = "MM/dd/yy";
        sdf = new SimpleDateFormat(dateFormat, Locale.US);

        editText.setText(sdf.format(calendar.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.term_details_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteCourses:
                new AlertDialog.Builder(TermDetails.this)
                        .setTitle("Delete All Courses")
                        .setMessage("Are you sure you want to delete ALL courses associated with this term?")
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Boolean isDeleted = false;
                                // Delete approved

                                // Find any associated courses
                                for (Course course : repository.getAllCourses()) {

                                    // Term has associated courses
                                    if (course.getTermId() == termId) {
                                        repository.delete(course);
                                        isDeleted = true;
                                    }
                                }

                                if (isDeleted) {
                                    Toast.makeText(TermDetails.this, "All associated courses deleted.", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(TermDetails.this, "No courses to delete.", Toast.LENGTH_LONG).show();
                                }
                                // TODO: refresh screen
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}