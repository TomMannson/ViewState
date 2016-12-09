package com.tommannson.viewstate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.List;

/**
 * Created by tomasz.krol on 2016-12-01.
 */
public class ActivityPersistLoader {

    public static void load(FragmentActivity activity) {

        Object savedState = activity.getLastCustomNonConfigurationInstance();

        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
        int fragmentCount = 0;
        if (fragments != null) {
            fragmentCount = fragments.size();
        }
        Bundle bb = new Bundle();


        Object[] configState = (Object[]) savedState;

        if (savedState != null) {
            int fragmentPosition = 0;

            if (fragments != null) {
                for (int stateIterator = 0; fragmentPosition < fragments.size(); stateIterator++) {
                    if ((fragments.get(fragmentPosition) instanceof Persistable)) {
                        Persistable fragmentToLoad = (Persistable) fragments.get(fragmentPosition);
                        fragmentToLoad.loadCustomState(configState[stateIterator]);
                        stateIterator++;
                        fragmentPosition++;
                        if (fragmentToLoad instanceof PersistableFragment)
                            ((PersistableFragment) fragmentToLoad).loadChildStateState(configState[stateIterator]);

                    } else {
                        stateIterator++;
                        fragmentPosition++;
                    }
                }
            }
            if (activity instanceof Persistable) {
                Persistable pActivity = (Persistable) activity;
                pActivity.loadCustomState(configState[fragmentCount * 2]);
            }
        }
    }

    public static Object persist(FragmentActivity activity) {
        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
        int fragmentCount = 0;
        if (fragments != null) {
            fragmentCount = fragments.size();
        }
        Object[] configState = new Object[fragmentCount * 2 + 1];

        int fragmentPosition = 0;
        if (fragments != null) {
            for (int stateIterator = 0; fragmentPosition < fragments.size(); stateIterator++) {
                if (fragments.get(fragmentPosition) instanceof Persistable) {
                    Persistable fragmentToLoad = (Persistable) fragments.get(fragmentPosition);
                    configState[stateIterator] = fragmentToLoad.saveCustomState();
                    stateIterator++;
                    fragmentPosition++;
                    if (fragmentToLoad instanceof PersistableFragment) {
                        PersistableFragment pFragment = (PersistableFragment) fragmentToLoad;
                        configState[stateIterator] = pFragment.saveChildStateState();
                    }
                } else {
                    stateIterator++;
                    fragmentPosition++;
                }
            }
        }
        if (activity instanceof Persistable) {
            Persistable pActivity = (Persistable) activity;
            configState[fragmentCount * 2] = pActivity.saveCustomState();
        }


        return configState;
    }
}
