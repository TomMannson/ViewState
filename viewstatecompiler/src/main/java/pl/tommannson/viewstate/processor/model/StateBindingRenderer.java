package pl.tommannson.viewstate.processor.model;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Created by tomasz.krol on 2016-05-10.
 */
public class StateBindingRenderer {

    private static final String GENERATED_COMMENTS = "beta version ";

    private static final AnnotationSpec GENERATED =
            AnnotationSpec.builder(Generated.class)
                    .addMember("value", "$S", StateBindingRenderer.class.getName())
                    .addMember("comments", "$S", GENERATED_COMMENTS)
                    .build();


//    private static final ClassName CONTEXT = ClassName.get("android.content", "Context");
//    private static final ClassName PERSIST_FRAGMENT = ClassName.get("pl.tomaszkrol.viewstate", "PersisterFragment");
//    private static final ClassName FRAGMENT_ACTIVITY = ClassName.get("android.support.v4.app", "FragmentActivity");

    public ClassName generetedClassName;
    public ClassName targetClassName;
    public List<VariableBinding> variables = new ArrayList<>();

    public StateBindingRenderer(ClassName generetedClassName, ClassName targetClassName, boolean isActivity) {
        this.generetedClassName = generetedClassName;
        this.targetClassName = targetClassName;
    }

    public JavaFile generateJava() {
        TypeSpec.Builder result = TypeSpec.classBuilder(generetedClassName)
                .addAnnotation(GENERATED)
                .addModifiers(PUBLIC);

        result.addMethod(createPersistMethod());
        result.addMethod(createRestoreMethod());
        result.addType(createHolderClass());

        return JavaFile.builder(generetedClassName.packageName(), result.build())
                .addFileComment("Generated code do not modify!")
                .build();
    }

    private MethodSpec createPersistMethod() {

        String holderClassName = targetClassName.simpleName() + "_Holder";

        MethodSpec.Builder result = MethodSpec.methodBuilder("persist")
                .addModifiers(PUBLIC)
                .addModifiers(STATIC)
                .addParameter(targetClassName, "target")
                .returns(TypeName.OBJECT)
                .addStatement(holderClassName + " holder = new " + holderClassName +"()");
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
        result.addStatement(holderClassName + " holder = ("+holderClassName+")data" );

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
