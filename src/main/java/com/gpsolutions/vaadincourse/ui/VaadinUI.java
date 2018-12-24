package com.gpsolutions.vaadincourse.ui;

import com.gpsolutions.vaadincourse.dto.Email;
import com.gpsolutions.vaadincourse.form.EmailForm;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@SpringUI(path = "vaadin")
@Theme("valo")
public class VaadinUI extends UI {


    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        final List<Email> emails = new ArrayList<>();
        final Email first = createEmail("Demian", "Hello, have you done your homework",
                                        Arrays.asList("Wladimir", "Pavel", "Iosif"), LocalDate.now().minusDays(2));
        final Email second = createEmail("Wladimir", "Hello, I'm not going to do any homework. Don't write me anymore.",
                                         Collections.singletonList("Demian"), LocalDate.now().minusDays(1));
        final Email third = createEmail("Demian", "Ok",
                                        Collections.singletonList("Wladimir"), LocalDate.now());
        emails.add(first);
        emails.add(second);
        emails.add(third);


        final VerticalLayout layout = new VerticalLayout();
        final BeanItemContainer<Email> container =
                new BeanItemContainer<>(Email.class, emails);
        final Grid emailGrid = new Grid(container);
        emailGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        emailGrid.setSizeFull();

        final Button removeButton = new Button("Remove");
        final Button editButton = new Button("Edit");
        final Button addButton = new Button("Add");
        removeButton.setEnabled(false);
        editButton.setEnabled(false);
        emailGrid.addSelectionListener(selectionEvent -> {
            final Set<Object> selected = selectionEvent.getSelected();
            removeButton.setEnabled(!selected.isEmpty());
            editButton.setEnabled(selected.size() == 1);
        });
        removeButton.addClickListener(event -> {
            final Collection<Object> selectedRows = emailGrid.getSelectedRows();
            emails.removeAll(selectedRows);
            selectedRows.forEach(
                    emailGrid.getContainerDataSource()::removeItem);
            emailGrid.refreshAllRows();
        });

        editButton.addClickListener(event -> {
            final Collection<Object> selectedRows = emailGrid.getSelectedRows();
            if (!selectedRows.isEmpty()) {
                final Object next = selectedRows.iterator().next();
                openWindowForEmail(emailGrid, (Email) next, Window::close, Window::close);
            }
        });

        addButton.addClickListener(event -> {
            final Email newEmail = new Email();
            openWindowForEmail(emailGrid, newEmail, window -> {
                emails.add(newEmail);
                emailGrid.getContainerDataSource().addItem(newEmail);
                window.close();
            }, Window::close);

        });

        final HorizontalLayout buttonLayout = new HorizontalLayout(addButton, editButton, removeButton);

        layout.addComponents(emailGrid, buttonLayout);

        this.setContent(layout);
        this.setSizeFull();
    }

    private void openWindowForEmail(final Grid emailGrid,
                                    final Email email,
                                    final Consumer<Window> onClose, final Consumer<Window> onDiscard) {
        final Window editWindow = new Window();
        editWindow.setModal(true);
        editWindow
                .setContent(new EmailForm(email, () -> onClose.accept(editWindow), () -> onDiscard.accept(editWindow)));
        editWindow.addCloseListener(e -> emailGrid.refreshAllRows());
        this.addWindow(editWindow);
    }

    private Email createEmail(final String name,
                              final String message,
                              final List<String> recipients,
                              final LocalDate date) {
        final Email email = new Email();
        email.setName(name);
        email.setMessage(message);
        email.setRecipients(new ArrayList<>(recipients));
        email.setDate(date);
        return email;
    }
}
