package com.example.tomaszkrol.viewstate.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.tomaszkrol.viewstate.R;
import com.example.tomaszkrol.viewstate.model.UniversalArgModelBuilder;
import com.example.tomaszkrol.viewstate.ui.fragment.UniversalFragment;

/**
 * Created by tomasz.krol on 2017-04-05.
 */

public class UniverstalActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal);

        UniversalFragment fragment = new UniversalFragment();
        fragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }
}
