package com.identity.views;

import org.vaadin.addons.tatu.ColorPicker;
import org.vaadin.addons.tatu.ColorPickerVariant;

import com.identity.dbservice.DbService;
import com.identity.entity.MasterEvent;
import com.identity.entity.MasterEvent;
import com.identity.views.EventForm;
import com.identity.views.EventForm.DeleteEvent;
import com.identity.views.EventForm.MasterEventFormEvent;
import com.identity.views.EventForm.SaveEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
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

public class EventForm extends FormLayout{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Binder<MasterEvent> binder=new BeanValidationBinder<>(MasterEvent.class);
	TextField eventName=new TextField("Event Name");
	Checkbox isdefault=new Checkbox("Set Default");
	Button save= new Button("Save");
	Button delete= new Button("Delete");
	//Button close= new Button("Close");
	Notification notification=new Notification();
	Anchor anchor =new Anchor("", "Reference for HEX Colour");
	private MasterEvent masterevent;
	private final DbService dbService;
	boolean isadmin;
	public EventForm(DbService dbservice) {
		this.dbService=dbservice;
		binder.bindInstanceFields(this);
		isadmin=dbservice.isAdmin();
		add(eventName,isdefault, createButtonsLayout());
		//addMasterEvent();
		
	}
	
	
	private Component createButtonsLayout() {
		// TODO Auto-generated method stub
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.setEnabled(isadmin);
		save.addClickListener(event-> validateandSave());
		delete.addClickListener(event-> fireEvent(new DeleteEvent(this, masterevent)));
		delete.setEnabled(isadmin);
		//close.addClickListener(event-> fireEvent(new CloseEvent(this)));
		return new HorizontalLayout(save, delete);
	}
		
	
	private void validateandSave() {
		// TODO Auto-generated method stub
		try {
			binder.writeBean(masterevent);
			masterevent.setDistrict(dbService.getLoggedDistrict());
			masterevent.setState(dbService.getLoggedState());
			fireEvent(new SaveEvent(this, masterevent));
		} catch (ValidationException e) {
			notification.show("Please Enter All Required Fields",3000,Position.TOP_CENTER);
		}
	}


	public void setMasterEvent(MasterEvent event) {
		this.masterevent=event;
		binder.readBean(event);
	}
	

///Operation Save/Delete Events

	public static abstract class MasterEventFormEvent extends ComponentEvent<EventForm> {
		  private MasterEvent MasterEvent;

		  protected MasterEventFormEvent(EventForm source, MasterEvent MasterEvent) { 
		    super(source, false);
		    this.MasterEvent=MasterEvent;
		  }

		  public MasterEvent getMasterEvent() {
		    return MasterEvent;
		  }
		}

		public static class SaveEvent extends MasterEventFormEvent {
		  SaveEvent(EventForm source, MasterEvent MasterEvent) {
		    super(source, MasterEvent);
		  }
		}

		public static class DeleteEvent extends MasterEventFormEvent {
		  DeleteEvent(EventForm source, MasterEvent MasterEvent) {
		    super(source, MasterEvent);
		  }

		}

		public static class CloseEvent extends MasterEventFormEvent {
		  CloseEvent(EventForm source) {
		    super(source, null);
		  }
		}

		public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
		    ComponentEventListener<T> listener) { 
		  return getEventBus().addListener(eventType, listener);
		}


}





