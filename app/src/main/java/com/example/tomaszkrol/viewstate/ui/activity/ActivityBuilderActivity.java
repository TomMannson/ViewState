package com.example.tomaszkrol.viewstate.ui.activity;

import android.os.Bundle;

import com.example.tomaszkrol.viewstate.base.BaseActivity;
import com.example.tomaszkrol.viewstate.model.Data;
import com.example.tomaszkrol.viewstate.model.SubParcelable;
import com.tommannson.viewstate.annotations.ActivityArg;
import com.tommannson.viewstate.annotations.ViewData;

import java.util.ArrayList;

/**
 * Created by tomasz.krol on 2016-12-14.
 */

public class ActivityBuilderActivity extends BaseActivity {

    @ActivityArg
    int intData;

    @ActivityArg
    short shortData;

    @ActivityArg
    byte byteData;

    @ActivityArg
    long longData;

    @ActivityArg
    float floatData;

    @ActivityArg
    double doubleData;

    @ActivityArg
    @ViewData
    boolean booleanData;

    @ActivityArg
    String stringData;

    @ActivityArg
    CharSequence charSeqData;

    @ActivityArg
    SubParcelable parcelableData;

    @ActivityArg
    @ViewData
    byte[] byteArrayData;

    @ActivityArg
    @ViewData
    int[] intArrayData;

    @ActivityArg
    @ViewData
    String[] stringArray;

    @ActivityArg
    @ViewData
    CharSequence[] charSeqArray;

    @ActivityArg
    @ViewData
    SubParcelable[] parcelableArray;

    @ActivityArg
    @ViewData
    ArrayList<String> stringArrayList;

    @ActivityArg
    @ViewData
    ArrayList<Integer> integerArrayList;

    @ActivityArg
    @ViewData
    ArrayList<Data> serializableArrayList;

    @ActivityArg
    @ViewData
    ArrayList<SubParcelable> parcelableArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBuilderActivityIntentBuilder.getDataFromIntent(this);
        this.toString();

    }
}
