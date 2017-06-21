package com.example.tomaszkrol.viewstate.base;


import android.support.v4.app.Fragment;

import com.tommannson.viewstate.Persistable;
import com.tommannson.viewstate.ViewStateBinder;


/**
 * Created by tomasz.krol on 2016-12-01.
 */

public abstract class BaseFragment extends Fragment implements Persistable {

    @Override
    public Object saveCustomState() {
        return ViewStateBinder.persist(this);
    }

    @Override
    public void loadCustomState(Object retainedState) {
        ViewStateBinder.restore(this, retainedState);
    }

}
