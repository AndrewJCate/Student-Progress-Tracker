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
    private List<Term> allTerms;
    private Repository repository;
    private TermAdapter termAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        RecyclerView recyclerView = findViewById(R.id.term_courseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        termAdapter = new TermAdapter(this);
        recyclerView.setAdapter(termAdapter);
        repository = new Repository(getApplication());
        allTerms = repository.getAllTerms();
        termAdapter.setTerms(allTerms);

        FloatingActionButton fab = findViewById(R.id.floatingActionAddTermButton);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(TermList.this, TermDetails.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        allTerms = repository.getAllTerms();
        termAdapter.setTerms(allTerms);
    }
}