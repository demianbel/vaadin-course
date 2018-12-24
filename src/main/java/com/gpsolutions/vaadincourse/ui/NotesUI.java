package com.gpsolutions.vaadincourse.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@SpringUI(path = "vaadin")
@Theme("valo")
public class NotesUI extends UI {


    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        final List<Email> emails = new ArrayList<>();
        final Email first = createEmail("Demian", "Hello, have you done your homework",
                                        Arrays.asList("Wladimir", "Pavel", "Iosif"), LocalDate.now().minusDays(2));
        final Email second = createEmail("Wladimir", "Hello, I'm not going to do any homework. Don't write me anymore.",
                                         Arrays.asList("Demian"), LocalDate.now().minusDays(1));
        final Email third = createEmail("Demian", "Ok",
                                        Arrays.asList("Wladimir"), LocalDate.now());
        emails.add(first);
        emails.add(second);
        emails.add(third);


        final VerticalLayout layout = new VerticalLayout();
        final BeanItemContainer<Email> container =
                new BeanItemContainer<>(Email.class, emails);
        final Grid emailGrid = new Grid(container);
        emailGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        this.setSizeFull();
        emailGrid.setSizeFull();

        final Button removeButton = new Button("Remove");
        removeButton.setEnabled(false);
        emailGrid.addSelectionListener(selectionEvent -> {
            final Set<Object> selected = selectionEvent.getSelected();
            removeButton.setEnabled(!selected.isEmpty());
        });
        removeButton.addClickListener(event -> {
            final Collection<Object> selectedRows = emailGrid.getSelectedRows();
            emails.removeAll(selectedRows);
            selectedRows.forEach(
                    emailGrid.getContainerDataSource()::removeItem);
            emailGrid.refreshAllRows();
        });
        final StringListField recipients = new StringListField();
        recipients.setValue(first.getRecipients());
        final StrangeTextField strangeTextField = new StrangeTextField();
        strangeTextField.setValue("hello");
        layout.addComponents(emailGrid, removeButton, recipients, strangeTextField);
        setContent(layout);
    }

    private Email createEmail(final String name,
                              final String message,
                              final List<String> recipients,
                              final LocalDate date) {
        final Email email = new Email();
        email.setName(name);
        email.setMessage(message);
        email.setRecipients(recipients);
        email.setDate(date);
        return email;
    }
}
