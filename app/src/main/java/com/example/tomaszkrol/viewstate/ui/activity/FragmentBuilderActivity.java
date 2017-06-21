package com.example.tomaszkrol.viewstate.ui.activity;

import android.os.Bundle;

import com.example.tomaszkrol.viewstate.R;
import com.example.tomaszkrol.viewstate.base.BaseActivity;
import com.example.tomaszkrol.viewstate.ui.fragment.FragmentBuilderFragment;
import com.example.tomaszkrol.viewstate.ui.fragment.FragmentBuilderFragmentBuilder;

/**
 * Created by tomasz.krol on 2016-12-14.
 */

public class FragmentBuilderActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentbuilder);

        FragmentBuilderFragment fragment = new FragmentBuilderFragmentBuilder()
                .build();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_layout, fragment)
                .commit();
    }
}
