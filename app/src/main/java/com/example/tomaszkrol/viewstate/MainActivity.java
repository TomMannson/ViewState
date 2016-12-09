package com.example.tomaszkrol.viewstate;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tommannson.viewstate.annotations.ActivityArg;
import com.tommannson.viewstate.annotations.ViewData;

import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends BaseActivity {


    @ActivityArg
    @ViewData
    String[] data;

    @ActivityArg
    @ViewData
    ArrayList<String> data2;

    @ActivityArg
    @ViewData
    byte[] data3;

    @ActivityArg
    @ViewData
    Serializable data4;

    @ActivityArg
    Data data5;

    @ActivityArg
    CharSequence data6;

    @ActivityArg
    CharSequence[] data7;

    @ActivityArg
    ArrayList<CharSequence> data8;

    @ActivityArg
    @ViewData
    ArrayList<Integer> data9;

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

//        savedInstanceState.put

//        data = "asdasd";
    }

    @Override
    public Object saveCustomState() {
        return MainActivityBinder.persist(this);
    }

    @Override
    public void loadCustomState(Object retainedState) {
        MainActivityBinder.restore(this, retainedState);
    }

}
