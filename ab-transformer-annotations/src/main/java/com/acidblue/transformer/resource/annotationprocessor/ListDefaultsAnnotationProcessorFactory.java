package com.acidblue.transformer.resource.annotationprocessor;

import com.acidblue.transformer.resource.*;
import com.acidblue.transformer.resource.ResourceKeyBuilder;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


/**
 * TODO Documentation
 *
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes(
        { "com.acidblue.transformer.resource.Default"
        , "com.acidblue.transformer.resource.Resource"
        , "com.acidblue.transformer.resource.DefaultParam"})
public class ListDefaultsAnnotationProcessorFactory extends AbstractProcessor {

    //todo, need to make this available to configure as an environment property
    /**
     * The default path and filename for the output of this annotation processor processor.
     */
    public static final String FACTORY_PROPERTIES = "com/acidblue/transformer/resources/acidblue.properties";

    /**
     * The string format for the output file's configuration entries.
     * <p />
     * <i>[interface].[propertyName].[type]=[value]</i>
     */
    public static final String FORMAT = "#%s\n%s.%s.%s=%s";

    private static final String PARAMETER_TYPE = "parameterType";
    private static final String SIMPLE_NAME = "simpleName";
    private static final String METHOD_NAME = "methodName";
    private static final String TYPE = "type";
    private static final String DEFS = "defs";
    private static final String CLASS_NAME = "className";

    //silly doc string for the property file
    public final static String FILE_HEADER =
            "#--------------------------------------------------------\n" +
            "This is the output of the transformer annotation processor.\n" +
            "Uncomment a property to change it's value.\n" +
            "#--------------------------------------------------------\n";

    private String resourceClassName;
    private final StringBuilder builder = new StringBuilder();
    private final TypeResolver typeResolver;
    private final AnnotationValueVisitor<List<String>, Map> defaultParameterHandler;

    public ListDefaultsAnnotationProcessorFactory() throws Exception {
        this.typeResolver = new TypeResolver(loadConfiguration(), new ReturnTypeVisitor());
        this.defaultParameterHandler = new DefinitionsHandler();
    }

    protected Map<String, String> loadConfiguration() {

        final Properties properties = new Properties();
        final Map<String, String> annotationProperties = new HashMap<>();

        try {
            properties.load(getClass().getClassLoader()
                    .getResourceAsStream(
                            "META-INF/com/acidblue/transformer/annotationprocessor/ListDefaultsAnnotationProcessorFactory.properties"));

        } catch (IOException e) {
            final Messager messager = processingEnv.getMessager();
            messager.printMessage(Diagnostic.Kind.WARNING, e.getMessage());
        }

        for (Map.Entry entry : properties.entrySet()) {
            annotationProperties.put((String) entry.getKey(), (String) entry.getValue());
        }

        return annotationProperties;
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {

        if (annotations.size() == 0) return false;

        for (final TypeElement typeElement : annotations) {
            processProperties(roundEnv, typeElement);
        }

        writePropertyFile();

        return false;
    }

    private void writePropertyFile() {

        //TODO we should create an environment variable to allow a name change.
        try {
            final Filer filer = processingEnv.getFiler();
            final FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "",
                    FACTORY_PROPERTIES);
            final BufferedWriter fileWriter = new BufferedWriter(fileObject.openWriter());
            fileWriter.write(FILE_HEADER);
            fileWriter.write("\n\n");
            fileWriter.write(this.builder.toString());
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            super.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Unable to create property" +
                    " file\n. Message is: " + e.getMessage());
        }
    }

    private void processProperties(final RoundEnvironment roundEnv, final TypeElement typeElement) {

        for (final Element element : roundEnv.getElementsAnnotatedWith(typeElement)) {
            this.builder.append("\n");
            this.builder.append("## ");//we append 2 for headers
            this.builder.append(element.getSimpleName());
            this.builder.append("\n");
            this.resourceClassName = element.toString();

            processProperty(element);
        }
    }


    private void processProperty(final Element element) {
        processTypeMirrors(element, element.getEnclosingElement().getEnclosingElement());
        processEnclosedElements(element);
    }

    private void processEnclosedElements(final Element element) {

        for (final Element subElement : element.getEnclosedElements()) {

            final TypeMirror mirror = subElement.asType();

            final Default def = subElement.getAnnotation(Default.class);

            final String docString = def == null ? "" : def.desc();

            final String output = String.format(FORMAT,
                    docString == null ? "" : docString,
                    resourceClassName,
                    subElement.getSimpleName(),
                    typeResolver.resolveTypeExtension(mirror),
                    def != null ? def.value().replace("\n", "\\n") : "");


            builder.append("#").append(output).append('\n');
        }
    }

    private void processTypeMirrors(final Element element, final Element subElement) {

        for (final AnnotationMirror mirror : element.getAnnotationMirrors()) {
            extractDefs(element, subElement, mirror.getElementValues());
        }
    }

    private void extractDefs(final Element element, final Element subElement,
                             final Map<? extends ExecutableElement, ? extends AnnotationValue> mirrorValues) {

        for (final ExecutableElement key : mirrorValues.keySet()) {

            if (key.getSimpleName().contentEquals(DEFS)) {

                final List<String> defs = mirrorValues.get(key)
                        .accept(defaultParameterHandler, new HashMap<String, Object>() {{
                            put(CLASS_NAME, element.getEnclosingElement().getEnclosingElement()
                                    .getSimpleName().toString());
                            put(PARAMETER_TYPE, subElement.getEnclosingElement());
                            put(SIMPLE_NAME, subElement.getSimpleName());
                            put(METHOD_NAME, element.getEnclosingElement().getSimpleName());
                            put(TYPE, typeResolver.resolveTypeExtension(element.getEnclosingElement().asType()));
                        }});

                for (final String def : defs) {
                    builder.append("#").append(def).append('\n');
                }

            }
        }
    }


    private class ReturnTypeVisitor extends SimpleTypeVisitor6<String, Void> {
        @Override
        public String visitExecutable(final ExecutableType t, final Void aVoid) {

            return t.getReturnType().toString();
        }
    }

    private class ParameterTypeVisitor extends SimpleTypeVisitor6<String, Void> {
        @Override
        public String visitExecutable(final ExecutableType t, final Void aVoid) {

            return t.getParameterTypes().toString();
        }
    }

    private class KeyResolver extends SimpleTypeVisitor6<String, Void> {
        @Override
        public String visitExecutable(final ExecutableType t, final Void aVoid) {

            return t.getReturnType().toString();
        }
    }

    private class DefinitionsHandler extends SimpleAnnotationValueVisitor6<List<String>, Map> {

        @Override
        public List<String> visitArray(final List<? extends AnnotationValue> vals, Map env) {

            final List<String> values = new LinkedList<>();

            final AnnotationValue[] annotationValues = vals.toArray(new AnnotationValue[vals.size()]);
            final String parameterType = env.get(PARAMETER_TYPE).toString();
            final String methodName = env.get(METHOD_NAME).toString();
            final String simpleName = env.get(SIMPLE_NAME).toString();
            final String type = (String) env.get(TYPE);
            final String className = (String) env.get(CLASS_NAME);

            if (annotationValues.length % 2 != 0) {
                final Messager messager = processingEnv.getMessager();
                messager.printMessage(Diagnostic.Kind.WARNING,
                        String.format(
                                        " Unable to provide the default values for annotated method %s",
                                className, methodName));
            } else {

                //skip over 2, key/value
                for (int i = 0; i < annotationValues.length; i += 2) {
                    final String packageName = annotationValues[i].getValue().toString();
                    final String value = annotationValues[i + 1].getValue().toString();
                    final String bundle = new ResourceKeyBuilder()
                            .value(value)
                            .parameterType(parameterType)
                            .propertyName(methodName)
                            .interfaceName(simpleName)
                            .propertyType(type)
                            .packageName(packageName)
                            .toBundleEntry();

                    values.add(bundle);
                }
            }

            return values;
        }
    }


    private class TypeResolver {

        private final Map<String, String> annotationProperties;
        private final SimpleTypeVisitor6<String, Void> typeVisitor;

        public TypeResolver(final Map<String, String> properties, final SimpleTypeVisitor6<String, Void> typeVisitor) {
            this.annotationProperties = new HashMap<>(properties);
            this.typeVisitor = typeVisitor;
        }

        public String resolveTypeExtension(final TypeMirror mirror) {
            return annotationProperties.get(mirror.accept(typeVisitor, null));
        }

    }
}