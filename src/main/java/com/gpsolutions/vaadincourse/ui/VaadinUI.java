package com.gpsolutions.vaadincourse.ui;

import com.gpsolutions.vaadincourse.view.AccessDeniedView;
import com.gpsolutions.vaadincourse.view.ChatView;
import com.gpsolutions.vaadincourse.view.ErrorView;
import com.gpsolutions.vaadincourse.view.MainView;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

@Push
@SpringUI(path = "vaadin")
@Theme("mytheme")
public class VaadinUI extends UI implements ViewAccessControl {

    private final SpringViewProvider viewProvider;

    public VaadinUI(final SpringViewProvider viewProvider) {
        this.viewProvider = viewProvider;
        viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
    }

    @Override
    protected void init(VaadinRequest request) {

        final Navigator navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);
        navigator.setErrorView(new ErrorView());
        navigator.navigateTo(MainView.NAME);
    }


    @Override public boolean isAccessGranted(final UI ui, final String beanName) {
        if ("notAvailableView".equals(beanName)) {
            return false;
        }
        return true;
    }

}
