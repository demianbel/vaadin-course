package com.gpsolutions.vaadincourse.view;

import com.gpsolutions.vaadincourse.dto.Email;
import com.gpsolutions.vaadincourse.form.EmailForm;
import com.gpsolutions.vaadincourse.service.EmailService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;


@SpringView(name = EmailView.NAME)
public class EmailView extends CustomComponent implements View {
    public final static String NAME = "email";


    public EmailView(final EmailService emailService) {
        final Button closeButton = new Button("Close");
        closeButton.addClickListener(event -> getUI().getNavigator().navigateTo(MainView.NAME));
        final List<Email> emails = emailService.getAllEmails();

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

        layout.addComponents(closeButton, emailGrid, buttonLayout);

        this.setCompositionRoot(layout);
    }

    @Override public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

    private void openWindowForEmail(final Grid emailGrid,
                                    final Email email,
                                    final Consumer<Window> onClose, final Consumer<Window> onDiscard) {
        final Window editWindow = new Window();
        editWindow.setModal(true);
        editWindow.setWidth(20, Unit.PERCENTAGE);
        editWindow.center();
        editWindow
                .setContent(new EmailForm(email, () -> onClose.accept(editWindow), () -> onDiscard.accept(editWindow)));
        editWindow.addCloseListener(e -> emailGrid.refreshAllRows());
        UI.getCurrent().addWindow(editWindow);
    }
}
