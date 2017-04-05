package com.example.tomaszkrol.viewstate.model;

import com.example.tomaszkrol.viewstate.ui.activity.UniverstalActivity;
import com.example.tomaszkrol.viewstate.ui.fragment.UniversalFragment;
import com.tommannson.viewstate.annotations.ActivityArgModel;
import com.tommannson.viewstate.annotations.FragmentArgModel;

/**
 * Created by tomasz.krol on 2017-04-05.
 */

@ActivityArgModel(target = UniverstalActivity.class)
@FragmentArgModel(target = UniversalFragment.class)
public class UniversalArgModel {

    String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
