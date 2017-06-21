package com.example.tomaszkrol.viewstate.model;

import com.example.tomaszkrol.viewstate.ui.activity.ModelBuilderActivity;
import com.example.tomaszkrol.viewstate.ui.fragment.ModelBuilderFragment;
import com.tommannson.viewstate.annotations.ActivityArgModel;
import com.tommannson.viewstate.annotations.FragmentArgModel;

/**
 * Created by tomasz.krol on 2017-04-05.
 */

@ActivityArgModel(target = ModelBuilderActivity.class)
@FragmentArgModel(target = ModelBuilderFragment.class)
public class UniversalArgModel {

    String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
