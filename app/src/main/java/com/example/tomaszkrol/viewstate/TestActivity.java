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
    String stringData;

    @ActivityArg
    float floatData;

    @ActivityArg
    double doubleData;

    @ActivityArg
    boolean booleanData;






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

//    @ActivityArg
//    CharSequence data6;

//    @ActivityArg
//    CharSequence[] data7;
//
//    @ActivityArg
//    ArrayList<CharSequence> data8;

    @ActivityArg
    @ViewData
    ArrayList<Integer> data9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
