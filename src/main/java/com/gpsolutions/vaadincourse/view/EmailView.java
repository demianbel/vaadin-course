package com.gpsolutions.vaadincourse.view;

import com.gpsolutions.vaadincourse.broadcast.Broadcast;
import com.gpsolutions.vaadincourse.dbo.Email;
import com.gpsolutions.vaadincourse.form.EmailForm;
import com.gpsolutions.vaadincourse.repository.EmailRepository;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.filter.Like;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;


@SpringView(name = EmailView.NAME)
public class EmailView extends CustomComponent implements View, Consumer<Email> {
    public final static String NAME = "email";

    private final Broadcast<Email> emailBroadcast;
    private final JPAContainer<Email> container;
    private final EmailRepository emailRepository;

    public EmailView(final Broadcast<Email> emailBroadcast,
                     final EmailRepository emailRepository,
                     final EntityManagerFactory entityManagerFactory) {
        this.emailBroadcast = emailBroadcast;
        this.emailRepository = emailRepository;
        container = JPAContainerFactory.make(Email.class, entityManagerFactory.createEntityManager());

        final Button closeButton = new Button("Close");
        closeButton.addClickListener(event -> getUI().getNavigator().navigateTo(MainView.NAME));

        final VerticalLayout layout = new VerticalLayout();
        final Grid emailGrid = new Grid(container);

        addMessageFilter(emailGrid);

        emailGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        emailGrid.setSizeFull();

        final Button removeButton = createRemoveButton(emailGrid);

        final Button addButton = getAddButton(emailBroadcast);

        final Button editButton = createEditButton(emailGrid);

        emailGrid.addSelectionListener(selectionEvent -> {
            final Set<Object> selected = selectionEvent.getSelected();
            removeButton.setEnabled(!selected.isEmpty());
            editButton.setEnabled(selected.size() == 1);
        });

        final HorizontalLayout buttonLayout = new HorizontalLayout(addButton, editButton, removeButton);

        layout.addComponents(closeButton, emailGrid, buttonLayout);

        this.setCompositionRoot(layout);
    }

    private void addMessageFilter(final Grid emailGrid) {
        final Grid.HeaderRow headerRow = emailGrid.addHeaderRowAt(0);
        final Grid.HeaderCell messageHeader = headerRow.getCell("message");
        final TextField messageFilter = new TextField();
        messageFilter.addTextChangeListener(event -> {
            final String newFilterValue = event.getText();
            container.removeAllContainerFilters();
            container.addContainerFilter(new Like("message", "%" + newFilterValue + "%"));
        });
        messageHeader.setComponent(messageFilter);
    }

    private Button getAddButton(final Broadcast<Email> emailBroadcast) {
        final Button addButton = new Button("Add");
        addButton.addClickListener(event -> {
            final Email newEmail = new Email();
            openWindowForEmail(container, newEmail, window -> {
                container.addEntity(newEmail);
                emailBroadcast.broadcast(newEmail);
                window.close();
            }, Window::close);
        });
        return addButton;
    }

    private Button createEditButton(final Grid emailGrid) {
        final Button editButton = new Button("Edit");
        editButton.addClickListener(event -> {
            final Collection<Object> selectedRows = emailGrid.getSelectedRows();
            if (!selectedRows.isEmpty()) {
                final Long next = (Long) selectedRows.iterator().next();
                final Email email = emailRepository.findById(next)
                        .orElseThrow(RuntimeException::new);
                openWindowForEmail(container, email, components -> {
                    emailRepository.save(email);
                    emailBroadcast.broadcast(email);
                    components.close();
                }, Window::close);
            }
        });
        editButton.setEnabled(false);
        return editButton;
    }

    private Button createRemoveButton(final Grid emailGrid) {
        final Button removeButton = new Button("Remove");
        removeButton.addClickListener(event -> {
            final Collection<Object> selectedRows = emailGrid.getSelectedRows();
            selectedRows.forEach(
                    emailGrid.getContainerDataSource()::removeItem);
            emailGrid.refreshAllRows();
        });
        removeButton.setEnabled(false);
        return removeButton;
    }

    @Override public void enter(final ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        emailBroadcast.register(this);
    }

    @Override public void detach() {
        emailBroadcast.unregister(this);
        super.detach();
    }

    private void openWindowForEmail(final JPAContainer<Email> container,
                                    final Email email,
                                    final Consumer<Window> onClose, final Consumer<Window> onDiscard) {
        final Window editWindow = new Window();
        editWindow.setModal(true);
        editWindow.setWidth(20, Unit.PERCENTAGE);
        editWindow.center();
        editWindow
                .setContent(new EmailForm(email, () -> onClose.accept(editWindow), () -> onDiscard.accept(editWindow)));
        editWindow.addCloseListener(e -> container.refresh());
        UI.getCurrent().addWindow(editWindow);
    }

    @Override public void accept(final Email email) {
        getUI().access(container::refresh);
    }
}
