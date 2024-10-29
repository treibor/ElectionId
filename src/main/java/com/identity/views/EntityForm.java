package com.identity.views;

import com.identity.dbservice.DbService;
import com.identity.entity.District;
import com.identity.entity.State;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class EntityForm extends FormLayout{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Binder<District> binder=new BeanValidationBinder<>(District.class);
	//Binder<Employee> binder = new BeanValidationBinder<>(Employee.class);
	ComboBox<State> state=new ComboBox<State>("State");
	TextField districtName=new TextField("Name");
	Button save= new Button("Save");
	Button delete= new Button("Delete");
	Button newButton= new Button("New Entity");
	private District district;
	private final DbService dbservice;
	boolean isadmin;
	public EntityForm(DbService dbservice) {
		//isadmin=dbservice.isAdmin();
		this.dbservice=dbservice;
		binder.bindInstanceFields(this);
		
		add(state,districtName, createButtonsLayout());
		//populateState();
	}
	
	
	public void populateState() {
		// TODO Auto-generated method stub
		state.setItems(dbservice.findAllStates());
		state.setItemLabelGenerator(state->state.getStateName());
	}


	private Component createButtonsLayout() {
		
		// TODO Auto-generated method stub
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(event-> validateandSave());
		delete.addClickListener(event-> fireEvent(new DeleteEvent(this, district)));
		//newButton.addClickListener(event-> this.setDistrict(new District()));
		return new HorizontalLayout(save, delete);
	}
		
	
	private void validateandSave() {
		// TODO Auto-generated method stub
		try {
			binder.writeBean(district);
			//district.setDistrict(dbservice.getLoggedDistrict());
			//district.setState(dbservice.getLoggedState());
			fireEvent(new SaveEvent(this, district));
		} catch (ValidationException e) {
			Notification.show("Please Enter All Required Fields",3000,Position.TOP_CENTER);
		}
	}


	public void setDistrict(District district) {
		this.district=district;
		binder.readBean(district);
	}
	

///Operation Save/Delete Events

	public static abstract class EntityFormEvent extends ComponentEvent<EntityForm> {
		  private District district;

		  protected EntityFormEvent(EntityForm source, District district) { 
		    super(source, false);
		    this.district=district;
		  }

		  public District getDistrict() {
		    return district;
		  }
		}

		public static class SaveEvent extends EntityFormEvent {
		  SaveEvent(EntityForm source, District district) {
		    super(source, district);
		  }
		}

		public static class DeleteEvent extends EntityFormEvent {
		  DeleteEvent(EntityForm source, District district) {
		    super(source, district);
		  }

		}

		public static class CloseEvent extends EntityFormEvent {
		  CloseEvent(EntityForm source) {
		    super(source, null);
		  }
		}

		public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
		    ComponentEventListener<T> listener) { 
		  return getEventBus().addListener(eventType, listener);
		}


}




