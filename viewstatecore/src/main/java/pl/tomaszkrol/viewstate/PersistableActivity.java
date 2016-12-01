package pl.tomaszkrol.viewstate;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by tomasz.krol on 2016-12-01.
 */

public abstract class PersistableActivity extends AppCompatActivity implements Persistable {

    public void loadChildStateState(Object retainedState){

        Object[] savedState = (Object[]) retainedState;

        if (savedState != null) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            int fragmentCount = fragments.size();

            for (int stateIterator = 0; stateIterator < fragmentCount; stateIterator++) {
//                BaseFragment fragmentToLoad = (BaseFragment) fragments.get(stateIterator);
//                fragmentToLoad.loadCustomState(savedState[stateIterator]);
            }
        }
    }

}
