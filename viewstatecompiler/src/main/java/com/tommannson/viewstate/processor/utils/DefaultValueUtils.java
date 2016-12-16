package com.tommannson.viewstate.processor.utils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomasz.krol on 2016-12-15.
 */

public class DefaultValueUtils {

    static Map<TypeName, String> defaultValueMaping = new HashMap<>();

    static {
        defaultValueMaping.put(ClassName.BOOLEAN, "false");
        defaultValueMaping.put(ClassName.BYTE, "(byte) 0");
        defaultValueMaping.put(ClassName.SHORT, "(short) 0");
        defaultValueMaping.put(ClassName.INT, "0");
        defaultValueMaping.put(ClassName.LONG, "0L");
        defaultValueMaping.put(ClassName.CHAR, "0");
        defaultValueMaping.put(ClassName.DOUBLE, "0.0");
        defaultValueMaping.put(ClassName.FLOAT, "0.0f");

    }

    public static String getDefaultValue(TypeName className, String initValue) {
        if (initValue != null) {
            return initValue;
        }
        if (className != null) {
            String defaultValueForPrimitive = defaultValueMaping.get(className);

            if (defaultValueForPrimitive != null) {
                return defaultValueForPrimitive;
            }
        }
        return "null";
    }

    public static String toNoTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toLowerCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

}
