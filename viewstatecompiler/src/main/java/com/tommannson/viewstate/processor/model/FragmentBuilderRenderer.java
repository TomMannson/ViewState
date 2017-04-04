package com.tommannson.viewstate.processor.model;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.tommannson.viewstate.processor.StatePersisterAnnotationProcessor;
import com.tommannson.viewstate.processor.utils.CaseUtils;
import com.tommannson.viewstate.processor.utils.DefaultValueUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Created by tomasz.krol on 2016-05-10.
 */
public class FragmentBuilderRenderer {

    private static final String GENERATED_COMMENTS = "beta version ";

    private String[] acceptableForReadArray =
            new String[]{"android.os.Parcelable",
                    String.class.getCanonicalName(),
                    CharSequence.class.getCanonicalName()};

    private String[] acceptableForReadArrayList =
            new String[]{"android.os.Parcelable",
                    Integer.class.getCanonicalName(),
                    String.class.getCanonicalName(),
                    CharSequence.class.getCanonicalName()};

    private String[] acceptableForRead =
            new String[]{"android.os.Parcelable",
                    Integer.class.getCanonicalName(),
                    Serializable.class.getCanonicalName(),
                    String.class.getCanonicalName(),
                    CharSequence.class.getCanonicalName()};

    private String[] acceptableForWriteArrayList =
            new String[]{"android.os.Parcelable",
                    Integer.class.getCanonicalName(),
                    String.class.getCanonicalName(),
                    CharSequence.class.getCanonicalName()};

    private static final AnnotationSpec GENERATED =
            AnnotationSpec.builder(Generated.class)
                    .addMember("value", "$S", FragmentBuilderRenderer.class.getName())
                    .addMember("comments", "$S", GENERATED_COMMENTS)
                    .build();

    private ClassName generetedClassName;
    private ClassName targetClassName;
    public List<VariableBinding> variables = new ArrayList<>();

    private ClassName dataHolder;
    private boolean hasHolder;

    public FragmentBuilderRenderer(ClassName generetedClassName, ClassName targetClassName) {
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
        addGetDataFromIntentMethodsSection(result);

        return JavaFile.builder(generetedClassName.packageName(), result.build())
                .addFileComment("Generated code do not modify!")
                .build();
    }

