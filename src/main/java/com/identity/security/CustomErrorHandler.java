package com.identity.security;




import java.io.Serializable;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.SessionExpiredException;

public class CustomErrorHandler implements ErrorHandler, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public void error(ErrorEvent event) {
		  UI.getCurrent().getPage().setLocation("/login");
        if (event.getThrowable().getCause() instanceof IllegalStateException &&
            event.getThrowable().getCause().getMessage().contains("Invalid JSON")) {
            // Suppress the error and redirect to login page
            UI.getCurrent().getPage().setLocation("/login");
        } else {
            // Handle other errors (optional: suppress all errors)
            // event.getThrowable().printStackTrace();
        }
    }
}