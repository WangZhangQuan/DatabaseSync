package com.wzq.sql.value;

import java.io.Serializable;

public class PlaceholderValue implements Serializable {
    public static final String PLACEHOLDER = "?";
    public static final PlaceholderValue PLACEHOLDER_VALUE = new PlaceholderValue();

    private PlaceholderValue() {
    }

    public static PlaceholderValue getInstance(){
        return PLACEHOLDER_VALUE;
    }
}
