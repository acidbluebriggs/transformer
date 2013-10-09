package com.acidblue.example;

import com.acidblue.example.States;
import com.acidblue.transformer.resource.DefaultParam;

public interface ExampleSubInterface extends ExampleResources {


    /**
     * Demonstrates overriding a value.
     *
     * @param states
     * @return
     */
    String exampleEnumTag(@DefaultParam(defs = {
            "Stop", "(SUB) Halt now! (from annotationprocessor)",
            "Go", "(SUB) Go Now! (from annotationprocessor)",
            "Wait", "(SUB) Stay here and wait! (from annotationprocessor)"
    }, unknown = "(SUB) Hey, you added this and I didn't know about it. (from annotationprocessor)") States states);

}
