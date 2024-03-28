package com.identity.views;

import com.identity.dbservice.DbService;
import com.identity.entity.Office;
import com.identity.views.OfficeForm;
import com.identity.views.OfficeForm.DeleteEvent;
import com.identity.views.OfficeForm.OfficeFormEvent;
import com.identity.views.OfficeForm.SaveEvent;
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

public class OfficeForm extends FormLayout{
	
	
	Binder<Office> binder=new BeanValidationBinder<>(Office.class);
	TextField officeName=new TextField("Office Name");
	Button save= new Button("Save");
	Button delete= new Button("Delete");
	//Button close= new Button("Close");
	Notification notification=new Notification();
	private Office office;
	private final DbService dbService;
	boolean isadmin;
	public OfficeForm(DbService dbservice) {
		this.dbService=dbservice;
		binder.bindInstanceFields(this);
		//binder.bind(officeName, Office::getOfficeName, Office::setOfficeName);
		isadmin=dbservice.isAdmin();
		add(officeName, createButtonsLayout());
		//addOffice();
		
	}
	
	
	private Component createButtonsLayout() {
		// TODO Auto-generated method stub
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(event-> validateandSave());
		delete.addClickListener(event-> fireEvent(new DeleteEvent(this, office)));
		delete.setEnabled(isadmin);
		//close.addClickListener(event-> fireEvent(new CloseEvent(this)));
		return new HorizontalLayout(save, delete);
	}
		
	
	private void validateandSave() {
		// TODO Auto-generated method stub
		try {
			binder.writeBean(office);
			office.setDistrict(dbService.getLoggedDistrict());
			office.setState(dbService.getLoggedState());
			fireEvent(new SaveEvent(this, office));
		} catch (ValidationException e) {
			notification.show("Please Enter All Required Fields",3000,Position.TOP_CENTER);
		}
	}


	public void setOffice(Office office) {
		this.office=office;
		binder.readBean(office);
	}
	

///Operation Save/Delete Events

	public static abstract class OfficeFormEvent extends ComponentEvent<OfficeForm> {
		  private Office office;

		  protected OfficeFormEvent(OfficeForm source, Office office) { 
		    super(source, false);
		    this.office=office;
		  }

		  public Office getOffice() {
		    return office;
		  }
		}

		public static class SaveEvent extends OfficeFormEvent {
		  SaveEvent(OfficeForm source, Office office) {
		    super(source, office);
		  }
		}

		public static class DeleteEvent extends OfficeFormEvent {
		  DeleteEvent(OfficeForm source, Office office) {
		    super(source, office);
		  }

		}

		public static class CloseEvent extends OfficeFormEvent {
		  CloseEvent(OfficeForm source) {
		    super(source, null);
		  }
		}

		public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
		    ComponentEventListener<T> listener) { 
		  return getEventBus().addListener(eventType, listener);
		}


}





