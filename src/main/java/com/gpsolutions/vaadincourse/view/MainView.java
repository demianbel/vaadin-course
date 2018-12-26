package com.gpsolutions.vaadincourse.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;

@SpringView(name = MainView.NAME)
public class MainView extends CustomComponent implements View {

    public static final String NAME = "main";

    public MainView() {
        final MenuBar menuBar = new MenuBar();
        menuBar.addItem("Email", (MenuBar.Command) selectedItem -> getUI().getNavigator().navigateTo(EmailView.NAME));
        menuBar.addItem("Chat", (MenuBar.Command) selectedItem -> getUI().getNavigator().navigateTo(ChatView.NAME));
        menuBar.addItem("Error",
                        (MenuBar.Command) selectedItem -> getUI().getNavigator().navigateTo(EmailView.NAME + "error"));
        this.setCompositionRoot(menuBar);
    }

    @Override public void enter(final ViewChangeListener.ViewChangeEvent event) {

    }
}
