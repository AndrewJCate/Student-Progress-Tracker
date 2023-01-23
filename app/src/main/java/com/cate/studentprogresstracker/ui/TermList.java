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
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final TermAdapter    termAdapter;

        FloatingActionButton fab;
        List<Term>           allTerms;
        RecyclerView         recyclerView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        recyclerView = findViewById(R.id.term_courseRecyclerView);
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

    @Override
    protected void onResume() {
        final TermAdapter termAdapter;

        List<Term> allTerms;
        RecyclerView recyclerView;

        super.onResume();

        allTerms     = repository.getAllTerms();
        recyclerView = findViewById(R.id.term_courseRecyclerView);
        termAdapter  = new TermAdapter(this );

        recyclerView.setAdapter(termAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        termAdapter.setTerms(allTerms);
    }
}