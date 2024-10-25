package com.identity.views;

import com.identity.dbservice.DbService;
import com.identity.entity.Users;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;


public class UsersForm extends FormLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DbService service;
	Binder<Users> binder=new BeanValidationBinder<>(Users.class);
	Checkbox enabled=new Checkbox("Enabled");
	TextField districtLabel=new TextField("Label");
	Button save= new Button("Update");
	private Users user;
	//private Impldistrict impldist;
	public UsersForm(DbService service) {
		this.service=service;
		binder.bindInstanceFields(this);
		add(enabled, createButtonsLayout());
	
	}

	

	private Component createButtonsLayout() {
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(event-> validateandSave());
		return new HorizontalLayout(save);
	}
	
	private void validateandSave() {
		try {
			binder.writeBean(user);
			fireEvent(new SaveEvent(this, user));
		} catch (ValidationException e) {
			Notification.show("Please Enter All Required Fields", 3000, Position.TOP_CENTER);
			
		} catch (Exception e) {
			
		}

	}

	public void setUsers(Users user) {
		this.user=user;
		binder.readBean(user);
	}
	
	public static abstract class UsersFormEvent extends ComponentEvent<UsersForm> {
		private Users user;

		protected UsersFormEvent(UsersForm source, Users user) {
			super(source, false);
			this.user = user;
		}

		public Users getUsers() {
			return user;
		}
	}

	public static class SaveEvent extends UsersFormEvent {
		SaveEvent(UsersForm source, Users user) {
			super(source, user);
		}
	}

	public static class DeleteEvent extends UsersFormEvent {
		DeleteEvent(UsersForm source, Users user) {
			super(source, user);
		}

	}

	public static class CloseEvent extends UsersFormEvent {
		CloseEvent(UsersForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

	
}
