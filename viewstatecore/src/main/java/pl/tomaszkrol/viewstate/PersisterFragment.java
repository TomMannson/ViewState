package pl.tomaszkrol.viewstate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomasz.krol on 2016-05-09.
 */
public class PersisterFragment extends Fragment {

    public static final String FRAGMENT_TAG =
            "STORAGE_FRAGMENT_" + PersisterFragment.class.getCanonicalName();
    Map<String, Object> storage = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void saveData(String name, Object data) {
        storage.put(name, data);
    }

    public Object loadData(String name) {
        return storage.get(name);
    }


    public static PersisterFragment injectPresistFragment(FragmentActivity activity) {
        PersisterFragment persistFragment = (PersisterFragment) activity.getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_TAG);

        if (persistFragment == null) {
            persistFragment = new PersisterFragment();
            activity.getSupportFragmentManager().beginTransaction().add(persistFragment, FRAGMENT_TAG)
                    .commit();
        }

        return persistFragment;
    }
}
