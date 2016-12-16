package com.example.tomaszkrol.viewstate;

import android.app.Activity;
import android.os.Bundle;

import com.tommannson.viewstate.annotations.ActivityArg;
import com.tommannson.viewstate.annotations.ViewData;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tomasz.krol on 2016-12-14.
 */

public class TestActivity extends Activity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
