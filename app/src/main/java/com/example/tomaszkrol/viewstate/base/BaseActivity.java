package com.example.tomaszkrol.viewstate.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tommannson.viewstate.ActivityPersistLoader;
import com.tommannson.viewstate.Persistable;
import com.tommannson.viewstate.ViewBinder;


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

    @Override
    public Object saveCustomState() {
        return ViewBinder.persist(this);
    }

    @Override
    public void loadCustomState(Object retainedState) {
        ViewBinder.restore(this, retainedState);
    }
}
