package com.example.tomaszkrol.viewstate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.tomaszkrol.viewstate.model.Data;
import com.example.tomaszkrol.viewstate.model.SubParcelable;
import com.tommannson.viewstate.annotations.FragmentArg;

import java.util.ArrayList;

/**
 * Created by tomasz.krol on 2017-04-04.
 */

public class TestArgsFragment extends Fragment {

    @FragmentArg
    int intData;

    @FragmentArg
    short shortData;

    @FragmentArg
    byte byteData;

    @FragmentArg
    long longData;

    @FragmentArg
    float floatData;

    @FragmentArg
    double doubleData;

    @FragmentArg
    boolean booleanData;

    @FragmentArg
    String stringData;

    @FragmentArg
    CharSequence charSeqData;

    @FragmentArg
    SubParcelable parcelableData;

    @FragmentArg
    byte[] byteArrayData;

    @FragmentArg
    int[] intArrayData;

    @FragmentArg
    String[] stringArray;

    @FragmentArg
    CharSequence[] charSeqArray;

    @FragmentArg
    SubParcelable[] parcelableArray;

    @FragmentArg
    ArrayList<String> stringArrayList;

    @FragmentArg
    ArrayList<Integer> integerArrayList;

    @FragmentArg
    ArrayList<Data> serializableArrayList;

    @FragmentArg
    ArrayList<SubParcelable> parcelableArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TestArgsFragmentBuilder.getDataFromArgs(this);

    }
}
