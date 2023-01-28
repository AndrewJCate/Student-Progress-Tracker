package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.Objects;

import database.Repository;
import entities.Course;
import entities.Term;
import util.CalendarComparator;

public class TermDetails extends AppCompatActivity {

    private final Calendar CALENDAR_END   = Calendar.getInstance();
    private final Calendar CALENDAR_START = Calendar.getInstance();
    private final String   DATE_FORMAT    = "MM/dd/yy";
    private final SimpleDateFormat SDF    = new SimpleDateFormat(DATE_FORMAT, Locale.US);

    private CourseAdapter courseAdapter;
    private EditText      editEndDate;
    private EditText      editStartDate;
    private EditText      editTitle;
    private DatePickerDialog.OnDateSetListener endDateDialog;
    private List<Course>  filteredCourses;
    private Repository    repository;
    private DatePickerDialog.OnDateSetListener startDateDialog;
    private int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        editTitle     = findViewById(R.id.termEditTitle);
        editStartDate = findViewById(R.id.termEditStartDate);
        editEndDate   = findViewById(R.id.termEditEndDate);

        // Get current values of selected term if any
        termId           = getIntent().getIntExtra("id", -1);
        String title     = getIntent().getStringExtra("title");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate   = getIntent().getStringExtra("endDate");

        // Set existing values to display in text areas
        editTitle.setText(title);

        // Set date fields to current date if adding new term
        if (termId == -1) {
            editStartDate.setText(SDF.format(new Date()));
            editEndDate.setText(SDF.format(new Date()));
        }
        else {
            editStartDate.setText(startDate);
            editEndDate.setText(endDate);
        }

