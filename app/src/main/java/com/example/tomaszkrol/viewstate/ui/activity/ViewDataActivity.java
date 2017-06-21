package com.example.tomaszkrol.viewstate.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.tomaszkrol.viewstate.R;
import com.example.tomaszkrol.viewstate.base.BaseActivity;
import com.example.tomaszkrol.viewstate.ui.fragment.ViewDataDynamicFragment;
import com.tommannson.viewstate.annotations.ViewData;


public class ViewDataActivity extends BaseActivity {

    @ViewData
    Integer data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportFragmentManager().getFragments().size() == 1) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.dynamic_fragment, new ViewDataDynamicFragment(), "")
                    .commit();
        }

        if (data == null) {
            data = 0;
        } else {
            data++;
            Toast.makeText(this, "Activity recreated " + data + " times", Toast.LENGTH_SHORT).show();
        }

//        ArrayList<Data> data = new ArrayList<>();
//        data.add(new Data());
//        new TestArgsFragmentBuilder()
//                .serializableArrayList(data)
//                .build()
    }

    public void recreateActivity(View view) {
        recreate();
    }
}
