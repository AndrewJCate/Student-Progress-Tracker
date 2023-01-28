package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import database.Repository;
import entities.Assessment;

public class AssessmentDetails extends AppCompatActivity {

    private final Calendar CALENDAR_START = Calendar.getInstance();
    private final Calendar CALENDAR_END = Calendar.getInstance();

    private int assessmentId;
    private EditText editTitle;
    private String editType;
    private EditText editEndDate;
    private EditText editStartDate;
    private Repository repository;
    private int courseId;
    private DatePickerDialog.OnDateSetListener startDateDialog;
    private DatePickerDialog.OnDateSetListener endDateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        String title;
        String type;
        String startDate;
        String endDate;
        String dateFormat;
        SimpleDateFormat sdf;
        Spinner typeSpinner;

        editTitle = findViewById(R.id.assessmentEditTitle);
        editStartDate = findViewById(R.id.assessmentEditStartDate);
        editEndDate = findViewById(R.id.assessmentEditEndDate);

        dateFormat = "MM/dd/yy";
        sdf = new SimpleDateFormat(dateFormat, Locale.US);

        // Get current values of selected term if any
        assessmentId = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        courseId = getIntent().getIntExtra("courseId", -1);

        // Set existing values to display in text areas
        editTitle.setText(title);

        // Set date fields to current date if adding new term
        if (assessmentId == -1) {
            editStartDate.setText(sdf.format(new Date()));
            editEndDate.setText(sdf.format(new Date()));
        }
        else {
            editStartDate.setText(startDate);
            editEndDate.setText(endDate);
        }

        // Set type spinner contents
        typeSpinner = findViewById(R.id.assessmentTypeSpinner);
        ArrayAdapter<CharSequence> assessmentArrayAdapter = ArrayAdapter.createFromResource(this, R.array.assessment_types, android.R.layout.simple_spinner_item);
        assessmentArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(assessmentArrayAdapter);

        // Set spinner selection to match existing value
        if (type == null) {
            typeSpinner.setSelection(0);
        }
        else {
            typeSpinner.setSelection(assessmentArrayAdapter.getPosition(type));
        }

        // Get spinner item when clicked
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  editType = parent.getItemAtPosition(position).toString();
              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {
                  // Do nothing
              }
        });

        repository = new Repository(getApplication());

        // Save button clicked
        Button saveButton = findViewById(R.id.assessmentSaveDetailsButton);
        saveButton.setOnClickListener(v -> {
            // Check dates
            if (CALENDAR_START.after(CALENDAR_END)) {
                Toast.makeText(AssessmentDetails.this, "End date should be on or after start date.", Toast.LENGTH_LONG).show();
            }
            else {  // Dates ok
                Assessment assessment;

                // Set default title if left blank
                if (editTitle.getText().toString().trim().equals("")) {
                    editTitle.setText(R.string.blank);
                }

                // Create new Assessment
                if (assessmentId == -1) {
                    assessment = new Assessment(
                            0,
                            editTitle.getText().toString(),
                            editType,
                            editStartDate.getText().toString(),
                            editEndDate.getText().toString(),
                            courseId
                    );
                    repository.insert(assessment);
                } else {  // Update existing Assessment object data
                    assessment = new Assessment(
                            assessmentId,
                            editTitle.getText().toString(),
                            editType,
                            editStartDate.getText().toString(),
                            editEndDate.getText().toString(),
                            courseId
                    );
                    repository.update(assessment);
                }
                finish();
            }
        });

        // Add delete button if not creating new assessment
        if (assessmentId != -1) {
            LinearLayout layout = findViewById(R.id.assessmentDeleteButtonLayout);
            Button deleteButton = new MaterialButton(this);
            deleteButton.setText(R.string.delete);
            deleteButton.setBackgroundColor(getResources().getColor(R.color.dark_red, this.getTheme()));
            layout.addView(deleteButton);

            // Delete button clicked
            deleteButton.setOnClickListener(v -> {
                // Delete confirmation dialog
                new AlertDialog.Builder(AssessmentDetails.this)
                        .setTitle("Delete Assessment")
                        .setMessage("Are you sure you want to delete this assessment?")
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            // Delete approved
                            boolean isDeleted = false;

                            for (Assessment assessment : repository.getAllAssessments()) {
                                if (assessment.getAssessmentId() == assessmentId) {
                                    repository.delete(assessment);
                                    isDeleted = true;
                                    Toast.makeText(AssessmentDetails.this, title + " deleted.", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                            // Not deleted
                            if (!isDeleted) {
                                Toast.makeText(AssessmentDetails.this, "Assessment not found.", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            });
        }

        // Display calendar when clicking on start date text view
        editStartDate.setOnClickListener(v -> {
            try {
                CALENDAR_START.setTime(Objects.requireNonNull(sdf.parse(editStartDate.getText().toString())));
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

            new DatePickerDialog(
                    AssessmentDetails.this,
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

            updateLabel(editStartDate, CALENDAR_START);
        };

        // Display calendar when clicking on end date text view
        editEndDate.setOnClickListener(v -> {
            try {
                CALENDAR_END.setTime(Objects.requireNonNull(sdf.parse(editEndDate.getText().toString())));
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

            new DatePickerDialog(
                    AssessmentDetails.this,
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

            updateLabel(editEndDate, CALENDAR_END);
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
        getMenuInflater().inflate(R.menu.assessment_details_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        final String dateFormat = "MM/dd/yy";

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        String dateFromScreen;
        Date date = null;
        long trigger;
        Intent intent;
        PendingIntent pendingIntent;
        AlarmManager alarmManager;

        if (item.getItemId() == R.id.assessmentNotifyStart) {
            // Don't create reminder if assessment does not exist
            if (assessmentId == -1) {
                Toast.makeText(AssessmentDetails.this, "Assessment does not exist. Please save first.", Toast.LENGTH_LONG).show();
                // FIXME: is there a way to make this more general?
            }
            else {
                dateFromScreen = editStartDate.getText().toString();

                try {
                    date = sdf.parse(dateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                assert date != null;
                trigger = date.getTime();

                intent = new Intent(AssessmentDetails.this, MyReceiver.class);
                intent.putExtra("msg", "Assessment " + editTitle.getText().toString() + " starting.");
                pendingIntent = PendingIntent.getBroadcast(
                        AssessmentDetails.this,
                        ++MainActivity.alertNumber,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE);
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
            }
            return true;
        }
        else if (item.getItemId() ==  R.id.assessmentNotifyEnd) {
            // Don't create reminder if assessment does not exist
            if (assessmentId == -1) {
                Toast.makeText(AssessmentDetails.this, "Assessment does not exist. Please save first.", Toast.LENGTH_LONG).show();
                // FIXME: is there a way to make this more general?
            }
            else {
                dateFromScreen = editEndDate.getText().toString();

                try {
                    date = sdf.parse(dateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                assert date != null;
                trigger = date.getTime();

                intent = new Intent(AssessmentDetails.this, MyReceiver.class);
                intent.putExtra("msg", "Assessment " + editTitle.getText().toString() + " ending.");
                pendingIntent = PendingIntent.getBroadcast(
                        AssessmentDetails.this,
                        ++MainActivity.alertNumber,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE);
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}