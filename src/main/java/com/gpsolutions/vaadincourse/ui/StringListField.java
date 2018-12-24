package com.gpsolutions.vaadincourse.ui;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StringListField extends CustomField<List<String>> {

    private VerticalLayout mainLayout;
    private List<TextField> textFields = new ArrayList<>();

    @Override protected Component initContent() {
        final VerticalLayout parent = new VerticalLayout();
        parent.addComponent(getMainLayout());
        final Button addRecipient = new Button("add recipient");
        addRecipient.addClickListener(event -> {
            addRecipientToForm("");
            fireValueChange(true);
        });
        parent.addComponent(addRecipient);
        return parent;
    }

    @Override public Class<? extends List<String>> getType() {
        try {
            return (Class<? extends List<String>>) StringListField.class.getMethod("getType").getReturnType();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("omg");
        }
    }

    @Override public List<String> getValue() {
        return textFields.stream().map(AbstractField::getValue).collect(Collectors.toList());
    }

    @Override public void setValue(final List<String> newFieldValue)
            throws ReadOnlyException, Converter.ConversionException {
        getMainLayout().removeAllComponents();
        newFieldValue.forEach(this::addRecipientToForm);
        super.setValue(new ArrayList<>(newFieldValue));
    }

    private void addRecipientToForm(final String recipient) {
        final HorizontalLayout layout = new HorizontalLayout();
        final TextField textField = new TextField();
        textField.setValue(recipient);
        textField.addValueChangeListener(event -> fireValueChange(true));
        textFields.add(textField);
        final Button remove = new Button("Remove");
        remove.addClickListener(event -> {
            textFields.remove(textField);
            getMainLayout().removeComponent(layout);
            fireValueChange(true);
        });
        layout.addComponents(textField, remove);
        getMainLayout().addComponent(layout);
    }

    public VerticalLayout getMainLayout() {
        if (mainLayout == null) {
            mainLayout = new VerticalLayout();
        }
        return mainLayout;
    }
}
