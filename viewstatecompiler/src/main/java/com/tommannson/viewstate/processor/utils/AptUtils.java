package com.tommannson.viewstate.processor.utils;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Created by tomasz.krol on 2016-05-11.
 */
public class AptUtils {

    public static String getPackageName(Elements elementUtils, TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    public static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    public static boolean isPrimitive(TypeMirror input) {

        try {
            PrimitiveType type = (PrimitiveType) input;
            if (type != null)
                return true;
        } catch (Exception ex) {
        }

        return false;
    }

    public static boolean isArray(Element element) {
        try {
            ArrayType type = (ArrayType) element.asType();
            if (type != null)
                return true;
        } catch (Exception ex) {
        }

        return false;
    }

//    private boolean isInaccessibleViaGeneratedCode(Class<? extends Annotation> annotationClass,
//                                                   String targetThing, Element element) {
//        boolean hasError = false;
//        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
//
//        // Verify method modifiers.
//        Set<Modifier> modifiers = element.getModifiers();
//        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
//            error(element, "@%s %s must not be private or static. (%s.%s)",
//                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
//                    element.getSimpleName());
//            hasError = true;
//        }
//
//        // Verify containing type.
//        if (enclosingElement.getKind() != CLASS) {
//            error(enclosingElement, "@%s %s may only be contained in classes. (%s.%s)",
//                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
//                    element.getSimpleName());
//            hasError = true;
//        }
//
//        // Verify containing class visibility is not private.
//        if (enclosingElement.getModifiers().contains(PRIVATE)) {
//            error(enclosingElement, "@%s %s may not be contained in private classes. (%s.%s)",
//                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
//                    element.getSimpleName());
//            hasError = true;
//        }
//
//        return hasError;
//    }

//    public static boolean isSubTypeOfType(){
//        TypeMirror parameterTypes = mirror.getParameterTypes();
//        TypeMirror typeOfE = processingEnv.getElementUtils().getTypeElement(E.class.getName()).asType();
//        boolean isSubTypeOfE = processingEnv.getTypeUtils().isSubtype(parameterType, eventType)
//    }
}
