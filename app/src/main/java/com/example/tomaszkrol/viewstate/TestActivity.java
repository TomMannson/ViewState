package com.example.tomaszkrol.viewstate;

import android.os.Bundle;
import android.os.Parcelable;

import com.tommannson.viewstate.annotations.ActivityArg;
import com.tommannson.viewstate.annotations.ViewData;

import java.util.ArrayList;

/**
 * Created by tomasz.krol on 2016-12-14.
 */

public class TestActivity extends BaseActivity {

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
        TestActivityIntentBuilder.getDataFromIntent(this);
        this.toString();

    }

    @Override
    public Object saveCustomState() {
//        return null;
        return TestActivityBinder.persist(this);
    }

    @Override
    public void loadCustomState(Object retainedState) {
        TestActivityBinder.restore(this, retainedState);
    }
}
