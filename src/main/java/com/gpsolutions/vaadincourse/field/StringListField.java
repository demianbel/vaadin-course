package com.gpsolutions.vaadincourse.field;

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
import java.util.Optional;
import java.util.stream.Collectors;

public class StringListField extends CustomField<List<String>> {

    private VerticalLayout mainLayout;
    private List<TextField> textFields = new ArrayList<>();
    private String addButtonCaption;

    public StringListField() {
        super();
    }

    public StringListField(final String caption, final String addButtonCaption) {
        this();
        this.setCaption(caption);
        this.addButtonCaption = addButtonCaption;
    }

    @Override protected Component initContent() {
        final VerticalLayout parent = new VerticalLayout();
        parent.addComponent(getMainLayout());
        getValue().forEach(this::addStringToForm);
        final Button addRecipient = new Button(addButtonCaption);
        addRecipient.addClickListener(event -> {
            addStringToForm("");
            super.setValue(textFields.stream().map(AbstractField::getValue).collect(Collectors.toList()));
        });
        parent.addComponent(addRecipient);
        return parent;
    }

    @SuppressWarnings("unchecked")
    @Override public Class<? extends List<String>> getType() {

        return (Class<List<String>>) ((List<String>) new ArrayList<String>()).getClass();
    }

    @Override public List<String> getValue() {
        return super.getValue();
    }

    @Override public void setValue(final List<String> newFieldValue)
            throws ReadOnlyException, Converter.ConversionException {
        getMainLayout().removeAllComponents();
        if (newFieldValue != null) {
            newFieldValue.forEach(this::addStringToForm);
        }
        super.setValue(new ArrayList<>(Optional.ofNullable(newFieldValue).orElse(new ArrayList<>())));
    }

    private void addStringToForm(final String stringToAdd) {
        final HorizontalLayout layout = new HorizontalLayout();
        final TextField textField = new TextField();
        textField.setValue(stringToAdd);
        textField.addTextChangeListener(
                event -> super.setValue(textFields.stream().map(field -> {
                    if (field != textField) {
                        return field.getValue();
                    } else {
                        return event.getText();
                    }
                }).collect(Collectors.toList())));
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

    private VerticalLayout getMainLayout() {
        if (mainLayout == null) {
            mainLayout = new VerticalLayout();
        }
        return mainLayout;
    }
}
