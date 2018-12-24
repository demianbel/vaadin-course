package com.gpsolutions.vaadincourse.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ErrorView extends CustomComponent implements View {

    public static final String SUSANIN_IMAGE =
            "https://k1news.ru/upload/iblock/12a/12ab88984f95918bbce3f80d554a2fc4.jpg";
    public static final String ERROR_MESSAGE = "Where do we go, Susanin?";

    public ErrorView() {
        final Label errorMessage = new Label();
        errorMessage.setValue(ERROR_MESSAGE);
        final Image image = new Image("", new ExternalResource(
                SUSANIN_IMAGE));
        image.setWidth(200, Unit.PIXELS);

        this.setCompositionRoot(new VerticalLayout(errorMessage, image));
    }

    @Override public void enter(final ViewChangeListener.ViewChangeEvent event) {

    }
}
