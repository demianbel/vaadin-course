package com.gpsolutions.vaadincourse.form;

import com.gpsolutions.vaadincourse.dbo.Email;
import com.gpsolutions.vaadincourse.field.LocalDateField;
import com.gpsolutions.vaadincourse.field.StringListField;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import java.time.LocalDate;
import java.util.List;

public class EmailForm extends Panel {

    @PropertyId("name")
    private final TextField nameTextField = new TextField("Name");
    @PropertyId("message")
    private final TextArea messageTextArea = new TextArea("Message");
    @PropertyId("recipients")
    private final StringListField recipientsField = new StringListField("Recipients", "Add recipient");
    @PropertyId("date")
    private final LocalDateField dateField = new LocalDateField("Date");

    public EmailForm(final Email email, final Runnable onSave, final Runnable onDiscard) {
        nameTextField.setNullRepresentation("");
        messageTextArea.setNullRepresentation("");
        final BeanFieldGroup<Email> emailFieldGroup = new BeanFieldGroup<>(Email.class);
        emailFieldGroup.setItemDataSource(email);
        emailFieldGroup.bindMemberFields(this);

        dateField.addValidator(this::validateDate);

        recipientsField.addValidator(this::validateRecipientsValue);

        final Button save = new Button("save");
        save.addClickListener(click -> {
            try {
                emailFieldGroup.commit();
                onSave.run();
            } catch (FieldGroup.CommitException e) {
                Notification.show("Form isn't valid", Notification.Type.WARNING_MESSAGE);
            }
        });

        final Button discard = new Button("cancel");
        discard.addClickListener(click -> {
            emailFieldGroup.discard();
            onDiscard.run();
        });

        final HorizontalLayout buttonLayout = new HorizontalLayout(save, discard);

        this.setContent(new FormLayout(nameTextField, messageTextArea, recipientsField, dateField, buttonLayout));
        this.addStyleName("email-form");

    }

    private void validateDate(final Object value) {
        if (value == null) {
            throw new Validator.InvalidValueException("date is empty");
        }
        if (((LocalDate) value).isBefore(LocalDate.now())) {
            throw new Validator.InvalidValueException("date is before today");
        }
    }

    private void validateRecipientsValue(final Object value) {
        final List<String> castedValue = (List<String>) value;
        if (castedValue == null || castedValue.isEmpty()) {
            throw new Validator.InvalidValueException("recipients is empty");
        }
        if (castedValue.stream().anyMatch(recipient -> recipient == null || recipient.isEmpty())) {
            throw new Validator.InvalidValueException("some recipient value is null empty");
        }
        if (castedValue.size() > 2) {
            throw new Validator.InvalidValueException("recipients number should be less than 2");
        }
    }

}
