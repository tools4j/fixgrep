package org.tools4j.fix;

import org.jetbrains.annotations.NotNull;
import org.tools4j.properties.PropertyKeysAndDefaultValues;
import org.tools4j.properties.PropertySource;
import org.tools4j.properties.PropertySourceBase;
import org.tools4j.properties.PropertySourceImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * User: ben
 * Date: 16/06/2017
 * Time: 6:08 PM
 */
public class TestPropertySource extends PropertySourceBase {
    private final PropertySource propertySource;

    public TestPropertySource(final Consumer<Map<String, String>> customConfig) {
        this(PropertyKeysAndDefaultValues.Companion.getAsPropertySource().overrideWith(((Supplier<PropertySource>) () -> {
            final Map<String, String> properties = new HashMap<>();
            customConfig.accept(properties);
            return new PropertySourceImpl(Collections.unmodifiableMap(properties));
        }).get()));
    }

    public TestPropertySource() {
        this(PropertyKeysAndDefaultValues.Companion.getAsPropertySource());
    }

    private TestPropertySource(final PropertySource propertySource) {
        this.propertySource = propertySource;
    }

    @Override
    public boolean containsKey(@NotNull final String key) {
        return propertySource.containsKey(key);
    }

    @NotNull
    @Override
    public PropertySource overrideWith(@NotNull final PropertySource other) {
        return propertySource.overrideWith(other);
    }

    @Override
    protected String get(@NotNull final String key) {
        return propertySource.getAsString(key);
    }
}
