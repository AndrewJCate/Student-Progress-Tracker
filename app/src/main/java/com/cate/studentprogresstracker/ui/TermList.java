package com.cate.studentprogresstracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cate.studentprogresstracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import database.Repository;
import entities.Term;

public class TermList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FloatingActionButton fab;
        List<Term> allTerms;
        RecyclerView recyclerView;
        Repository repository;
        TermAdapter termAdapter;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        recyclerView = findViewById(R.id.termRecyclerView);
        termAdapter = new TermAdapter(this);
        recyclerView.setAdapter(termAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repository = new Repository(getApplication());
        allTerms = repository.getAllTerms();
        termAdapter.setTerms(allTerms);

        fab = findViewById(R.id.floatingActionAddTermButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermList.this, TermDetails.class);
                startActivity(intent);
            }
        });
    }
}