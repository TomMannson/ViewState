package com.example.tomaszkrol.viewstate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tomaszkrol.viewstate.base.BaseFragment;
import com.tommannson.viewstate.annotations.ViewData;


/**
 * Created by tomasz.krol on 2016-12-01.
 */

public class ViewDataDynamicFragment extends BaseFragment {

    @ViewData
    String contentToPersist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView tv = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, container, false);
        if (contentToPersist == null) {
            contentToPersist = "Dynamic";
        } else {
            contentToPersist = contentToPersist + " recreated";
        }

        tv.setText(contentToPersist);
        return tv;
    }
}
