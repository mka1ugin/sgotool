package com.mkalugin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableEntry {
    private final StringProperty parameter;
    private final StringProperty value;
    private final StringProperty offset;

    public TableEntry(String parameter, String value, String offset){
        this.parameter = new SimpleStringProperty(parameter);
        this.value = new SimpleStringProperty(value);
        this.offset = new SimpleStringProperty(offset);
    }

    public StringProperty parameterProperty() {
        return parameter;
    }

    public StringProperty valueProperty() {
        return value;
    }

    public StringProperty offsetProperty() {
        return offset;
    }
}
