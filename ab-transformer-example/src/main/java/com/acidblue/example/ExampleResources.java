package com.acidblue.example;


import com.acidblue.transformer.resource.ColorIcon;
import com.acidblue.transformer.resource.Default;
import com.acidblue.transformer.resource.DefaultParam;
import com.acidblue.transformer.resource.Resource;

import javax.swing.Icon;
import java.awt.Color;

/**
 * An example interface that serves as the property value holder for a class wishing to retrieve it's values.
 */
@Resource
public interface ExampleResources {

    @Default(value = "#CCCCCC")
    Color exampleColor();

    @Default(value = "A (Default) String")
    String exampleString();

    @Default("#FFFFFF")
    ColorIcon exampleColorIcon();

    @Default(value = "41")
    Integer age();

    @Default(value = "71")
    long height();

    @Default(value = "/com/acidblue/ab_drop_200.png")
    Icon image();

    @Default(value = "true")
    boolean booleanValue();

    //will return null
    String exampleNull();

    String exampleEnumTag(@DefaultParam(defs = {
            "Stop", "Halt now! (from annotationprocessor)",
            "Go", "Go Now! (from annotationprocessor)",
            "Wait"
    }, unknown = "Hey, you added this and I didn't know about it. (from annotationprocessor)") States states);


    /*
    static final BeanProperty acidblue = new BeanProperty("acidblue", "ll");
    static final BeanProperty rabbles = new BeanProperty("rabbles", "cofeehouse");
    static final BeanProperty unknown = new BeanProperty("UnknownValue", "unknown");

     */
    String exampleBeanValues(@DefaultParam(defs = {
            "acidblue.ll", "acidblue!"
            , "rabbles.coffeehouse", "Rabbles!"
    }, lookup = {"getFirstName", "getLastName"}, unknown = "I don't know.") com.acidblue.example.BeanProperty bean);


    Icon exampleBeanIcon(@DefaultParam(defs = {
            "acid_drop_200.png", "null"
    }, lookup = {"getFirstName", "getLastName"}) BeanProperty bean);


}


