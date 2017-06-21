package com.tommannson.viewstate.processor.model;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Generated;
import javax.lang.model.element.TypeElement;

import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Created by tomasz.krol on 2017-06-21.
 */

public class BinderRenderer {

    public static final String NAME_OF_MAP_FIELD = "mapOfBinders";

    private static final String GENERATED_COMMENTS = "beta version ";
    private static final AnnotationSpec GENERATED =
            AnnotationSpec.builder(Generated.class)
                    .addMember("value", "$S", StateBindingRenderer.class.getName())
                    .addMember("comments", "$S", GENERATED_COMMENTS)
                    .build();

    Set<Map.Entry<TypeElement, StateBindingRenderer>> setToGenerateMap = null;

    public ClassName generetedClassName;

    public BinderRenderer(Set<Map.Entry<TypeElement, StateBindingRenderer>> setToGenerateMap) {
        this.setToGenerateMap = setToGenerateMap;
        generetedClassName = ClassName.bestGuess("com.tommannson.viewstate.ViewStateBinder");
    }

    public JavaFile generateJava() {

        TypeSpec.Builder result = TypeSpec.classBuilder(generetedClassName)
                .addAnnotation(GENERATED)
                .addModifiers(PUBLIC);

        result.addField(createMap());
        result.addMethod(initMap());
        result.addMethod(createRestoreMethod());
        result.addMethod(createPersistMethod());

        return JavaFile.builder(generetedClassName.packageName(), result.build())
                .addFileComment("Generated code do not modify!")
                .build();
    }

    private FieldSpec createMap() {

        ClassName mapClassName = ClassName.get(Map.class);
        ClassName genericClassName = ClassName.get(Class.class);
        ClassName binderClassName = ClassName.bestGuess("com.tommannson.viewstate.Binder");
        TypeName parametrizedMap = ParameterizedTypeName.get(mapClassName, genericClassName, binderClassName);

        return FieldSpec.builder(parametrizedMap, NAME_OF_MAP_FIELD, PRIVATE, STATIC).build();
    }

    private MethodSpec initMap() {


        MethodSpec.Builder builder = MethodSpec.methodBuilder("init")
                .addModifiers(STATIC)
                .beginControlFlow("if(" + NAME_OF_MAP_FIELD + " == null)");

        builder.addStatement(NAME_OF_MAP_FIELD + " = new java.util.HashMap<Class, Binder>()");
        List<Map.Entry<TypeElement, StateBindingRenderer>> list = new ArrayList<>(setToGenerateMap);
        for (int i = 0; i < list.size(); i++) {
            StateBindingRenderer item = list.get(i).getValue();
            builder.addStatement(NAME_OF_MAP_FIELD + ".put(" + item.targetClassName + ".class, new " + item.generetedClassName + "())");
        }
        builder.endControlFlow();

        return builder.build();
    }

    private MethodSpec createRestoreMethod() {
        String holderClassName = generetedClassName.simpleName() + "_Holder";
        MethodSpec.Builder result = MethodSpec.methodBuilder("restore")
                .addModifiers(PUBLIC)
                .addModifiers(STATIC)
                .addParameter(TypeName.OBJECT, "target")
                .addParameter(TypeName.OBJECT, "data");

        result.beginControlFlow(" if( data != null )");
        result.addStatement("init();");
        ClassName binderClassName = ClassName.bestGuess("com.tommannson.viewstate.Binder");
        result.addStatement(binderClassName + " binder = mapOfBinders.get(target.getClass())");
        result.beginControlFlow("if(binder != null)");
        result.addStatement("binder.restore(target, data)");
        result.endControlFlow();
        result.endControlFlow();
        return result.build();
    }

    private MethodSpec createPersistMethod() {
        String holderClassName = generetedClassName.simpleName() + "_Holder";
        MethodSpec.Builder result = MethodSpec.methodBuilder("persist")
                .addModifiers(PUBLIC)
                .addModifiers(STATIC)
                .returns(TypeName.OBJECT)
                .addParameter(TypeName.OBJECT, "target");

        result.addStatement("init();");
        ClassName binderClassName = ClassName.bestGuess("com.tommannson.viewstate.Binder");
        result.addStatement(binderClassName + " binder = mapOfBinders.get(target.getClass())");
        result.beginControlFlow("if(binder != null)");
        result.addStatement("return binder.persist(target)");
        result.endControlFlow();
        result.beginControlFlow("else");
        result.addStatement("return null");
        result.endControlFlow();


        return result.build();
    }
}
