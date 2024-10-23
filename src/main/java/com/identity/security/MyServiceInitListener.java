package com.identity.security;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class MyServiceInitListener implements VaadinServiceInitListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public void serviceInit(ServiceInitEvent event) {
		//System.out.println("Testing");
		
        event.getSource().addSessionInitListener(sessionInitEvent -> {
            VaadinSession session = sessionInitEvent.getSession();
            session.setErrorHandler(new CustomErrorHandler());
        });
    }

	
}