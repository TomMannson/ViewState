package com.example.tomaszkrol.viewstate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tommannson.viewstate.annotations.ViewData;

import java.util.ArrayList;


public class MainActivity extends BaseActivity {

    @ViewData
    Integer data;

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

                ArrayList<SubParcelable> list = new ArrayList<>();
                list.add(new SubParcelable());

                Intent intentForStart = new TestActivityIntentBuilder()
                        .booleanData(true)
                        .charSeqArray(new CharSequence[]{})
                        .parcelableArrayList(list)
                        .parcelableArray(new SubParcelable[]{new SubParcelable()})
                        .build(MainActivity.this);

                startActivity(intentForStart);




            }
        });

        ArrayList<Data> data = new ArrayList<>();
        data.add(new Data());

        getSupportFragmentManager()
                .beginTransaction()
                .add(new TestArgsFragmentBuilder()
                        .serializableArrayList(data)
                        .build(), "")
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
