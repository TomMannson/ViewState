package com.tommannson.viewstate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tomasz.krol on 2016-05-12.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value= ElementType.FIELD)
public @interface ActivityArg {
}
