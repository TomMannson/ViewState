package com.tommannson.viewstate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tomasz.krol on 2017-04-04.
 */

@Retention(RetentionPolicy.SOURCE)
@Target(value= ElementType.TYPE)
public @interface FragmentArgModel {
    public Class<?> target();
    public Class<?>[] targets() default {};
}
