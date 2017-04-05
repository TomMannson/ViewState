package com.example.tomaszkrol.viewstate.model;

import com.example.tomaszkrol.viewstate.ui.activity.TestActivity;
import com.example.tomaszkrol.viewstate.ui.fragment.TestArgsFragment;
import com.tommannson.viewstate.annotations.ActivityArgModel;
import com.tommannson.viewstate.annotations.FragmentArgModel;

/**
 * Created by tomasz.krol on 2017-04-04.
 */

@FragmentArgModel(target = TestArgsFragment.class)
public class FArgModel {

    int data;
}
