package com.tommannson.viewstate.processor;

import com.google.auto.service.AutoService;
import com.tommannson.viewstate.annotations.ActivityArg;
import com.tommannson.viewstate.annotations.FragmentArg;
import com.tommannson.viewstate.annotations.ViewData;
import com.tommannson.viewstate.processor.model.ActivityIntentBuilderRenderer;
import com.tommannson.viewstate.processor.model.FragmentBuilderRenderer;
import com.tommannson.viewstate.processor.model.ModelFactory;
import com.tommannson.viewstate.processor.model.StateBindingRenderer;
import com.tommannson.viewstate.processor.model.VariableBinding;
import com.tommannson.viewstate.processor.utils.AptUtils;

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
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


@AutoService(Processor.class)
public class StatePersisterAnnotationProcessor extends AbstractProcessor {

    static public Elements elementUtils;
    public static Types typeUtils;
    public static Filer filer;

    ModelFactory factory;
    private TypeElement arrayListType;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();
        filer = env.getFiler();

        factory = new ModelFactory(elementUtils);
        arrayListType = elementUtils.getTypeElement("java.util.ArrayList");
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
            Map<TypeElement, FragmentBuilderRenderer> fragments = findAndParseFragmentTargets(roundEnv);

            for (Map.Entry<TypeElement, StateBindingRenderer> entry : elements.entrySet()) {

                StateBindingRenderer bindingClass = entry.getValue();
                bindingClass.generateJava().writeTo(filer);
            }

            for (Map.Entry<TypeElement, ActivityIntentBuilderRenderer> entry : activities.entrySet()) {
                ActivityIntentBuilderRenderer bindingClass = entry.getValue();
                bindingClass.generateJava().writeTo(filer);
            }

            for (Map.Entry<TypeElement, FragmentBuilderRenderer> entry : fragments.entrySet()) {

                FragmentBuilderRenderer bindingClass = entry.getValue();
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

        for (Element element : env.getElementsAnnotatedWith(ViewData.class)) {

            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

            StateBindingRenderer binding = factory.getOrCreateTargetClass(targetClassMap, enclosingElement);

            VariableBinding varBind = new VariableBinding();
            varBind.fieldType = element.asType().toString();
            varBind.fieldTypeMinor = element.asType();
            varBind.fieldName = element.getSimpleName().toString();
            varBind.isPrimitive = AptUtils.isPrimitive(element.asType());
            binding.variables.add(varBind);
        }

        return targetClassMap;
    }

    private Map<TypeElement, ActivityIntentBuilderRenderer> findAndParseActivityTargets(RoundEnvironment env) {
        Map<TypeElement, ActivityIntentBuilderRenderer> targetClassMap = new LinkedHashMap<>();

        for (Element element : env.getElementsAnnotatedWith(ActivityArg.class)) {

            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

            ActivityIntentBuilderRenderer binding = factory.getOrCreateActivityRendererClass(targetClassMap, enclosingElement);
            VariableBinding varBind = exportVariableInfo(element);
            binding.variables.add(varBind);
        }

        return targetClassMap;
    }

    private Map<TypeElement, FragmentBuilderRenderer> findAndParseFragmentTargets(RoundEnvironment env) {
        Map<TypeElement, FragmentBuilderRenderer> targetClassMap = new LinkedHashMap<>();

        for (Element element : env.getElementsAnnotatedWith(FragmentArg.class)) {

            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

            FragmentBuilderRenderer binding = factory.getOrCreateFragmentRendererClass(targetClassMap, enclosingElement);
            VariableBinding varBind = exportVariableInfo(element);
            binding.variables.add(varBind);
        }

        return targetClassMap;
    }


    private VariableBinding exportVariableInfo(Element element) {
        VariableBinding varBind = new VariableBinding();
        varBind.fieldType = element.asType().toString();
        varBind.fieldTypeMinor = element.asType();
        varBind.fieldName = element.getSimpleName().toString();
        varBind.isPrimitive = AptUtils.isPrimitive(element.asType());
        varBind.isArray = AptUtils.isArray(element);

        if (!varBind.isPrimitive) {

            TypeElement type = (TypeElement) typeUtils.asElement(varBind.fieldTypeMinor);
            if (type != null) {

                if (typeUtils.isAssignable(type.asType(), arrayListType.asType())) {

                    varBind.isArrayList = true;
                    DeclaredType inner = ((DeclaredType)element.asType());

                    if(inner.getTypeArguments().size() == 1) {

                        varBind.subType = inner.getTypeArguments().get(0);
                    }
                }
            }
            else if(varBind.isArray){

                ArrayType arrayType = (ArrayType)element.asType();
                varBind.subType = arrayType.getComponentType();
                varBind.isPrimitiveSubType = AptUtils.isPrimitive(varBind.subType);
            }
        }
        return varBind;
    }
}
