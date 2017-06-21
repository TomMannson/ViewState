package com.tommannson.viewstate;

/**
 * Created by tomasz.krol on 2017-06-21.
 */

public interface Binder<Target> {

    void restore(Target target, Object data);

    Object persist(Target target);
}