    private void addVariablesSection(TypeSpec.Builder result) {
        for (VariableBinding variable : variables) {
            result.addField(
                    FieldSpec.builder(TypeName.get(variable.fieldTypeMinor),
                            variable.fieldName, Modifier.PRIVATE).build());
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

        ClassName bundle = ClassName.get("android.os", "Bundle");

        MethodSpec.Builder method = MethodSpec.methodBuilder("build")
                .addModifiers(PUBLIC)
                .returns(targetClassName)
                .addStatement("$T fragment = new $T()", targetClassName, targetClassName)
                .addStatement("$T args = new $T()", bundle, bundle);

        for (VariableBinding variable : variables) {

            method.addStatement("args." + putExtraMethodName(variable)
                    + "(\"" + variable.fieldName + "_KEY\", " + variable.fieldName + ")");
        }
        method.addStatement("fragment.setArguments(args)");
        method.addStatement("return fragment");

        result.addMethod(method.build());
    }

    private void addGetDataFromIntentMethodsSection(TypeSpec.Builder result) {

        MethodSpec.Builder method = MethodSpec.methodBuilder("getDataFromArgs")
                .addModifiers(PUBLIC, STATIC)
                .addParameter(targetClassName, "activity")
                .addStatement("$T args = activity.getArguments()", ClassName.get("android.os", "Bundle"));

        for (VariableBinding variable : variables) {

            TypeElement acceptableParcelableElement = StatePersisterAnnotationProcessor.elementUtils.getTypeElement("android.os.Parcelable");
            ClassName parcelableType = ClassName.get(acceptableParcelableElement);
            TypeName variableType = ClassName.get(variable.fieldTypeMinor);
            String fieldName = variable.fieldName;

            if (variable.isPrimitive) {
                String defaultValue = DefaultValueUtils.getDefaultValue(ClassName.get(variable.fieldTypeMinor), null);
                method.addStatement("activity." + variable.fieldName + " = (" + variable.fieldType + ") args." + getExtraMethodName(variable)
                        + "(\"" + variable.fieldName + "_KEY\", " + defaultValue + ")");
            } else if (variable.isArrayList && StatePersisterAnnotationProcessor.typeUtils.isAssignable(variable.subType, acceptableParcelableElement.asType())) {
                method.addStatement("activity." + variable.fieldName + " = args." + getExtraMethodName(variable)
                        + "(\"" + variable.fieldName + "_KEY\")");
            } else if (variable.isArray && StatePersisterAnnotationProcessor.typeUtils.isAssignable(variable.subType, acceptableParcelableElement.asType())) {

                TypeName subType = ClassName.get(variable.subType);

                method.beginControlFlow(" if( args.getParcelableArray(\"$L_KEY\") != null )", fieldName);
                method.addStatement("$T[] array = args.getParcelableArray(\"$L_KEY\")", parcelableType, fieldName);
                method.addStatement("$1T[] destArray = new $1T[array.length]", subType);
                method.beginControlFlow("for( int i = 0; i < array.length; i++ )");

                method.addStatement("destArray[i] = ( $T ) array[i]", subType);

                method.endControlFlow();
                method.addStatement("activity.$L = destArray", fieldName);
                method.endControlFlow();

            } else {
                method.addStatement("activity." + variable.fieldName + " = (" + variable.fieldType + ") args." + getExtraMethodName(variable)
                        + "(\"" + variable.fieldName + "_KEY\")");
            }
        }

        result.addMethod(method.build());
    }

    private String putExtraMethodName(VariableBinding variable) {

        StringBuilder builder = new StringBuilder();
        builder.append("put");

        TypeElement acceptableSerializableElement = StatePersisterAnnotationProcessor.elementUtils.getTypeElement(Serializable.class.getCanonicalName());


        if (variable.isPrimitive) {
            builder.append(CaseUtils.toTitleCase(variable.fieldType));
        }
        else if (variable.isArray && variable.isPrimitiveSubType) {
            builder.append(CaseUtils.toTitleCase(variable.subType.toString()));
            builder.append("Array");
        }
        else if (variable.isArray) {
            getNameFromType(variable, builder, acceptableForReadArray);
            builder.append("Array");
        }
        else if (variable.isArrayList) {
            if (getNameFromType(variable, builder, acceptableForReadArrayList)) {
                builder.append("ArrayList");
            } else if (StatePersisterAnnotationProcessor.typeUtils.isAssignable(variable.subType, acceptableSerializableElement.asType())) {
                builder.append("Serializable");
//                builder.append("Extra");
            }
        } else {
            getNameFromType(variable, builder, acceptableForRead);
//            builder.append(CaseUtils.toTitleCase(variable.fieldType));
//            builder.append("Extra");
        }

        return builder.toString();
    }

    private String getExtraMethodName(VariableBinding variable) {
        StringBuilder builder = new StringBuilder();
        builder.append("get");

        TypeElement acceptableSerializableElement = StatePersisterAnnotationProcessor.elementUtils.getTypeElement(Serializable.class.getCanonicalName());
//        TypeElement acceptablePercelableElement = StatePersisterAnnotationProcessor.elementUtils.getTypeElement(Serializable.class.getCanonicalName());

        if (variable.isPrimitive) {
            builder.append(CaseUtils.toTitleCase(variable.fieldType));
//            builder.append("Extra");
        } else if (variable.isArray && variable.isPrimitiveSubType) {
            builder.append(CaseUtils.toTitleCase(variable.subType.toString()));
            builder.append("Array");

        } else if (variable.isArray) {
            getNameFromType(variable, builder, acceptableForReadArray);
            builder.append("Array");

        } else if (variable.isArrayList) {
            if (getNameFromType(variable, builder, acceptableForReadArrayList)) {
                builder.append("ArrayList");
            } else if (StatePersisterAnnotationProcessor.typeUtils.isAssignable(variable.subType, acceptableSerializableElement.asType())) {
                builder.append("Serializable");
            }
        } else {
            getNameFromType(variable, builder, acceptableForRead);
        }

        return builder.toString();
    }

    private boolean getNameFromType(VariableBinding variable, StringBuilder builder, String[] acceptedValues) {
        for (int elementOnWriteList = 0; elementOnWriteList < acceptedValues.length; elementOnWriteList++) {
            String name = null;
            if (variable.subType == null) {
                name = variable.fieldTypeMinor.toString();
            } else {
                name = variable.subType.toString();
            }
            TypeElement acceptableElement = StatePersisterAnnotationProcessor.elementUtils.getTypeElement(acceptedValues[elementOnWriteList]);
            TypeElement variableTypeElement = StatePersisterAnnotationProcessor.elementUtils.getTypeElement(name);

            if (StatePersisterAnnotationProcessor.typeUtils.isAssignable(variableTypeElement.asType(), acceptableElement.asType())) {
                ClassName className = ClassName.bestGuess(acceptedValues[elementOnWriteList]);
                builder.append(className.simpleName());
                return true;
            }
        }
        return false;
    }

    public void setDataHolder(ClassName dataHolder) {
        this.dataHolder = dataHolder;
        hasHolder = true;
    }
}
