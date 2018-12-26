package com.gpsolutions.vaadincourse.view;

import com.gpsolutions.vaadincourse.broadcast.Broadcast;
import com.gpsolutions.vaadincourse.dbo.Email;
import com.gpsolutions.vaadincourse.repository.EmailRepository;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.time.LocalDate;
import java.util.function.Consumer;

@SpringView(name = ChatView.NAME)
public class ChatView extends CustomComponent implements View, Consumer<Email> {

    public static final String NAME = "chat";

    private final Broadcast<Email> messageBroadcast;
    private final VerticalLayout messageLayout;
    private String name = null;

    public ChatView(final Broadcast<Email> messageBroadcast, final EmailRepository emailRepository) {
        this.messageBroadcast = messageBroadcast;
        final VerticalLayout mainLayout = new VerticalLayout();

        final Panel messagePanel = new Panel();
        this.messageLayout = new VerticalLayout();
        messagePanel.setSizeFull();
        messagePanel.setContent(messageLayout);

        final HorizontalLayout registerLayout = new HorizontalLayout();
        final HorizontalLayout messageInputLayout = new HorizontalLayout();

        createTextFieldButtonPair(registerLayout, nameField -> {
            registerLayout.setVisible(false);
            messageInputLayout.setVisible(true);
            name = nameField.getValue();
            messagePanel.setCaption(name);
        }, "Please, input your name:", "Join Chat");

        createTextFieldButtonPair(messageInputLayout, messageField -> {
            final Email email = new Email();
            email.setName(name);
            email.setMessage(messageField.getValue());
            email.setDate(LocalDate.now());
            emailRepository.save(email);
            messageBroadcast.broadcast(email);
            messageField.clear();
            messageField.setEnabled(false);
        }, null, "Send");
        messageInputLayout.setVisible(false);

        mainLayout.addComponents(messagePanel, registerLayout, messageInputLayout);

        mainLayout.setExpandRatio(messagePanel, 20);
        mainLayout.setExpandRatio(registerLayout, 2);
        mainLayout.setExpandRatio(messageInputLayout, 2);

        mainLayout.setSizeFull();
        this.setCompositionRoot(mainLayout);
        this.setSizeFull();
    }

    private void createTextFieldButtonPair(final HorizontalLayout registerLayout,
                                           final Consumer<TextField> consumer,
                                           final String labelValue, final String buttonCaption) {
        if (labelValue != null) {
            final Label nameLabel = new Label();
            nameLabel.setValue(labelValue);
            registerLayout.addComponent(nameLabel);
        }
        final TextField nameField = new TextField();
        final Button button = new Button(buttonCaption);
        button.addStyleName(ValoTheme.BUTTON_PRIMARY);
        button.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        button.setEnabled(false);
        button.addClickListener(event -> consumer.accept(nameField));
        nameField.addTextChangeListener(event -> button.setEnabled(!event.getText().isEmpty()));
        registerLayout.addComponents(nameField, button);
    }

    @Override public void enter(final ViewChangeListener.ViewChangeEvent event) {
        messageBroadcast.register(this);
    }

    @Override public void detach() {
        messageBroadcast.unregister(this);
        super.detach();
    }

    @Override public void accept(final Email email) {
        getUI().access(() -> {
            final Label emailLabel = new Label(email.getName());
            emailLabel.setValue(email.getName() + ": " + email.getMessage());
            messageLayout.addComponent(emailLabel);
        });
    }
}
