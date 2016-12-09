package com.tommannson.viewstate.processor.model;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.lang.model.type.TypeMirror;

import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Created by tomasz.krol on 2016-05-10.
 */
public class ActivityIntentBuilderRenderer {

    private static final String GENERATED_COMMENTS = "beta version ";

    private static final AnnotationSpec GENERATED =
            AnnotationSpec.builder(Generated.class)
                    .addMember("value", "$S", ActivityIntentBuilderRenderer.class.getName())
                    .addMember("comments", "$S", GENERATED_COMMENTS)
                    .build();


//    private static final ClassName CONTEXT = ClassName.get("android.content", "Context");
//    private static final ClassName PERSIST_FRAGMENT = ClassName.get("pl.tomaszkrol.viewstate", "PersisterFragment");
//    private static final ClassName FRAGMENT_ACTIVITY = ClassName.get("android.support.v4.app", "FragmentActivity");

    public ClassName generetedClassName;
    public ClassName targetClassName;
    public List<VariableBinding> variables = new ArrayList<>();

    public ActivityIntentBuilderRenderer(ClassName generetedClassName, ClassName targetClassName) {
        this.generetedClassName = generetedClassName;
        this.targetClassName = targetClassName;
    }

    public JavaFile generateJava() {
        TypeSpec.Builder result = TypeSpec.classBuilder(generetedClassName)
                .addAnnotation(GENERATED)
                .addModifiers(PUBLIC);

        addVariablesSection(result);
        addMethodsSection(result);
        addBuildMethodsSection(result);

        return JavaFile.builder(generetedClassName.packageName(), result.build())
                .addFileComment("Generated code do not modify!")
                .build();
    }

    private void addVariablesSection(TypeSpec.Builder result) {
        for (VariableBinding variable : variables) {
            result.addField(FieldSpec.builder(TypeName.get(variable.fieldTypeMinor), variable.fieldName).build());
        }
    }

    private void addMethodsSection(TypeSpec.Builder result) {
        for (VariableBinding variable : variables) {

            MethodSpec method = MethodSpec.methodBuilder(variable.fieldName)
                    .addModifiers(PUBLIC)
                    .addParameter(TypeName.get(variable.fieldTypeMinor), "var")
                    .returns(generetedClassName)
                    .addStatement("this." + variable.fieldName + " = var")
                    .addStatement("return this")
                    .build();

            result.addMethod(method);
        }
    }

    private void addBuildMethodsSection(TypeSpec.Builder result) {

        ClassName bundleClass = ClassName.get("android.os", "Bundle");

        MethodSpec.Builder method = MethodSpec.methodBuilder("build")
                .addModifiers(PUBLIC)
                .addParameter(ClassName.get("android.content", "Context"), "ctx")
                .returns(ClassName.get("android.content", "Intent"))
                .addStatement("Intent starter = new Intent(ctx, " + targetClassName.simpleName() + ".class"  )
                .addStatement("$T bundle = new $T()", bundleClass, bundleClass);

        for (VariableBinding variable : variables) {


            method.addStatement()
        }


        method.addStatement("return starter");

        result.addMethod(method.build());
    }

    private String selectMethodNameForTypeVariable(VariableBinding variable){
        if(variable.isPrimitive){
            if(TypeName.get(variable.fieldTypeMinor).equals(TypeName.BOOLEAN)){
                return "putBoolean";
            }
            else if(TypeName.get(variable.fieldTypeMinor).equals(TypeName.BYTE)){
                return "putByte";
            }
            else if(TypeName.get(variable.fieldTypeMinor).equals(TypeName.CHAR)){

            }
            else if(TypeName.get(variable.fieldTypeMinor).equals(TypeName.DOUBLE)){

            }
            else if(TypeName.get(variable.fieldTypeMinor).equals(TypeName.FLOAT)){

            }
            else if(TypeName.get(variable.fieldTypeMinor).equals(TypeName.INT)){

            }
            else if(TypeName.get(variable.fieldTypeMinor).equals(TypeName.LONG)){

            }
            else if(TypeName.get(variable.fieldTypeMinor).equals(TypeName.SHORT)){

            }
        }
    }



    private MethodSpec createPersistMethod() {

        String holderClassName = targetClassName.simpleName() + "_Holder";

        MethodSpec.Builder result = MethodSpec.methodBuilder("persist")
                .addModifiers(PUBLIC)
                .addModifiers(STATIC)
                .addParameter(targetClassName, "target")
                .returns(TypeName.OBJECT)
                .addStatement(holderClassName + " holder = new " + holderClassName + "()");
//

        for (VariableBinding variable : variables) {
            String ass = String.format("holder.%s = target.%s", variable.fieldName, variable.fieldName);
            result.addStatement(ass);
        }

        result.addStatement("return holder");

        return result.build();
    }

    private MethodSpec createRestoreMethod() {

        String holderClassName = targetClassName.simpleName() + "_Holder";
        MethodSpec.Builder result = MethodSpec.methodBuilder("restore")
                .addModifiers(PUBLIC)
                .addModifiers(STATIC)
                .addParameter(targetClassName, "target")
                .addParameter(TypeName.OBJECT, "data");

        result.beginControlFlow(" if( data != null )");
        result.addStatement(holderClassName + " holder = (" + holderClassName + ")data");

        for (VariableBinding variable : variables) {

            String asignement = String.format("target.%s = holder.%s", variable.fieldName,
                    variable.fieldName);
            result.addStatement(asignement, targetClassName);
        }

        result.endControlFlow();
        return result.build();
    }

    private TypeSpec createHolderClass() {

        TypeSpec.Builder result = TypeSpec.classBuilder(targetClassName.simpleName() + "_Holder")
                .addModifiers(PUBLIC)
                .addModifiers(STATIC);

        for (VariableBinding variable : variables) {
            result.addField(FieldSpec.builder(TypeName.get(variable.fieldTypeMinor), variable.fieldName).build());
        }

        return result.build();
    }
}
