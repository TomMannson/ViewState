package com.tommannson.viewstate.processor.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import com.tommannson.viewstate.processor.utils.AptUtils;


/**
 * Created by tomasz.krol on 2016-05-11.
 */
public class ModelFactory {

    private static final String BINDING_CLASS_SUFFIX = "Binder";
    private static final String INTENT_BUILDER_CLASS_SUFFIX = "IntentBuilder";
    private static final String FRAGMENT_BUILDER_CLASS_SUFFIX = "Builder";

    private Elements elementUtils;

    public ModelFactory(Elements elementUtils) {
        this.elementUtils = elementUtils;
    }

    public StateBindingRenderer getOrCreateTargetClass(Map<TypeElement, StateBindingRenderer> targetClassMap,
                                                TypeElement enclosingElement) {
        StateBindingRenderer bindingClass = targetClassMap.get(enclosingElement);
        if (bindingClass == null) {
            TypeName targetType = TypeName.get(enclosingElement.asType());
            if (targetType instanceof ParameterizedTypeName) {
                targetType = ((ParameterizedTypeName) targetType).rawType;
            }

            String packageName = AptUtils.getPackageName(elementUtils, enclosingElement);
            ClassName classFqcn = ClassName.get(packageName,
                    AptUtils.getClassName(enclosingElement, packageName) + BINDING_CLASS_SUFFIX);
            ClassName targetClassName = ClassName.get(packageName,
                    AptUtils.getClassName(enclosingElement, packageName));

            boolean isFinal = enclosingElement.getModifiers().contains(Modifier.FINAL);

            bindingClass = new StateBindingRenderer(classFqcn, targetClassName, false);
            targetClassMap.put(enclosingElement, bindingClass);
        }
        return bindingClass;
    }

    public ActivityIntentBuilderRenderer getOrCreateActivityRendererClass(Map<TypeElement, ActivityIntentBuilderRenderer> targetClassMap,
                                                       TypeElement enclosingElement) {
        ActivityIntentBuilderRenderer renderer = targetClassMap.get(enclosingElement);
        if (renderer == null) {
            TypeName targetType = TypeName.get(enclosingElement.asType());
            if (targetType instanceof ParameterizedTypeName) {
                targetType = ((ParameterizedTypeName) targetType).rawType;
            }

            String packageName = AptUtils.getPackageName(elementUtils, enclosingElement);
            ClassName classFqcn = ClassName.get(packageName,
                    AptUtils.getClassName(enclosingElement, packageName) + INTENT_BUILDER_CLASS_SUFFIX);
            ClassName targetClassName = ClassName.get(packageName,
                    AptUtils.getClassName(enclosingElement, packageName));

            renderer = new ActivityIntentBuilderRenderer(classFqcn, targetClassName);
            targetClassMap.put(enclosingElement, renderer);
        }
        return renderer;
    }

    public FragmentBuilderRenderer getOrCreateFragmentRendererClass(Map<TypeElement, FragmentBuilderRenderer> targetClassMap,
                                                                          TypeElement enclosingElement) {
        FragmentBuilderRenderer renderer = targetClassMap.get(enclosingElement);
        if (renderer == null) {
            TypeName targetType = TypeName.get(enclosingElement.asType());
            if (targetType instanceof ParameterizedTypeName) {
                targetType = ((ParameterizedTypeName) targetType).rawType;
            }

            String packageName = AptUtils.getPackageName(elementUtils, enclosingElement);
            ClassName classFqcn = ClassName.get(packageName,
                    AptUtils.getClassName(enclosingElement, packageName) + FRAGMENT_BUILDER_CLASS_SUFFIX);
            ClassName targetClassName = ClassName.get(packageName,
                    AptUtils.getClassName(enclosingElement, packageName));

            renderer = new FragmentBuilderRenderer(classFqcn, targetClassName);
            targetClassMap.put(enclosingElement, renderer);
        }
        return renderer;
    }

    public ActivityModelBuilderRenderer getOrCreateActivityModelRendererClass(Map<TypeElement, ActivityModelBuilderRenderer> targetClassMap,
                                                                          TypeElement enclosingElement, ClassName[] classes) {
        ActivityModelBuilderRenderer renderer = targetClassMap.get(enclosingElement);
        if (renderer == null) {
            TypeName targetType = TypeName.get(enclosingElement.asType());
            if (targetType instanceof ParameterizedTypeName) {
                targetType = ((ParameterizedTypeName) targetType).rawType;
            }

            String packageName = AptUtils.getPackageName(elementUtils, enclosingElement);
            ClassName classFqcn = ClassName.get(packageName,
                    AptUtils.getClassName(enclosingElement, packageName) + INTENT_BUILDER_CLASS_SUFFIX);
            ClassName targetClassName = ClassName.get(packageName,
                    AptUtils.getClassName(enclosingElement, packageName));

//            ClassName[] activityClasses = new ClassName[classes.length];
//            for (int i = 0; i < classes.length; i++){
//                activityClasses[i] = ClassName.get(classes[i]);
//            }

            renderer = new ActivityModelBuilderRenderer(classFqcn, targetClassName, classes);
            targetClassMap.put(enclosingElement, renderer);
        }
        return renderer;
    }
}