        // Set list of courses in recycler view
        repository = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.term_courseRecyclerView);
        courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filteredCourses = new ArrayList<>();
        for (Course course : repository.getAllCourses()) {
            if (course.getTermId() == termId) {
                filteredCourses.add(course);
            }
        }
        courseAdapter.setCourses(filteredCourses);

        // Save term details button
        Button saveButton = findViewById(R.id.termSaveDetailsButton);
        saveButton.setOnClickListener(v -> {

            // Check valid dates
            CalendarComparator calCompare = new CalendarComparator();
            if (calCompare.isDayAfter(editStartDate.getText().toString(), editEndDate.getText().toString())) {
                Toast.makeText(TermDetails.this, "End date should be on or after start date.", Toast.LENGTH_LONG).show();
            }
            else {  // Dates valid
                Term term;

                // Set default title if left blank
                if (editTitle.getText().toString().equals("")) {
                    editTitle.setText(R.string.blank);
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
                finish();
            }
        });

        // Add delete button if not new term
        if (termId != -1) {
            LinearLayout deleteButtonLayout = findViewById(R.id.termDeleteButtonLayout);
            Button deleteButton = new MaterialButton(this);
            deleteButton.setText(R.string.delete);
            deleteButton.setBackgroundColor(getResources().getColor(R.color.dark_red, this.getTheme()));
            deleteButtonLayout.addView(deleteButton);

            // Delete button clicked
            deleteButton.setOnClickListener(v -> {
                Term currentTerm = null;
                boolean courseFound = false;

                // Find current term
                for (Term term : repository.getAllTerms()) {

                    // Current term found
                    if (term.getTermId() == termId) {
                        currentTerm = term;

                        // Find any associated courses
                        for (Course course : repository.getAllCourses()) {
                            // Term has associated courses
                            if (course.getTermId() == termId) {
                                courseFound = true;
                                Toast.makeText(TermDetails.this, "Cannot delete term with courses. Remove courses before deleting term.", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }

                        if (!courseFound) {
                            // Term found and no associated courses
                            // Delete confirmation dialog
                            Term finalCurrentTerm = currentTerm;
                            new AlertDialog.Builder(TermDetails.this)
                                    .setTitle("Delete Term")
                                    .setMessage("Are you sure you want to delete this term?")
                                    .setPositiveButton(R.string.delete, (dialog, which) -> {
                                        // Delete approved
                                        repository.delete(finalCurrentTerm);
                                        Toast.makeText(TermDetails.this, title + " deleted.", Toast.LENGTH_LONG).show();
                                        finish();
                                    })
                                    .setNegativeButton(R.string.cancel, null)
                                    .show();
                        }
                    }
                }

                // Term not found
                if (currentTerm == null) {
                    Toast.makeText(TermDetails.this, "Term not found.", Toast.LENGTH_LONG).show();
                }
            });
        }

        // Display calendar when clicking on start date text view
        editStartDate.setOnClickListener(v -> {
            try {
                CALENDAR_START.setTime(Objects.requireNonNull(SDF.parse(editStartDate.getText().toString())));
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
        });

        // Saves selected start date info from calendar
        startDateDialog = (view, year, month, dayOfMonth) -> {
            CALENDAR_START.set(Calendar.YEAR, year);
            CALENDAR_START.set(Calendar.MONTH, month);
            CALENDAR_START.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            editStartDate.setText(SDF.format(CALENDAR_START.getTime()));
        };

        // Display calendar when clicking on end date text view
        editEndDate.setOnClickListener(v -> {
            try {
                CALENDAR_END.setTime(Objects.requireNonNull(SDF.parse(editEndDate.getText().toString())));
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
        });

        // Saves selected end date info from calendar
        endDateDialog = (view, year, month, dayOfMonth) -> {
            CALENDAR_END.set(Calendar.YEAR, year);
            CALENDAR_END.set(Calendar.MONTH, month);
            CALENDAR_END.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            editEndDate.setText(SDF.format(CALENDAR_END.getTime()));
        };

        // Hide fab and Assessments views if creating new course
        FloatingActionButton fab   = findViewById(R.id.termDetailsFab);
        LinearLayout coursesLayout = findViewById(R.id.termCoursesLayout);
        if (termId == -1) {
            fab.setVisibility(View.GONE);
            coursesLayout.setVisibility(View.GONE);
        }
        else {  // Set click listener for fab if visible
            fab.setOnClickListener(v -> {
                Intent intent = new Intent(TermDetails.this, CourseDetails.class);
                intent.putExtra("termId", termId);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        filteredCourses = new ArrayList<>();
        for (Course course : repository.getAllCourses()) {
            if (course.getTermId() == termId) {
                filteredCourses.add(course);
            }
        }
        courseAdapter.setCourses(filteredCourses);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Don't display menu if creating new course
        if (termId != -1) {
            getMenuInflater().inflate(R.menu.term_details_menu, menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        // Delete all courses selected from menu
        if (item.getItemId() == R.id.deleteCourses) {

            // Determine if there are courses to delete
            boolean hasCourses = false;
            for (Course course : repository.getAllCourses()) {

                // There are courses to delete
                if (course.getTermId() == termId) {
                    hasCourses = true;
                    break;
                }
            }

            // No courses to delete
            if (!hasCourses) {
                Toast.makeText(TermDetails.this, "No courses to delete.", Toast.LENGTH_LONG).show();
            } else {
                // Show alert dialog
                new AlertDialog.Builder(TermDetails.this)
                        .setTitle("Delete All Courses")
                        .setMessage("Are you sure you want to delete ALL courses associated with this term?")
                        .setPositiveButton(R.string.delete, (dialog, which) -> {    // Delete approved

                            // Find all associated courses
                            for (Course course : repository.getAllCourses()) {

                                // Term has associated courses
                                if (course.getTermId() == termId) {
                                    repository.delete(course);
                                }
                            }
                            Toast.makeText(TermDetails.this, "All associated courses deleted.", Toast.LENGTH_LONG).show();
                            onResume();
                        })
                        .setNegativeButton(R.string.cancel, null)   // Delete not approved
                        .show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}