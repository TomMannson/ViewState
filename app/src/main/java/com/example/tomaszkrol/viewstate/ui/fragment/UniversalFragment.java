package com.example.tomaszkrol.viewstate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomaszkrol.viewstate.R;
import com.example.tomaszkrol.viewstate.model.UniversalArgModel;
import com.example.tomaszkrol.viewstate.model.UniversalArgModelBuilder;

/**
 * Created by tomasz.krol on 2017-04-05.
 */

public class UniversalFragment extends Fragment {

    UniversalArgModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = UniversalArgModelBuilder.getDataFromIntent(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_universal, container, false);

    }
}
