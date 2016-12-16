package com.tommannson.viewstate.processor.model;

import javax.lang.model.type.TypeMirror;

/**
 * Created by tomasz.krol on 2016-05-10.
 */
public class VariableBinding {
    public boolean isPrimitive;
    public String fieldType;
    public TypeMirror fieldTypeMinor;
    public TypeMirror subType;
    public String fieldName;

    public boolean isPrimitiveSubType;
    public boolean isArray;
    public boolean isArrayList;


    public boolean isSerializable;
    public boolean isParcelable;




    @Override
    public String toString() {
        return "VariableBinding{" +
                "subType=" + subType +
                ", fieldTypeMinor=" + fieldTypeMinor +
                '}';
    }
}
