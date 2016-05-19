package pl.tomasz.krol.viewstatecompiler.model;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Created by tomasz.krol on 2016-05-10.
 */
public class StateBinding {

    private static final String GENERATED_COMMENTS = "beta version ";

    private static final AnnotationSpec GENERATED =
            AnnotationSpec.builder(Generated.class)
                    .addMember("value", "$S", StateBinding.class.getName())
                    .addMember("comments", "$S", GENERATED_COMMENTS)
                    .build();


    private static final ClassName CONTEXT = ClassName.get("android.content", "Context");
    private static final ClassName PERSIST_FRAGMENT = ClassName.get("pl.tomaszkrol.viewstate", "PersisterFragment");
    private static final ClassName FRAGMENT_ACTIVITY = ClassName.get("android.support.v4.app", "FragmentActivity");

    public ClassName generetedClassName;
    public ClassName targetClassName;
    public List<VariableBinding> variables = new ArrayList<>();

    public StateBinding(ClassName generetedClassName, ClassName targetClassName, boolean isActivity) {
        this.generetedClassName = generetedClassName;
        this.targetClassName = targetClassName;
    }

    public JavaFile generateJava() {
        TypeSpec.Builder result = TypeSpec.classBuilder(generetedClassName)
                .addAnnotation(GENERATED)
                .addModifiers(PUBLIC);

        result.addMethod(createPersistMethod());
        result.addMethod(createRestoreMethod());

        return JavaFile.builder(generetedClassName.packageName(), result.build())
                .addFileComment("Generated code do not modify!")
                .build();
    }

    private MethodSpec createPersistMethod() {
        MethodSpec.Builder result = MethodSpec.methodBuilder("persist")
                .addModifiers(PUBLIC)
                .addModifiers(STATIC)
                .addParameter(CONTEXT, "activityContext")
                .addParameter(targetClassName, "target")
                .addCode("if(!(activityContext instanceof $T)){\n" +
                        "    throw new RuntimeException(\"activityContext has to be FragmentActivity\");\n" +
                        "}\n" +
                        "else{\n", FRAGMENT_ACTIVITY)

                .addCode("    $T persistFragment = $T.injectPresistFragment(($T)activityContext);\n"
                        , PERSIST_FRAGMENT, PERSIST_FRAGMENT, FRAGMENT_ACTIVITY);

        for (VariableBinding variable : variables) {
            String asignement = String.format("    persistFragment.saveData(\"$T_%s\", target.%s);\n",
                    variable.fieldName, variable.fieldName);
            result.addCode(asignement, targetClassName);
        }

        result.addCode("}\n");
        return result.build();
    }

    private MethodSpec createRestoreMethod() {
        MethodSpec.Builder result = MethodSpec.methodBuilder("restore")
                .addModifiers(PUBLIC)
                .addModifiers(STATIC)
                .addParameter(CONTEXT, "activityContext")
                .addParameter(targetClassName, "target")
                .addCode("if(!(activityContext instanceof $T)){\n" +
                        "    throw new RuntimeException(\"activityContext has to be FragmentActivity\");\n" +
                        "}\n" +
                        "else{\n", FRAGMENT_ACTIVITY)

                .addCode("    $T persistFragment = $T.injectPresistFragment(($T)activityContext);\n"
                        , PERSIST_FRAGMENT, PERSIST_FRAGMENT, FRAGMENT_ACTIVITY)
                .addCode("    Object holder = null;\n");

        for (VariableBinding variable : variables) {
            String asignement = String.format("    holder = " + "persistFragment.loadData(\"$T_%s\");\n",
                    variable.fieldName);
            result.addCode(asignement, targetClassName);

            if (variable.isPrimitive) {
                String ifStateMent = "  if(holder != null){\n";
                result.addCode(ifStateMent);
            }

            asignement = String.format("\t    target.%s = (%s)" +
                            "holder;\n", variable.fieldName,
                    variable.fieldType);
            result.addCode(asignement, targetClassName);

            if (variable.isPrimitive) {
                String ifStateMent = "  }\n";
                result.addCode(ifStateMent);
            }
        }

        result.addCode("}\n");
        return result.build();
    }
}
