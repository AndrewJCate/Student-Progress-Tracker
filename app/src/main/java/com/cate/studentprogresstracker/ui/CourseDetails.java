package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import entities.Assessment;
import entities.Course;
import util.Broadcaster;
import util.CalendarComparator;
import util.MyReceiver;

public class CourseDetails extends AppCompatActivity {

    private final Calendar CALENDAR_END   = Calendar.getInstance();
    private final Calendar CALENDAR_START = Calendar.getInstance();
    private final String   DATE_FORMAT    = "MM/dd/yy";
    private final SimpleDateFormat SDF    = new SimpleDateFormat(DATE_FORMAT, Locale.US);

    private AssessmentAdapter assessmentAdapter;
    private int courseId;
    private EditText editEndDate;
    private EditText editInstructorEmail;
    private EditText editInstructorFirstName;
    private EditText editInstructorLastName;
    private EditText editInstructorPhone;
    private EditText editNote;
    private EditText editStartDate;
    private String   editStatus;
    private EditText editTitle;
    private DatePickerDialog.OnDateSetListener endDateDialog;
    private List<Assessment> filteredAssessments;
    private Repository repository;
    private DatePickerDialog.OnDateSetListener startDateDialog;
    private int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        editTitle               = findViewById(R.id.courseEditTitle);
        editStartDate           = findViewById(R.id.courseEditStartDate);
        editEndDate             = findViewById(R.id.courseEditEndDate);
        editInstructorFirstName = findViewById(R.id.courseEditInstructorFirstName);
        editInstructorLastName  = findViewById(R.id.courseEditInstructorLastName);
        editInstructorEmail     = findViewById(R.id.courseEditInstructorEmail);
        editInstructorPhone     = findViewById(R.id.courseEditInstructorPhone);
        editNote                = findViewById(R.id.courseEditNotes);

        // Get current values of selected course if any
        courseId         = getIntent().getIntExtra("id", -1);
        String title     = getIntent().getStringExtra("title");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate   = getIntent().getStringExtra("endDate");
        String status    = getIntent().getStringExtra("status");
        String instructorFirstName = getIntent().getStringExtra("instructorLastName");
        String instructorLastName  = getIntent().getStringExtra("instructorFirstName");
        String instructorPhone     = getIntent().getStringExtra("instructorPhoneNumber");
        String instructorEmail     = getIntent().getStringExtra("instructorEmail");
        String note = getIntent().getStringExtra("note");
        termId      = getIntent().getIntExtra("termId", -1);

        // Set existing values to display in text areas
        editTitle.setText(title);
        editInstructorFirstName.setText(instructorFirstName);
        editInstructorLastName.setText(instructorLastName);
        editInstructorEmail.setText(instructorEmail);
        editInstructorPhone.setText(instructorPhone);
        editNote.setText(note);

        // Set date fields to current date if adding new term
        if (courseId == -1) {
            editStartDate.setText(SDF.format(new Date()));
            editEndDate.setText(SDF.format(new Date()));
        }
        else {
            editStartDate.setText(startDate);
            editEndDate.setText(endDate);
        }

