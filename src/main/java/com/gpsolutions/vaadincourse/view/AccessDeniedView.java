package com.gpsolutions.vaadincourse.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = AccessDeniedView.NAME)
public class AccessDeniedView extends CustomComponent implements View {
    public static final String NAME = "accessDenied";
    private static final String ACCESS_DENIED_IMAGE =
            "https://img-fotki.yandex.ru/get/106972/97833783.15a2/0_1cea96_9c7c332d_orig.jpg";

    @Override public void enter(final ViewChangeListener.ViewChangeEvent event) {
        final VerticalLayout compositionRoot = new VerticalLayout();
        final Image accessDeniedImage = new Image("", new ExternalResource(
                ACCESS_DENIED_IMAGE));
        accessDeniedImage.setHeight(100, Unit.PERCENTAGE);

        compositionRoot.addComponents(accessDeniedImage);
        compositionRoot.setSizeFull();

        this.setCompositionRoot(compositionRoot);
        this.setSizeFull();
    }
}
