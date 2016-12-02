package pl.tommannson.viewstate;

/**
 * Created by tomasz.krol on 2016-12-01.
 */

public interface Persistable {

    Object saveCustomState();
    void loadCustomState(Object retainedState);

}
