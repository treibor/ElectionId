package com.identity.views;

import com.identity.dbservice.DbService;
import com.identity.dbservice.DbServicePol;
import com.identity.entity.Office;
import com.identity.entity.Party;
import com.identity.views.PartyForm;
import com.identity.views.PartyForm.DeleteEvent;
import com.identity.views.PartyForm.SaveEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class PartyForm extends FormLayout{
	
	
	Binder<Party> binder=new BeanValidationBinder<>(Party.class);
	TextField partyName=new TextField("Party Name");
	Button save= new Button("Save");
	Button delete= new Button("Delete");
	//Button close= new Button("Close");
	Notification notification=new Notification();
	private Party party;
	private final DbServicePol dbService;
	public PartyForm(DbServicePol dbservice) {
		this.dbService=dbservice;
		binder.bindInstanceFields(this);
		binder.bind(partyName, Party::getPartyName, Party::setPartyName);
		add(partyName, createButtonsLayout());
		//addOffice();
	}
	
	
	private Component createButtonsLayout() {
		// TODO Auto-generated method stub
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(event-> validateandSave());
		delete.addClickListener(event-> fireEvent(new DeleteEvent(this, party)));
		//close.addClickListener(event-> fireEvent(new CloseEvent(this)));
		return new HorizontalLayout(save, delete);
	}
		
	
	private void validateandSave() {
		// TODO Auto-generated method stub
		try {
			binder.writeBean(party);
			party.setDistrict(dbService.getLoggedDistrict());
			party.setState(dbService.getLoggedState());
			fireEvent(new SaveEvent(this, party));
		} catch (ValidationException e) {
			notification.show("Please Enter All Required Fields",3000,Position.TOP_CENTER);
		}
	}


	public void setParty(Party party) {
		this.party=party;
		binder.readBean(party);
	}
	

///Operation Save/Delete Events

	public static abstract class PartyFormEvent extends ComponentEvent<PartyForm> {
		  private Party party;

		  protected PartyFormEvent(PartyForm source, Party party) { 
		    super(source, false);
		    this.party=party;
		  }

		  public Party getParty() {
		    return party;
		  }
		}

		public static class SaveEvent extends PartyFormEvent {
		  SaveEvent(PartyForm source, Party party) {
		    super(source, party);
		  }
		}

		public static class DeleteEvent extends PartyFormEvent {
		  DeleteEvent(PartyForm source, Party party) {
		    super(source, party);
		  }

		}

		public static class CloseEvent extends PartyFormEvent {
		  CloseEvent(PartyForm source) {
		    super(source, null);
		  }
		}

		public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
		    ComponentEventListener<T> listener) { 
		  return getEventBus().addListener(eventType, listener);
		}


}





