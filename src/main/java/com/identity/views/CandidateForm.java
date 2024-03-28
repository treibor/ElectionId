package com.identity.views;

import com.identity.dbservice.DbService;
import com.identity.dbservice.DbServicePol;
import com.identity.entity.Candidate;
import com.identity.entity.Cell;
import com.identity.entity.Constituency;
import com.identity.entity.Office;
import com.identity.entity.Party;
import com.identity.views.CandidateForm;
import com.identity.views.CandidateForm.CandidateFormEvent;
import com.identity.views.CandidateForm.DeleteEvent;
import com.identity.views.CandidateForm.SaveEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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

public class CandidateForm extends FormLayout{
	
	
	Binder<Candidate> binder=new BeanValidationBinder<>(Candidate.class);
	TextField candidateName=new TextField("Candidate Name");
	//TextField candidateColour=new TextField("Candidate Colour");
	ComboBox<Constituency> constituency = new ComboBox("Constituency");
	ComboBox<Party> party = new ComboBox("Party");
	Button save= new Button("Save");
	Button delete= new Button("Delete");
	//Button close= new Button("Close");
	//Anchor anchor =new Anchor("https://www.w3schools.com/colors/colors_picker.asp", "Reference for HEX Colour");
	Notification notification=new Notification();
	private Candidate candidate;
	private final DbServicePol dbservice;
	public CandidateForm(DbServicePol dbservice) {
		this.dbservice=dbservice;
		binder.bindInstanceFields(this);
		constituency.setItems(dbservice.findConstBydistrict());
		party.setItems(dbservice.findPartyBydistrict());
		constituency.setItemLabelGenerator(Constituency::getConstituencyName);
		party.setItemLabelGenerator(Party:: getPartyName);
		add(candidateName, constituency, party, createButtonsLayout());
	}
	
	public void updateComboxes() {
		party.setItems(dbservice.findPartyBydistrict());
		constituency.setItems(dbservice.findConstBydistrict());
		constituency.setItemLabelGenerator(Constituency::getConstituencyName);
		party.setItemLabelGenerator(Party:: getPartyName);
	}
	private Component createButtonsLayout() {
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(event-> validateandSave());
		delete.addClickListener(event-> fireEvent(new DeleteEvent(this, candidate)));
		return new HorizontalLayout(save, delete);
	}
		
	
	private void validateandSave() {
		// TODO Auto-generated method stub
		try {
			binder.writeBean(candidate);
			candidate.setDistrict(dbservice.getLoggedDistrict());
			candidate.setState(dbservice.getLoggedState());
			fireEvent(new SaveEvent(this, candidate));
		} catch (ValidationException e) {
			notification.show("Please Enter All Required Fields",3000,Position.TOP_CENTER);
		}
	}


	public void setCandidate(Candidate candidate) {
		this.candidate=candidate;
		binder.readBean(candidate);
	}
	

///Operation Save/Delete Events

	public static abstract class CandidateFormEvent extends ComponentEvent<CandidateForm> {
		  private Candidate candidate;

		  protected CandidateFormEvent(CandidateForm source, Candidate candidate) { 
		    super(source, false);
		    this.candidate=candidate;
		  }

		  public Candidate getCandidate() {
		    return candidate;
		  }
		}

		public static class SaveEvent extends CandidateFormEvent {
		  SaveEvent(CandidateForm source, Candidate candidate) {
		    super(source, candidate);
		  }
		}

		public static class DeleteEvent extends CandidateFormEvent {
		  DeleteEvent(CandidateForm source, Candidate candidate) {
		    super(source, candidate);
		  }

		}

		public static class CloseEvent extends CandidateFormEvent {
		  CloseEvent(CandidateForm source) {
		    super(source, null);
		  }
		}

		public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
		    ComponentEventListener<T> listener) { 
		  return getEventBus().addListener(eventType, listener);
		}


}




