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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import database.Repository;
import entities.Assessment;
import entities.Course;

public class CourseDetails extends AppCompatActivity {

    private final Calendar CALENDAR_START = Calendar.getInstance();
    private final Calendar CALENDAR_END = Calendar.getInstance();

    private int courseId;
    private EditText editTitle;
    private EditText editEndDate;
    private EditText editStartDate;
    private String editStatus;
    private EditText editInstructorFirstName;
    private EditText editInstructorLastName;
    private EditText editInstructorEmail;
    private EditText editInstructorPhone;
    private EditText editNote;
    private Repository repository;
    private int termId;
    private DatePickerDialog.OnDateSetListener startDateDialog;
    private DatePickerDialog.OnDateSetListener endDateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        RecyclerView recyclerView;
        String title;
        String startDate;
        String endDate;
        String status;
        String instructorFirstName;
        String instructorLastName;
        String instructorPhone;
        String instructorEmail;
        String note;
        FloatingActionButton fab;
        String dateFormat;
        SimpleDateFormat sdf;
        Spinner statusSpinner;

        editTitle = findViewById(R.id.courseEditTitle);
        editStartDate = findViewById(R.id.courseEditStartDate);
        editEndDate = findViewById(R.id.courseEditEndDate);
//        editStatus = findViewById(R.id.courseEditStatus);
        editInstructorFirstName = findViewById(R.id.courseEditInstructorFirstName);
        editInstructorLastName = findViewById(R.id.courseEditInstructorLastName);
        editInstructorEmail = findViewById(R.id.courseEditInstructorEmail);
        editInstructorPhone = findViewById(R.id.courseEditInstructorPhone);
        editNote = findViewById(R.id.courseEditNotes);

        dateFormat = "MM/dd/yy";
        sdf = new SimpleDateFormat(dateFormat, Locale.US);

        // Get current values of selected course if any
        courseId = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        status = getIntent().getStringExtra("status");
        instructorFirstName = getIntent().getStringExtra("instructorLastName");
        instructorLastName = getIntent().getStringExtra("instructorFirstName");
        instructorPhone = getIntent().getStringExtra("instructorPhoneNumber");
        instructorEmail = getIntent().getStringExtra("instructorEmail");
        note = getIntent().getStringExtra("note");
        termId = getIntent().getIntExtra("termId", -1);

        // Set existing values to display in text areas
        editTitle.setText(title);
//        editStatus.setText(status);
        editInstructorFirstName.setText(instructorFirstName);
        editInstructorLastName.setText(instructorLastName);
        editInstructorEmail.setText(instructorEmail);
        editInstructorPhone.setText(instructorPhone);
        editNote.setText(note);

        // Set date fields to current date if adding new term
        if (courseId == -1) {
            editStartDate.setText(sdf.format(new Date()));
            editEndDate.setText(sdf.format(new Date()));
        }
        else {
            editStartDate.setText(startDate);
            editEndDate.setText(endDate);
        }

        // Set list of assessments
        repository = new Repository(getApplication());
        recyclerView = findViewById(R.id.course_assessmentRecyclerView);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Assessment> filteredAssessments = new ArrayList<>();
        for (Assessment assessment : repository.getAllAssessments()) {
            if (assessment.getCourseId() == courseId) {
                filteredAssessments.add(assessment);
            }
        }
        assessmentAdapter.setAssessments(filteredAssessments);

        // Set spinner contents
        statusSpinner = findViewById(R.id.courseStatusSpinner);
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

        // Add delete button
        LinearLayout deleteButtonLayout = findViewById(R.id.courseDeleteButtonLayout);
        Button deleteButton = new MaterialButton(this);
        deleteButton.setText(R.string.delete);
        deleteButton.setBackgroundColor(getResources().getColor(R.color.dark_red, this.getTheme()));
        deleteButtonLayout.addView(deleteButton);

        // Delete button clicked
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete confirmation dialog
                new AlertDialog.Builder(CourseDetails.this)
                        .setTitle("Delete Course")
                        .setMessage("Are you sure you want to delete this course?")
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete approved
                                for (Course course : repository.getAllCourses()) {
                                    if (course.getCourseId() == courseId) {
                                        repository.delete(course);
                                        Toast.makeText(CourseDetails.this, title + " deleted.", Toast.LENGTH_LONG).show();
                                        return;
                                        // TODO: return to previous screen
                                    }
                                }
                                // Not deleted
                                Toast.makeText(CourseDetails.this, "Course not found.", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });

        // Save button clicked
        Button saveButton = findViewById(R.id.courseSaveDetailsButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course course;

                // Set default title if left blank
                if (editTitle.getText().toString().equals("")) {
                    editTitle.setText("*blank*");
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
            }
        });

        // Hides fab and Assessments views if creating new course
        fab = findViewById(R.id.courseDetailsFab);
        LinearLayout assessmentsLayout = findViewById(R.id.courseDetailsAssessmentsLayout);
        if (courseId == -1) {
            fab.setVisibility(View.GONE);
            assessmentsLayout.setVisibility(View.GONE);
            deleteButtonLayout.setVisibility(View.INVISIBLE);
        }
        else {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CourseDetails.this, AssessmentDetails.class);
                    intent.putExtra("courseId", courseId);
                    startActivity(intent);
                }
            });
        }

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
                        CourseDetails.this,
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
                        CourseDetails.this,
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
    }

    private void updateLabel(EditText editText, Calendar calendar) {
        String dateFormat;
        SimpleDateFormat sdf;

        dateFormat = "MM/dd/yy";
        sdf = new SimpleDateFormat(dateFormat, Locale.US);

        editText.setText(sdf.format(calendar.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_details_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String dateFromScreen;
        String dateFormat;
        SimpleDateFormat sdf;
        Date date;
        Long trigger;
        Intent intent;
        PendingIntent pendingIntent;
        AlarmManager alarmManager;

        switch (item.getItemId()) {
            case R.id.shareCourseDetails:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString());
                sendIntent.putExtra(Intent.EXTRA_TITLE, editTitle.getText().toString() + " Notes");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

                return true;

            case R.id.courseNotifyStart:
                dateFromScreen = editStartDate.getText().toString();
                dateFormat = "MM/dd/yy";
                sdf = new SimpleDateFormat(dateFormat, Locale.US);
                date = null;

                try {
                    date = sdf.parse(dateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                trigger = date.getTime();

                intent = new Intent(CourseDetails.this, MyReceiver.class);
                intent.putExtra("msg", "Course " + editTitle.getText().toString() + " starting.");
                pendingIntent = PendingIntent.getBroadcast(
                        CourseDetails.this,
                        ++MainActivity.alertNumber,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE);
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);

                return true;

            case R.id.courseNotifyEnd:
                dateFromScreen = editEndDate.getText().toString();
                dateFormat = "MM/dd/yy";
                sdf = new SimpleDateFormat(dateFormat, Locale.US);
                date = null;

                try {
                    date = sdf.parse(dateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                trigger = date.getTime();

                intent = new Intent(CourseDetails.this, MyReceiver.class);
                intent.putExtra("msg", "Course " + editTitle.getText().toString() + " ending.");
                pendingIntent = PendingIntent.getBroadcast(
                        CourseDetails.this,
                        ++MainActivity.alertNumber,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE);
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}