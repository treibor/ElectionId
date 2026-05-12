package com.identity.security;

import java.io.Serializable;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.VaadinService;

public class CustomErrorHandler implements ErrorHandler, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void error(ErrorEvent event) {

        Throwable throwable = event.getThrowable();

        if (isInvalidJsonError(throwable)) {
            redirectToLogin();
            return;
        }

        // Optional: print other errors for debugging
        // throwable.printStackTrace();
    }

    private boolean isInvalidJsonError(Throwable throwable) {
        while (throwable != null) {
            String message = throwable.getMessage();

            if (throwable instanceof IllegalStateException
                    && message != null
                    && message.contains("Invalid JSON")) {
                return true;
            }

            throwable = throwable.getCause();
        }

        return false;
    }

    private void redirectToLogin() {
        UI ui = UI.getCurrent();

        if (ui != null && VaadinService.getCurrentRequest() != null) {
            String contextPath = VaadinService.getCurrentRequest().getContextPath();
            ui.getPage().setLocation(contextPath + "/login");
        }
    }
}