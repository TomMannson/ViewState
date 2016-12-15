package com.example.tomaszkrol.viewstate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
    int intData;

    @ActivityArg
    short shortData;

    @ActivityArg
    byte byteData;

    @ActivityArg
    long longData;

    @ActivityArg
    String stringData;

    @ActivityArg
    float floatData;

    @ActivityArg
    double doubleData;

    @ActivityArg
    boolean booleanData;

    @ActivityArg
    ArrayList<CharSequence> dataArrayListCharSeq;

    @ActivityArg
    ArrayList<Integer> dataArrayListInteger;

    @ActivityArg
    ArrayList<String> dataArrayListString;


    ArrayList<Parcelable> dataArrayListParcelable;


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
//        getIntent().putParcelableArrayListExtra()

//        data = "asdasd";
//        new TestActivityIntentBuilder()
        Intent intent = new TestActivityIntentBuilder()
                .data(null)
                .data3(new byte[] { 1, 3, 4,1})
                .data4(new Data())
                .build(this);
        startActivity(intent);
//                .
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        CharSequence ass = "asdasd";
//        ArrayList<CharSequence> asss = new ArrayList<>();
//        asss.add(ass);
//
//        outState.putSerializable("DATA1", new byte[]{1});
//        outState.putSerializable("DATA2", new char[]{'a'});
//        outState.putCharSequence("DATA3", ass);
    }

    @Override
    public Object saveCustomState() {
        return null;
//        return MainActivityBinder.persist(this);
    }

    @Override
    public void loadCustomState(Object retainedState) {
//        MainActivityBinder.restore(this, retainedState);
    }

}
