package com.booking.context;

import java.util.HashMap;
import java.util.Map;

public class TestContext {

    private final Map<String, Object> data = new HashMap<>();

    public <T> void set(String key, T value) {
        data.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) data.get(key);
    }
}