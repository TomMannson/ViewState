package com.example.tomaszkrol.viewstate.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tomaszkrol.viewstate.R;
import com.example.tomaszkrol.viewstate.base.BaseActivity;
import com.example.tomaszkrol.viewstate.model.SubParcelable;
import com.example.tomaszkrol.viewstate.model.UniversalArgModelIntentBuilder;

import java.util.ArrayList;

/**
 * Created by tomasz.krol on 2017-06-21.
 */

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void startViewStateDemo(View view) {
        Intent intent = new Intent(this, ViewDataActivity.class);
        startActivity(intent);
    }

    public void startActivityArgs(View view) {
        ArrayList<SubParcelable> list = new ArrayList<>();
        list.add(new SubParcelable());

        Intent intentForStart = new ActivityBuilderActivityIntentBuilder()
                .booleanData(true)
                .charSeqArray(new CharSequence[]{})
                .parcelableArrayList(list)
                .parcelableArray(new SubParcelable[]{new SubParcelable()})
                .build(this);

        startActivity(intentForStart);
    }

    public void startFragmentArgs(View view) {
        Intent intent = new Intent(this, FragmentBuilderActivity.class);
        startActivity(intent);
    }

    public void startModelArgs(View view) {
        Intent intentForStart = new UniversalArgModelIntentBuilder()
                .data("Test")
                .buildForModelBuilderActivity(this);

        startActivity(intentForStart);

    }


}
