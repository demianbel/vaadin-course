package com.gpsolutions.vaadincourse.ui;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class StrangeTextField extends CustomField<String> {

    private VerticalLayout mainLayout;
    private Label mirrorLabel;
    private TextField mirrorField;
    private TextField generalField;

    @Override protected Component initContent() {
        final VerticalLayout parent = getMainLayout();
        parent.addComponents(getMirrorField(), getGeneralField(), getMirrorLabel());
        return parent;
    }

    @Override public Class<? extends String> getType() {
        return String.class;
    }

    @Override public String getValue() {
        return getGeneralField().getValue();
    }

    @Override public void setValue(final String newFieldValue) throws ReadOnlyException, Converter.ConversionException {
        getGeneralField().setValue(newFieldValue);
        getMirrorField().setValue(newFieldValue);
        getMirrorLabel().setValue(newFieldValue);
        super.setValue(newFieldValue);
    }

    private VerticalLayout getMainLayout() {
        if (mainLayout == null) {
            mainLayout = new VerticalLayout();
        }
        return mainLayout;
    }

    private Label getMirrorLabel() {
        if (mirrorLabel == null) {
            mirrorLabel = new Label();
        }
        return mirrorLabel;
    }

    private TextField getMirrorField() {
        if (mirrorField == null) {
            mirrorField = new TextField();
            mirrorField.setEnabled(false);
        }
        return mirrorField;
    }

    private TextField getGeneralField() {
        if (generalField == null) {
            generalField = new TextField();
            generalField.addTextChangeListener(this::generalTextChange);
        }
        return generalField;
    }

    private void generalTextChange(final FieldEvents.TextChangeEvent event) {
        final String newValue = event.getText();
        getMirrorLabel().setValue(newValue);
        getMirrorField().setValue(newValue);
        fireValueChange(false);
    }
}
