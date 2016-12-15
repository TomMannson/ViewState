package com.tommannson.viewstate.processor;

import com.google.auto.service.AutoService;
import com.tommannson.viewstate.annotations.ActivityArg;
import com.tommannson.viewstate.annotations.FragmentArg;
import com.tommannson.viewstate.annotations.ViewData;
import com.tommannson.viewstate.processor.model.ActivityIntentBuilderRenderer;
import com.tommannson.viewstate.processor.model.ModelFactory;
import com.tommannson.viewstate.processor.model.StateBindingRenderer;
import com.tommannson.viewstate.processor.model.VariableBinding;
import com.tommannson.viewstate.processor.utils.AptUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


@AutoService(Processor.class)
public class StatePersisterAnnotationProcessor extends AbstractProcessor {

    private static final String BINDING_CLASS_SUFFIX = "Binder";
    private static final String PRIVATE = "private";
    private static final String STATIC = "static";

    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;

    ModelFactory factory;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();
        filer = env.getFiler();

        factory = new ModelFactory(elementUtils);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();

//        types.add(ViewState.class.getCanonicalName());
        types.add(ViewData.class.getCanonicalName());
        types.add(ActivityArg.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        try {
            Map<TypeElement, StateBindingRenderer> elements = findAndParseBindableTargets(roundEnv);
            Map<TypeElement, ActivityIntentBuilderRenderer> activities = findAndParseActivityTargets(roundEnv);

            for (Map.Entry<TypeElement, StateBindingRenderer> entry : elements.entrySet()) {
                TypeElement typeElement = entry.getKey();
                StateBindingRenderer bindingClass = entry.getValue();

                bindingClass.generateJava().writeTo(filer);
            }

            for (Map.Entry<TypeElement, ActivityIntentBuilderRenderer> entry : activities.entrySet()) {
                TypeElement typeElement = entry.getKey();
                ActivityIntentBuilderRenderer bindingClass = entry.getValue();

                bindingClass.generateJava().writeTo(filer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    private Map<TypeElement, StateBindingRenderer> findAndParseBindableTargets(RoundEnvironment env) {
        Map<TypeElement, StateBindingRenderer> targetClassMap = new LinkedHashMap<>();
        Set<TypeElement> erasedTargetNames = new LinkedHashSet<>();

        // Process each @BindArray element.
        for (Element element : env.getElementsAnnotatedWith(ViewData.class)) {

            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

            StateBindingRenderer binding = factory.getOrCreateTargetClass(targetClassMap, enclosingElement);

            VariableBinding varBind = new VariableBinding();
            varBind.fieldType = element.asType().toString();
            varBind.fieldTypeMinor = element.asType();
            varBind.fieldName = element.getSimpleName().toString();
            varBind.isPrimitive = AptUtils.isPrimitive(element);
            binding.variables.add(varBind);

        }

        return targetClassMap;
    }

    private Map<TypeElement, ActivityIntentBuilderRenderer> findAndParseActivityTargets(RoundEnvironment env) {
        Map<TypeElement, ActivityIntentBuilderRenderer> targetClassMap = new LinkedHashMap<>();
        Set<TypeElement> erasedTargetNames = new LinkedHashSet<>();

        // Process each @BindArray element.
        for (Element element : env.getElementsAnnotatedWith(ActivityArg.class)) {

            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

            ActivityIntentBuilderRenderer binding = factory.getOrCreateActivityRendererClass(targetClassMap, enclosingElement);

            VariableBinding varBind = new VariableBinding();
            varBind.fieldType = element.asType().toString();
            varBind.fieldTypeMinor = element.asType();
            varBind.fieldName = element.getSimpleName().toString();
            varBind.isPrimitive = AptUtils.isPrimitive(element);

            TypeElement serialType = elementUtils.getTypeElement(Serializable.class.getCanonicalName());
            TypeElement parcelableType = elementUtils.getTypeElement("android.os.Parcelable");
            TypeElement arrayListType = elementUtils.getTypeElement(ArrayList.class.getCanonicalName());
            TypeElement charSequenceType = elementUtils.getTypeElement(CharSequence.class.getCanonicalName());
            TypeElement integerType = elementUtils.getTypeElement(Integer.class.getCanonicalName());
            TypeElement stringType = elementUtils.getTypeElement(String.class.getCanonicalName());



//                    TypeElement charSequence = elementUtils.getTypeElement("android.os.Parcelable");
            if (!varBind.isPrimitive) {

                TypeElement type = (TypeElement) typeUtils.asElement(varBind.fieldTypeMinor);
                if (type != null) {

                    TypeMirror testElement = element.asType();

                    if (typeUtils.isAssignable(arrayListType.asType(), type.asType())) {
                        varBind.isArrayList = true;
                        DeclaredType inner = ((DeclaredType)element.asType());
                        if(inner.getTypeArguments().size() == 1) {
                            testElement = inner.getTypeArguments().get(0);

                            varBind.isCharSequence = typeUtils.isAssignable(testElement, charSequenceType.asType());
                            varBind.isInteger = typeUtils.isAssignable(testElement, integerType.asType());
                            varBind.isString = typeUtils.isAssignable(testElement, stringType.asType());
                            varBind.isParcelable = typeUtils.isAssignable(testElement, parcelableType.asType());
                        }
                    }
                }

                varBind.isSerializable = typeUtils.isAssignable(varBind.fieldTypeMinor, serialType.asType());


            }

            binding.variables.add(varBind);

        }

        return targetClassMap;
    }

    private Map<TypeElement, StateBindingRenderer> findAndParseFragmentTargets(RoundEnvironment env) {
        Map<TypeElement, StateBindingRenderer> targetClassMap = new LinkedHashMap<>();
        Set<TypeElement> erasedTargetNames = new LinkedHashSet<>();

        // Process each @BindArray element.
        for (Element element : env.getElementsAnnotatedWith(FragmentArg.class)) {

            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

            StateBindingRenderer binding = factory.getOrCreateTargetClass(targetClassMap, enclosingElement);

            VariableBinding varBind = new VariableBinding();
            varBind.fieldType = element.asType().toString();
            varBind.fieldTypeMinor = element.asType();
            varBind.fieldName = element.getSimpleName().toString();
            varBind.isPrimitive = AptUtils.isPrimitive(element);
            binding.variables.add(varBind);

        }

        return targetClassMap;
    }
}
