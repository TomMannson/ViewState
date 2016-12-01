package pl.tommannson.viewstate.processor;

import com.google.auto.service.AutoService;

import java.io.IOException;
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
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import pl.tommannson.viewstate.annotations.ViewData;
import pl.tommannson.viewstate.annotations.ViewState;
import pl.tommannson.viewstate.processor.model.ModelFactory;
import pl.tommannson.viewstate.processor.model.StateBindingRenderer;
import pl.tommannson.viewstate.processor.model.VariableBinding;
import pl.tommannson.viewstate.processor.utils.AptUtils;


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
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        try {
            Map<TypeElement, StateBindingRenderer> elements = findAndParseTargets(roundEnv);

            for (Map.Entry<TypeElement, StateBindingRenderer> entry : elements.entrySet()) {
                TypeElement typeElement = entry.getKey();
                StateBindingRenderer bindingClass = entry.getValue();

                bindingClass.generateJava().writeTo(filer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    private Map<TypeElement, StateBindingRenderer> findAndParseTargets(RoundEnvironment env) {
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
}
