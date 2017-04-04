package com.example.tomaszkrol.viewstate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tommannson.viewstate.annotations.ViewData;


/**
 * Created by tomasz.krol on 2016-12-01.
 */

public class MainFragment extends BaseFragment {

    @ViewData
    String asd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        savedInstanceState.put
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(android.R.layout.simple_list_item_1, container, false);
    }

    @Override
    public Object saveCustomState() {
        return MainFragmentBinder.persist(this);
    }

    @Override
    public void loadCustomState(Object retainedState) {
        MainFragmentBinder.restore(this, retainedState);
    }

}
