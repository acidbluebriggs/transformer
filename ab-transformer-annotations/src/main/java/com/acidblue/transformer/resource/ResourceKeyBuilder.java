package com.acidblue.transformer.resource;

import java.util.HashMap;
import java.util.Map;

public class ResourceKeyBuilder {

    private static final String EXTENDED_FORMAT = "%s.%s.%s.%s.%s=%s";
    private final Map<String, String> sections = new HashMap<>();

    public ResourceKeyBuilder packageName(final String key) {
        sections.put("key", key);

        return this;
    }

    public ResourceKeyBuilder value(final String key) {
        sections.put("value", key);

        return this;
    }

    public ResourceKeyBuilder parameterType(final String key) {
        sections.put("parameterType", key);

        return this;
    }


    public ResourceKeyBuilder propertyName(final String key) {
        sections.put("methodName", key);

        return this;
    }

    public ResourceKeyBuilder interfaceName(final String key) {
        sections.put("simpleName", key);

        return this;
    }


    public ResourceKeyBuilder propertyType(final String key) {
        sections.put("type", key);

        return this;
    }

    public void reset() {
        this.sections.clear();
    }

    public String toBundleEntry() {
        return String.format(EXTENDED_FORMAT,
                sections.get("parameterType"),
                sections.get("simpleName"),
                sections.get("methodName"),
                sections.get("key"),
                sections.get("type"),
                sections.get("value"));
    }


}
