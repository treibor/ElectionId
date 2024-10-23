package com.identity.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNavItem;

public class SideNavItemWithHelperText extends VerticalLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 public SideNavItemWithHelperText(String label, String helperText, Class<? extends Component> navigationTarget, Component icon) {
	        // Set layout properties
	        setSpacing(false);
	        setPadding(false);
	        setMargin(false);
	        icon.getElement().getStyle().set("color", "var(--lumo-primary-text-color)");
	        // Create the main SideNavItem
	        SideNavItem mainItem = new SideNavItem(label, navigationTarget, icon);

	        // Create the helper text
	        Span helperTextSpan = new Span(helperText);
	        helperTextSpan.getStyle().set("font-size", "var(--lumo-font-size-s)");
	        helperTextSpan.getStyle().set("color", "var(--lumo-secondary-text-color)");
	        
	        // Add the main item and helper text to the layout
	        add(mainItem, helperTextSpan);
	    }
}