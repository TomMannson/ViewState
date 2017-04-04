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
public class ActivityModelBuilderRenderer {

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
                    .addMember("value", "$S", ActivityModelBuilderRenderer.class.getName())
                    .addMember("comments", "$S", GENERATED_COMMENTS)
                    .build();

    private ClassName generetedClassName;
    private ClassName targetClassName;
    private final ClassName[] activityClassName;
    public List<VariableBinding> variables = new ArrayList<>();

    private ClassName dataHolder;
    private boolean hasHolder;

    public ActivityModelBuilderRenderer(ClassName generetedClassName, ClassName targetClassName, ClassName[] activityClassName) {
        this.generetedClassName = generetedClassName;
        this.targetClassName = targetClassName;
        this.activityClassName = activityClassName;
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

        for(int activityTargetIndex = 0; activityTargetIndex < activityClassName.length; activityTargetIndex++ ) {

            MethodSpec.Builder method = MethodSpec.methodBuilder("buildFor"+activityClassName[activityTargetIndex].simpleName())
                    .addModifiers(PUBLIC)
                    .addParameter(ClassName.get("android.content", "Context"), "ctx")
                    .returns(ClassName.get("android.content", "Intent"))
                    .addStatement("Intent starter = new Intent(ctx, $T.class)", activityClassName[activityTargetIndex]);

            for (VariableBinding variable : variables) {

                method.addStatement("starter." + putExtraMethodName(variable)
                        + "(\"" + variable.fieldName + "_KEY\", " + variable.fieldName + ")");
            }

            method.addStatement("starter.putExtra"
                    + "(\"Activity_Holder_Name_KEY\", " + targetClassName.simpleName() + ")");

            method.addStatement("return starter");

            result.addMethod(method.build());
        }
    }

    private void addGetDataFromIntentMethodsSection(TypeSpec.Builder result) {

        MethodSpec.Builder method = MethodSpec.methodBuilder("getDataFromIntent")
                .addModifiers(PUBLIC, STATIC)
                .returns(targetClassName)
                .addParameter(targetClassName, "activity")
                .addStatement("Intent intent = activity.getIntent()")
                .addStatement("$T data = new $T()", targetClassName, targetClassName);

        for (VariableBinding variable : variables) {

            TypeElement acceptableParcelableElement = StatePersisterAnnotationProcessor.elementUtils.getTypeElement("android.os.Parcelable");
            ClassName parcelableType = ClassName.get(acceptableParcelableElement);
            TypeName variableType = ClassName.get(variable.fieldTypeMinor);
            String fieldName = variable.fieldName;

            if (variable.isPrimitive) {
                String defaultValue = DefaultValueUtils.getDefaultValue(ClassName.get(variable.fieldTypeMinor), null);
                method.addStatement("data." + variable.fieldName + " = (" + variable.fieldType + ") intent." + getExtraMethodName(variable)
                        + "(\"" + variable.fieldName + "_KEY\", " + defaultValue + ")");
            } else if (variable.isArrayList && StatePersisterAnnotationProcessor.typeUtils.isAssignable(variable.subType, acceptableParcelableElement.asType())) {
                method.addStatement("data." + variable.fieldName + " = intent." + getExtraMethodName(variable)
                        + "(\"" + variable.fieldName + "_KEY\")");
            } else if (variable.isArray && StatePersisterAnnotationProcessor.typeUtils.isAssignable(variable.subType, acceptableParcelableElement.asType())) {

                TypeName subType = ClassName.get(variable.subType);

                method.beginControlFlow(" if( intent.getParcelableArrayExtra(\"$L_KEY\") != null )", fieldName);
//
                method.addStatement("$T[] array = intent.getParcelableArrayExtra(\"$L_KEY\")", parcelableType, fieldName);
                method.addStatement("$1T[] destArray = new $1T[array.length]", subType);
                method.beginControlFlow("for( int i = 0; i < array.length; i++ )");

                method.addStatement("destArray[i] = ( $T ) array[i]", subType);

                method.endControlFlow();
                method.addStatement("data.$L = destArray", fieldName);
                method.endControlFlow();

            } else {
                method.addStatement("data." + variable.fieldName + " = (" + variable.fieldType + ") intent." + getExtraMethodName(variable)
                        + "(\"" + variable.fieldName + "_KEY\")");
            }
        }

        result.addMethod(method.build());
    }

    private String putExtraMethodName(VariableBinding variable) {

        StringBuilder builder = new StringBuilder();
        builder.append("put");

        TypeElement acceptableSerializableElement = StatePersisterAnnotationProcessor.elementUtils.getTypeElement(Serializable.class.getCanonicalName());

        if (variable.isArrayList) {
            if (getNameFromType(variable, builder, acceptableForReadArrayList)) {
                builder.append("ArrayListExtra");
            } else if (StatePersisterAnnotationProcessor.typeUtils.isAssignable(variable.subType, acceptableSerializableElement.asType())) {
                builder.append("Extra");
            }
        } else {
            builder.append("Extra");
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
            builder.append("Extra");
        } else if (variable.isArray && variable.isPrimitiveSubType) {
            builder.append(CaseUtils.toTitleCase(variable.subType.toString()));
            builder.append("ArrayExtra");

        } else if (variable.isArray) {
            getNameFromType(variable, builder, acceptableForReadArray);
            builder.append("ArrayExtra");

        } else if (variable.isArrayList) {
            if (getNameFromType(variable, builder, acceptableForReadArrayList)) {
                builder.append("ArrayListExtra");
            } else if (StatePersisterAnnotationProcessor.typeUtils.isAssignable(variable.subType, acceptableSerializableElement.asType())) {
                builder.append("SerializableExtra");
            }
        } else {
            getNameFromType(variable, builder, acceptableForRead);
            builder.append("Extra");
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
