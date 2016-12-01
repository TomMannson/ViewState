package com.example.tomaszkrol.viewstate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pl.tomaszkrol.viewstate.ActivityPersistLoader;
import pl.tomaszkrol.viewstate.Persistable;

/**
 * Created by tomasz.krol on 2016-12-01.
 */

public abstract class BaseActivity extends AppCompatActivity implements Persistable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPersistLoader.load(this);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return ActivityPersistLoader.persist(this);
    }
}
