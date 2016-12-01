package pl.tommannson.viewstate.processor.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import pl.tommannson.viewstate.processor.utils.AptUtils;


/**
 * Created by tomasz.krol on 2016-05-11.
 */
public class ModelFactory {

    private static final String BINDING_CLASS_SUFFIX = "Binder";

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
}