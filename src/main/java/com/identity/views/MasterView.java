package com.identity.views;

import com.identity.dbservice.DbService;
import com.identity.entity.Cell;
import com.identity.entity.MasterEvent;
import com.identity.entity.Office;
import com.identity.views.CellForm;
import com.identity.views.MainLayout;
import com.identity.views.OfficeForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("Master Data")
@Route(value = "master", layout=MainLayout.class)
@RolesAllowed({"USER","SUPER", "ADMIN" })
public class MasterView extends VerticalLayout {
	Grid <Office> officegrid=new Grid<>(Office.class);
	Grid <Cell> cellgrid=new Grid<>(Cell.class);
	Grid <MasterEvent> eventgrid=new Grid<>(MasterEvent.class);
	private DbService dbservice;
	OfficeForm form;
	CellForm cellform;
	EventForm eventform;
	public MasterView(DbService dbservice) {
		this.dbservice=dbservice;
		setSizeFull();
		configureGrid();
		configureForm();
		add(getToolbar(),getContent());
		updateList();
		//addOffice();
		closeCellEditor();
		closeOfficeEditor();
		closeEventEditor();
	}
	
	private void closeCellEditor() {
		// TODO Auto-generated method stub
		cellform.setCell(null);
		cellform.setVisible(false);
		//cellgrid.asSingleSelect().clear();
		removeClassName("editing");
		
	}
	private void closeOfficeEditor() {
		// TODO Auto-generated method stub
		form.setOffice(null);
		form.setVisible(false);
		removeClassName("editing");
		
	}
	private void closeEventEditor() {
		// TODO Auto-generated method stub
		eventform.setMasterEvent(null);
		eventform.setVisible(false);
		removeClassName("editing");
		
	}
	private void configureForm() {
		// TODO Auto-generated method stub
		form=new OfficeForm(dbservice);
		cellform=new CellForm(dbservice);
		eventform=new EventForm(dbservice);
		form.setWidth("50%");
		eventform.setWidth("50%");
		cellform.setWidth("50%");
		form.addListener(OfficeForm.SaveEvent.class, this::saveOffice);
		form.addListener(OfficeForm.DeleteEvent.class, this::deleteOffice);
		form.addListener(OfficeForm.CloseEvent.class, e->closeOfficeEditor());
		cellform.addListener(CellForm.SaveEvent.class, this::saveCell);
		cellform.addListener(CellForm.DeleteEvent.class, this::deleteCell);
		cellform.addListener(CellForm.DeleteEvent.class, e->closeCellEditor());
		eventform.addListener(EventForm.SaveEvent.class, this::saveEvent);
		eventform.addListener(EventForm.DeleteEvent.class, this::deleteEvent);
		eventform.addListener(EventForm.CloseEvent.class, e->closeOfficeEditor());
	}
	
	public void saveOffice(OfficeForm.SaveEvent event) {
		dbservice.saveOffice(event.getOffice());
		updateList();
		closeOfficeEditor();
		
		
	}
	
	public void saveCell(CellForm.SaveEvent event) {
		dbservice.saveCell(event.getCell());
		updateList();
		closeCellEditor();
	}
	public void saveEvent(EventForm.SaveEvent event) {
		dbservice.saveEvent(event.getMasterEvent());
		updateList();
		closeOfficeEditor();
		
		
	}
	private Component getToolbar() {
		// TODO Auto-generated method stub
		Button addOfficebutton=new  Button("New Office");
		Button addCellbutton=new  Button("New Cell");
		Button addEventbutton=new  Button("New Event");
		addOfficebutton.setIcon(new Icon(VaadinIcon.INSTITUTION));
		addCellbutton.setIcon(new Icon(VaadinIcon.BUILDING));
		addEventbutton.setIcon(new Icon(VaadinIcon.EXCHANGE));
		addOfficebutton.addClickListener(e-> addOffice());
		addCellbutton.addClickListener(e-> addCell()); 
		addEventbutton.addClickListener(e-> addEvent()); 
		HorizontalLayout toolbar=new HorizontalLayout(addEventbutton,addOfficebutton, addCellbutton);
		return toolbar;
	}
	
	private void updateList() {
		// TODO Auto-generated method stub
		officegrid.setItems(dbservice.findOfficesBydistrict());
		cellgrid.setItems(dbservice.findCellsBydistrict());
		eventgrid.setItems(dbservice.findEventsBydistrict());
	}

	public void deleteOffice(OfficeForm.DeleteEvent event) {
		dbservice.deleteOffice(event.getOffice());
		updateList();
		closeOfficeEditor();
	}
	 public void deleteCell(CellForm.DeleteEvent event) {
		 dbservice.deleteCell(event.getCell());
		 updateList();
		 closeCellEditor();
	 }
	 public void deleteEvent(EventForm.DeleteEvent event) {
			dbservice.deleteEvent(event.getMasterEvent());
			updateList();
			closeOfficeEditor();
		}
	private void configureGrid() {
		officegrid.setSizeFull();
		cellgrid.setSizeFull();
		eventgrid.setSizeFull();
		officegrid.setColumns("officeName","officeColour");
		cellgrid.setColumns("cellName", "cellColour");
		//cellgrid.setColumns("cellColour");
    	officegrid.getColumns().forEach(col-> col.setAutoWidth(true));
    	cellgrid.getColumns().forEach(col-> col.setAutoWidth(true));
    	eventgrid.setColumns("eventName","isdefault");    	
    	officegrid.asSingleSelect().addValueChangeListener(e-> editOffice(e.getValue()));
    	cellgrid.asSingleSelect().addValueChangeListener(e->editCell(e.getValue()));
    	eventgrid.asSingleSelect().addValueChangeListener(e->editEvent(e.getValue()));
		
	}
	private void addOffice() {
		officegrid.asSingleSelect().clear();
		editOffice(new Office());
	}
	private void editOffice(Office office) {
		// TODO Auto-generated method stub
		cellform.setVisible(false);
		eventform.setVisible(false);
		if(office==null) {
			closeOfficeEditor();
		}else {
			form.setOffice(office);
			form.setVisible(true);
			addClassName("editing");
		}
	}
	private void addCell() {
		officegrid.asSingleSelect().clear();
		editCell(new Cell());
	}
	private void editCell(Cell cell) {
		// TODO Auto-generated method stub
		form.setVisible(false);
		eventform.setVisible(false);
		if(cell==null) {
			closeOfficeEditor();
		}else {
			cellform.setCell(cell);
			cellform.setVisible(true);
			addClassName("editing");
		}
	}
	private void addEvent() {
		eventgrid.asSingleSelect().clear();
		editEvent(new MasterEvent());
	}
	private void editEvent(MasterEvent event) {
		form.setVisible(false);
		cellform.setVisible(false);
		//System.out.println("Event-"+event.getEventName());
		if(event==null) {
			closeEventEditor();
			
		}else {
			eventform.setMasterEvent(event);
			eventform.setVisible(true);
			addClassName("editing");
		}
	}
	private Component getContent() {
		// TODO Auto-generated method stub
		HorizontalLayout content=new HorizontalLayout(eventgrid,officegrid,cellgrid, form, cellform,eventform);
		content.setFlexGrow(1, officegrid);
		content.setFlexGrow(1, cellgrid);
		content.setFlexGrow(1, eventgrid);
		content.setFlexGrow(1, form);
		content.setFlexGrow(1, cellform);
		content.setFlexGrow(1, eventform);
		content.addClassName("content");
		content.setSizeFull();
		return content;
	}

	
	
}