        // Set list of assessments in recycler view
        repository = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.course_assessmentRecyclerView);
        assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filteredAssessments = new ArrayList<>();
        for (Assessment assessment : repository.getAllAssessments()) {
            if (assessment.getCourseId() == courseId) {
                filteredAssessments.add(assessment);
            }
        }
        assessmentAdapter.setAssessments(filteredAssessments);

        // Set spinner contents
        Spinner statusSpinner = findViewById(R.id.courseStatusSpinner);
        ArrayAdapter<CharSequence> courseArrayAdapter = ArrayAdapter.createFromResource(this, R.array.course_status, android.R.layout.simple_spinner_item);
        courseArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(courseArrayAdapter);

        // Set spinner selection to match existing value
        if (status == null) {
            statusSpinner.setSelection(0);
        }
        else {
            statusSpinner.setSelection(courseArrayAdapter.getPosition(status));
        }

        // Get spinner item when clicked
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editStatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Add delete button if not creating a new course
        if (courseId != -1) {
            LinearLayout deleteButtonLayout = findViewById(R.id.courseDeleteButtonLayout);
            Button deleteButton = new MaterialButton(this);
            deleteButton.setText(R.string.delete);
            deleteButton.setBackgroundColor(getResources().getColor(R.color.dark_red, this.getTheme()));
            deleteButtonLayout.addView(deleteButton);

            // Delete button clicked
            deleteButton.setOnClickListener(v -> {
                // Delete confirmation dialog
                new AlertDialog.Builder(CourseDetails.this)
                        .setTitle("Delete Course")
                        .setMessage("Are you sure you want to delete this course?")
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            boolean isDeleted = false;
                            // Delete approved, look for course to delete
                            for (Course course : repository.getAllCourses()) {

                                // Found course, deleting
                                if (course.getCourseId() == courseId) {
                                    repository.delete(course);
                                    isDeleted = true;
                                    Toast.makeText(CourseDetails.this, title + " deleted.", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                            // Course not found, not deleted.
                            if (!isDeleted) {
                                Toast.makeText(CourseDetails.this, "Course not found.", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            });
        }

        // Save button clicked
        Button saveButton = findViewById(R.id.courseSaveDetailsButton);
        saveButton.setOnClickListener(v -> {

            // Check valid dates
            CalendarComparator calCompare = new CalendarComparator();
            if (calCompare.isDayAfter(editStartDate.getText().toString(), editEndDate.getText().toString())) {
                Toast.makeText(CourseDetails.this, "End date should be on or after start date.", Toast.LENGTH_LONG).show();
            }
            else {  // Dates valid
                Course course;

                // Set default title if left blank
                if (editTitle.getText().toString().equals("")) {
                    editTitle.setText(R.string.blank);
                }

                // Create new Course object
                if (courseId == -1) {
                    course = new Course(
                            0,
                            editTitle.getText().toString(),
                            editStartDate.getText().toString(),
                            editEndDate.getText().toString(),
                            editStatus,
                            editInstructorLastName.getText().toString(),
                            editInstructorFirstName.getText().toString(),
                            editInstructorPhone.getText().toString(),
                            editInstructorEmail.getText().toString(),
                            editNote.getText().toString(),
                            termId
                    );
                    repository.insert(course);
                } else {  // Update existing Course object data
                    course = new Course(
                            courseId,
                            editTitle.getText().toString(),
                            editStartDate.getText().toString(),
                            editEndDate.getText().toString(),
                            editStatus,
                            editInstructorLastName.getText().toString(),
                            editInstructorFirstName.getText().toString(),
                            editInstructorPhone.getText().toString(),
                            editInstructorEmail.getText().toString(),
                            editNote.getText().toString(),
                            termId
                    );
                    repository.update(course);
                }
                finish();
            }
        });

        // Hides fab and Assessments views if creating new course
        FloatingActionButton fab       = findViewById(R.id.courseDetailsFab);
        LinearLayout assessmentsLayout = findViewById(R.id.courseDetailsAssessmentsLayout);
        if (courseId == -1) {
            fab.setVisibility(View.GONE);
            assessmentsLayout.setVisibility(View.GONE);
        }
        else {
            fab.setOnClickListener(v -> {
                Intent intent = new Intent(CourseDetails.this, AssessmentDetails.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
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
                    CourseDetails.this,
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
                    CourseDetails.this,
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        filteredAssessments = new ArrayList<>();
        for (Assessment assessment : repository.getAllAssessments()) {
            if (assessment.getCourseId() == courseId) {
                filteredAssessments.add(assessment);
            }
        }
        assessmentAdapter.setAssessments(filteredAssessments);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Don't display menu if creating new course
        if (courseId != -1) {
            getMenuInflater().inflate(R.menu.course_details_menu, menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Broadcaster broadcaster = new Broadcaster();
        Date date;
        String dateFromScreen;

        int itemId = item.getItemId();

        // Share Course Details option
        if (itemId == R.id.shareCourseDetails) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString());
            sendIntent.putExtra(Intent.EXTRA_TITLE, editTitle.getText().toString() + " Notes");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

            return true;

        }   // Notify Start option
        else if (itemId == R.id.courseNotifyStart) {
            dateFromScreen = editStartDate.getText().toString();
            date = null;

            try {
                date = SDF.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date != null) {
                String message = "Course " + editTitle.getText().toString() + " starting.";
                broadcaster.createBroadcast(CourseDetails.this, MyReceiver.class, "msg", message, date.getTime());
                Toast.makeText(CourseDetails.this, "Notification set.", Toast.LENGTH_LONG).show();
            }

            return true;

        }   // Notify End option
        else if (itemId == R.id.courseNotifyEnd) {
            dateFromScreen = editEndDate.getText().toString();
            date = null;

            try {
                date = SDF.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date != null) {
                String message = "Course " + editTitle.getText().toString() + " ending.";
                broadcaster.createBroadcast(CourseDetails.this, MyReceiver.class, "msg", message, date.getTime());

                Toast.makeText(CourseDetails.this, "Notification set.", Toast.LENGTH_LONG).show();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}