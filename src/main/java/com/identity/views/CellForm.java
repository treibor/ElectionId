package com.identity.views;

import com.identity.dbservice.DbService;
import com.identity.entity.Cell;
import com.identity.views.CellForm;
import com.identity.views.CellForm.CellFormEvent;
import com.identity.views.CellForm.DeleteEvent;
import com.identity.views.CellForm.SaveEvent;
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

public class CellForm extends FormLayout{
	
	
	Binder<Cell> binder=new BeanValidationBinder<>(Cell.class);
	TextField cellName=new TextField("Cell Name");
	TextField cellColour=new TextField("Cell Colour");
	Button save= new Button("Save");
	Button delete= new Button("Delete");
	//Button close= new Button("Close");
	Anchor anchor =new Anchor("", "Reference for HEX Colour");
	Notification notification=new Notification();
	private Cell cell;
	private final DbService dbservice;
	boolean isadmin;
	public CellForm(DbService dbservice) {
		isadmin=dbservice.isAdmin();
		//anchor.setTarget("https://www.w3schools.com/colors/colors_picker.asp");
		anchor.setTarget("https://www.w3schools.com/colors/colors_picker.asp\", \"Reference for HEX Colour");
		this.dbservice=dbservice;
		binder.bindInstanceFields(this);
		add(cellName,cellColour, anchor,createButtonsLayout());
	}
	
	
	private Component createButtonsLayout() {
		// TODO Auto-generated method stub
		cellColour.setPlaceholder("HEX Colour Code");
		cellColour.setClearButtonVisible(true);
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(event-> validateandSave());
		delete.addClickListener(event-> fireEvent(new DeleteEvent(this, cell)));
		delete.setEnabled(isadmin);
		anchor.setHref("https://www.w3schools.com/colors/colors_picker.asp");
		//close.addClickListener(event-> fireEvent(new CloseEvent(this)));
		return new HorizontalLayout(save, delete);
	}
		
	
	private void validateandSave() {
		// TODO Auto-generated method stub
		try {
			binder.writeBean(cell);
			cell.setDistrict(dbservice.getLoggedDistrict());
			cell.setState(dbservice.getLoggedState());
			fireEvent(new SaveEvent(this, cell));
		} catch (ValidationException e) {
			notification.show("Please Enter All Required Fields",3000,Position.TOP_CENTER);
		}
	}


	public void setCell(Cell cell) {
		this.cell=cell;
		binder.readBean(cell);
	}
	

///Operation Save/Delete Events

	public static abstract class CellFormEvent extends ComponentEvent<CellForm> {
		  private Cell cell;

		  protected CellFormEvent(CellForm source, Cell cell) { 
		    super(source, false);
		    this.cell=cell;
		  }

		  public Cell getCell() {
		    return cell;
		  }
		}

		public static class SaveEvent extends CellFormEvent {
		  SaveEvent(CellForm source, Cell cell) {
		    super(source, cell);
		  }
		}

		public static class DeleteEvent extends CellFormEvent {
		  DeleteEvent(CellForm source, Cell cell) {
		    super(source, cell);
		  }

		}

		public static class CloseEvent extends CellFormEvent {
		  CloseEvent(CellForm source) {
		    super(source, null);
		  }
		}

		public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
		    ComponentEventListener<T> listener) { 
		  return getEventBus().addListener(eventType, listener);
		}


}




