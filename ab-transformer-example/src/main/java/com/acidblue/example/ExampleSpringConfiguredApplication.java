package com.acidblue.example;

import com.acidblue.transformer.resource.ResolverFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;


public class ExampleSpringConfiguredApplication {

    static final BeanProperty acidblue = new BeanProperty("acidblue", "ll");
    static final BeanProperty rabbles = new BeanProperty("rabbles", "coffeehouse");
    static final BeanProperty unknown = new BeanProperty("UnknownValue", "unknown");

    public static void main(final String[] args) {

        new FileSystemXmlApplicationContext(new String[]{
                "classpath:example_context.xml"});
        execute();
    }

    private static void execute() {
        ExampleSubInterface resources = ResolverFactory.get(ExampleSubInterface.class);

        System.out.println(resources.exampleNull());
        System.out.println(resources.booleanValue());
        System.out.println(resources.age());
        System.out.println(resources.exampleColor());
        System.out.println(resources.exampleString());
        System.out.println(resources.height());
//        System.out.println(resources.image());
//        System.out.println(resources.exampleEnumTag(States.Go));
        System.out.println(resources.exampleBeanValues(acidblue));
        System.out.println(resources.exampleBeanValues(rabbles));
        System.out.println(resources.exampleBeanValues(unknown));
    }

}