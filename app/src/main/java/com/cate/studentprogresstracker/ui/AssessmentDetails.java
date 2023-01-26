package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.cate.studentprogresstracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import database.Repository;
import entities.Assessment;

public class AssessmentDetails extends AppCompatActivity {

    private final Calendar CALENDAR_START = Calendar.getInstance();
    private final Calendar CALENDAR_END = Calendar.getInstance();

    private int assessmentId;
    private EditText editTitle;
    private EditText editType;
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

        editTitle = findViewById(R.id.assessmentEditTitle);
        editType = findViewById(R.id.assessmentEditType);
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
        editType.setText(type);

        // Set date fields to current date if adding new term
        if (assessmentId == -1) {
            editStartDate.setText(sdf.format(new Date()));
            editEndDate.setText(sdf.format(new Date()));
        }
        else {
            editStartDate.setText(startDate);
            editEndDate.setText(endDate);
        }

        repository = new Repository(getApplication());

        Button button = findViewById(R.id.assessmentSaveDetailsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Assessment assessment;

                // Set default title if left blank
                if (editTitle.getText().toString().equals("")) {
                    editTitle.setText("*blank*");
                }

                if (assessmentId == -1) {
                    assessment = new Assessment(
                            0,
                            editTitle.getText().toString(),
                            editType.getText().toString(),
                            editStartDate.getText().toString(),
                            editEndDate.getText().toString(),
                            courseId
                    );
                    repository.insert(assessment);
                } else {  // Update existing Assessment object data
                    assessment = new Assessment(
                            assessmentId,
                            editTitle.getText().toString(),
                            editType.getText().toString(),
                            editStartDate.getText().toString(),
                            editEndDate.getText().toString(),
                            courseId
                    );
                    repository.update(assessment);
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
                        AssessmentDetails.this,
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
                        AssessmentDetails.this,
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
        getMenuInflater().inflate(R.menu.assessment_details_menu, menu);
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
            case R.id.assessmentNotifyStart:
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

                intent = new Intent(AssessmentDetails.this, MyReceiver.class);
                intent.putExtra("msg", "Assessment " + editTitle.getText().toString() + " starting.");
                pendingIntent = PendingIntent.getBroadcast(
                        AssessmentDetails.this,
                        ++MainActivity.alertNumber,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE);
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);

                return true;

            case R.id.assessmentNotifyEnd:
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

                intent = new Intent(AssessmentDetails.this, MyReceiver.class);
                intent.putExtra("msg", "Assessment " + editTitle.getText().toString() + " ending.");
                pendingIntent = PendingIntent.getBroadcast(
                        AssessmentDetails.this,
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