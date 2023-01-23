package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cate.studentprogresstracker.R;

import database.Repository;
import entities.Term;

public class TermDetails extends AppCompatActivity {

    EditText   editEndDate;
    EditText   editStartDate;
    EditText   editTitle;
    String     endDate;
    int        id;
    Repository repository;
    String     startDate;
    Term       term;
    String     title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        editTitle     = findViewById(R.id.termEditTitle);
        editStartDate = findViewById(R.id.termEditStartDate);
        editEndDate   = findViewById(R.id.termEditEndDate);
        id            = getIntent().getIntExtra("id", -1);
        title         = getIntent().getStringExtra("title");
        startDate     = getIntent().getStringExtra("startDate");
        endDate       = getIntent().getStringExtra("endDate");
        repository    = new Repository(getApplication());

        editTitle.setText(title);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);

        Button button = findViewById(R.id.termSaveDetailsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create new Term object
                if (id == -1) {
                    term = new Term(0, editTitle.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                    repository.insert(term);
                }
                else {  // Update existing Term object data
                    term = new Term(id, editTitle.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                    repository.update(term);
                }
            }
        });
    }
}