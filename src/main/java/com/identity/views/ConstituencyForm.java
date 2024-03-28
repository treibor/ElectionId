package com.identity.views;

import com.identity.dbservice.DbService;
import com.identity.dbservice.DbServicePol;
import com.identity.entity.Constituency;
import com.identity.views.ConstituencyForm;
import com.identity.views.ConstituencyForm.ConstituencyFormEvent;
import com.identity.views.ConstituencyForm.DeleteEvent;
import com.identity.views.ConstituencyForm.SaveEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class ConstituencyForm extends FormLayout{
	
	
	Binder<Constituency> binder=new BeanValidationBinder<>(Constituency.class);
	TextField constituencyName=new TextField("Constituency Name");
	TextField constituencyColour=new TextField("Constituency Colour");
	Button save= new Button("Save");
	Button delete= new Button("Delete");
	Anchor anchor =new Anchor("", "Reference for HEX Colour");
	Notification notification=new Notification();
	private Constituency constituency;
	private final DbServicePol dbservice;
	public ConstituencyForm(DbServicePol dbservice) {
		this.dbservice=dbservice;
		anchor.setTarget("https://www.w3schools.com/colors/colors_picker.asp\", \"Reference for HEX Colour");
		binder.bindInstanceFields(this);
		add(constituencyName,constituencyColour, anchor,createButtonsLayout());
	}
	
	
	private Component createButtonsLayout() {
		// TODO Auto-generated method stub
		constituencyColour.setPlaceholder("HEX Colour Code");
		constituencyColour.setClearButtonVisible(true);
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(event-> validateandSave());
		delete.addClickListener(event-> fireEvent(new DeleteEvent(this, constituency)));
		anchor.setHref("https://www.w3schools.com/colors/colors_picker.asp");
		//close.addClickListener(event-> fireEvent(new CloseEvent(this)));
		return new HorizontalLayout(save, delete);
	}
		
	
	private void validateandSave() {
		// TODO Auto-generated method stub
		try {
			binder.writeBean(constituency);
			constituency.setDistrict(dbservice.getLoggedDistrict());
			constituency.setState(dbservice.getLoggedState());
			fireEvent(new SaveEvent(this, constituency));
		} catch (ValidationException e) {
			notification.show("Please Enter All Required Fields",3000,Position.TOP_CENTER);
		}
	}


	public void setConstituency(Constituency constituency) {
		this.constituency=constituency;
		binder.readBean(constituency);
	}
	

///Operation Save/Delete Events

	public static abstract class ConstituencyFormEvent extends ComponentEvent<ConstituencyForm> {
		  private Constituency constituency;

		  protected ConstituencyFormEvent(ConstituencyForm source, Constituency constituency) { 
		    super(source, false);
		    this.constituency=constituency;
		  }

		  public Constituency getConstituency() {
		    return constituency;
		  }
		}

		public static class SaveEvent extends ConstituencyFormEvent {
		  SaveEvent(ConstituencyForm source, Constituency constituency) {
		    super(source, constituency);
		  }
		}

		public static class DeleteEvent extends ConstituencyFormEvent {
		  DeleteEvent(ConstituencyForm source, Constituency constituency) {
		    super(source, constituency);
		  }

		}

		public static class CloseEvent extends ConstituencyFormEvent {
		  CloseEvent(ConstituencyForm source) {
		    super(source, null);
		  }
		}

		public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
		    ComponentEventListener<T> listener) { 
		  return getEventBus().addListener(eventType, listener);
		}


}




