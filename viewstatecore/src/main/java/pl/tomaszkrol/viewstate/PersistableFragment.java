package pl.tomaszkrol.viewstate;

import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by tomasz.krol on 2016-12-01.
 */

public abstract class PersistableFragment extends Fragment implements Persistable {


    public void loadChildStateState(Object retainedState) {

        Object[] savedState = (Object[]) retainedState;

        if (savedState != null) {
            List<Fragment> fragments = getChildFragmentManager().getFragments();
            int fragmentCount = fragments.size();

            for (int stateIterator = 0; stateIterator < fragmentCount; stateIterator++) {

                Fragment fragmentToLoad = (Fragment) fragments.get(stateIterator);
                if (fragmentToLoad instanceof PersistableFragment) {
                    PersistableFragment persistableFragment = (PersistableFragment) fragmentToLoad;
                    persistableFragment.loadCustomState(savedState[stateIterator]);
                }
            }
        }
    }

    public Object saveChildStateState() {

        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments == null) {
            return null;
        }
        int fragmentCount = fragments.size();
        Object[] configState = new Object[fragmentCount * 2 + 1];

        for (int stateIterator = 0; stateIterator < fragmentCount * 2; stateIterator++) {
            if (fragments.get(stateIterator) instanceof Persistable) {
                Persistable fragmentToLoad = (Persistable) fragments.get(stateIterator);
                configState[stateIterator] = fragmentToLoad.saveCustomState();
                stateIterator++;
                if (fragmentToLoad instanceof PersistableFragment) {
                    PersistableFragment persistableFragment = (PersistableFragment) fragmentToLoad;
                    configState[stateIterator] = persistableFragment.saveChildStateState();
                }
            }
            else{
                stateIterator++;
            }
        }

        configState[fragmentCount * 2] = this.saveCustomState();

        return configState;
    }

}
