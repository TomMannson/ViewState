package com.tommannson.viewstate.processor.model;

import javax.lang.model.type.TypeMirror;

/**
 * Created by tomasz.krol on 2016-05-10.
 */
public class VariableBinding {
    public String fieldType;
    public String fieldName;
    public boolean isPrimitive;
    public TypeMirror fieldTypeMinor;
}
