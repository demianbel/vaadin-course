package com.gpsolutions.vaadincourse.field;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class VaadinBookListField extends CustomField<List<String>> {
    private VerticalLayout fields = new VerticalLayout();

    public VaadinBookListField(String caption) {
        setCaption(caption);
    }

    @Override
    protected Component initContent() {
        final GridLayout content = new GridLayout(2, 1);
        content.addComponent(fields);

        final Button add = new Button("+", event -> addItem());
        content.addComponent(add);
        content.setComponentAlignment(add, Alignment.BOTTOM_CENTER);
        return content;
    }

    private void addItem() {
        List<String> list = getValue();
        if (list == null)
            list = new ArrayList<>();
        list.add("");
        setValue(list);

        final TextField tf = new TextField();
        tf.addValueChangeListener(valueChange -> { // Java 8
            int index = fields.getComponentIndex(tf);
            List<String> values = getValue();
            values.set(index, tf.getValue());
            setValue(values);
        });
        tf.focus();
        fields.addComponent(tf);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends List<String>> getType() {
        return (Class<List<String>>) ((List<String>) new ArrayList<String>()).getClass();
    }

    @Override
    public List<String> getValue() {
        return super.getValue();
    }

    @Override public void setValue(final List<String> newFieldValue)
            throws ReadOnlyException, Converter.ConversionException {
        super.setValue(newFieldValue);
    }
}