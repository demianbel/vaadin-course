package com.gpsolutions.vaadincourse.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;

@SpringView(name = NotAvailableView.NAME)
public class NotAvailableView implements View {
    public static final String NAME = "notAvailable";

    @Override public void enter(final ViewChangeListener.ViewChangeEvent event) {

    }
}
