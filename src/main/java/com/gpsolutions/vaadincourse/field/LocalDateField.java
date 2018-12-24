package com.gpsolutions.vaadincourse.field;

import com.gpsolutions.vaadincourse.DateUtil;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DateField;

import java.time.LocalDate;
import java.util.Date;

public class LocalDateField extends CustomField<LocalDate> {

    private final DateField dateField = new DateField();

    public LocalDateField() {
    }

    public LocalDateField(final String caption) {
        this();
        setCaption(caption);
    }

    @Override protected Component initContent() {
        dateField.setValue(DateUtil.getDate(getValue()));
        dateField.addValueChangeListener(
                event -> setValue(DateUtil.getLocalDate((Date) event.getProperty().getValue())));
        return dateField;
    }

    @Override public Class<? extends LocalDate> getType() {
        return LocalDate.class;
    }

}
