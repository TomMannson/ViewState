package com.example.tomaszkrol.viewstate;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import pl.tommannson.viewstate.annotations.ViewData;
import pl.tommannson.viewstate.annotations.ViewState;

public class MainActivity extends AppCompatActivity {

    @ViewData
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Object obj = getLastCustomNonConfigurationInstance();
        MainActivityBinder.restore(this, obj);

//        MainActivityBinder.restore(this, this);
        data = "asdasd";
//        MainActivityBinder.persist(this, this);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return MainActivityBinder.persist(this);
    }
}
