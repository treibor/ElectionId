package com.identity.views;

import com.identity.dbservice.DbService;
import com.identity.entity.Cell;
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

@PageTitle("Master Data")
@Route(value = "master", layout=MainLayout.class)
public class OfficeView extends VerticalLayout {
	Grid <Office> officegrid=new Grid<>(Office.class);
	Grid <Cell> cellgrid=new Grid<>(Cell.class);
	private DbService dbservice;
	OfficeForm form;
	CellForm cellform;
	public OfficeView(DbService dbservice) {
		this.dbservice=dbservice;
		setSizeFull();
		configureGrid();
		configureForm();
		add(getToolbar(),getContent());
		updateList();
		//addOffice();
		closeCellEditor();
		closeOfficeEditor();
	}
	
	private void closeCellEditor() {
		// TODO Auto-generated method stub
		cellform.setCell(null);
		cellform.setVisible(false);
		removeClassName("editing");
		
	}
	private void closeOfficeEditor() {
		// TODO Auto-generated method stub
		form.setOffice(null);
		form.setVisible(false);
		removeClassName("editing");
		
	}
	private void configureForm() {
		// TODO Auto-generated method stub
		form=new OfficeForm(dbservice);
		cellform=new CellForm(dbservice);
		form.setWidth("35em");
		cellform.setWidth("35em");
		form.addListener(OfficeForm.SaveEvent.class, this::saveOffice);
		form.addListener(OfficeForm.DeleteEvent.class, this::deleteOffice);
		form.addListener(OfficeForm.CloseEvent.class, e->closeOfficeEditor());
		cellform.addListener(CellForm.SaveEvent.class, this::saveCell);
		cellform.addListener(CellForm.DeleteEvent.class, this::deleteCell);
		cellform.addListener(CellForm.DeleteEvent.class, e->closeCellEditor());
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
	private Component getToolbar() {
		// TODO Auto-generated method stub
		Button addOfficebutton=new  Button("New Office");
		Button addCellbutton=new  Button("New Cell");
		addOfficebutton.setIcon(new Icon(VaadinIcon.INSTITUTION));
		addCellbutton.setIcon(new Icon(VaadinIcon.BUILDING));
		addOfficebutton.addClickListener(e-> addOffice());
		addCellbutton.addClickListener(e-> addCell()); ///////////////CHANGE METHOD
		HorizontalLayout toolbar=new HorizontalLayout(addOfficebutton, addCellbutton);
		return toolbar;
	}
	
	private void updateList() {
		// TODO Auto-generated method stub
		officegrid.setItems(dbservice.findOfficesBydistrict());
		cellgrid.setItems(dbservice.findCellsBydistrict());
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
	private void configureGrid() {
		officegrid.setSizeFull();
		cellgrid.setSizeFull();
		officegrid.setColumns("officeName");
		cellgrid.setColumns("cellName", "cellColour");
		//cellgrid.setColumns("cellColour");
    	officegrid.getColumns().forEach(col-> col.setAutoWidth(true));
    	cellgrid.getColumns().forEach(col-> col.setAutoWidth(true));
    	officegrid.asSingleSelect().addValueChangeListener(e-> editOffice(e.getValue()));
    	cellgrid.asSingleSelect().addValueChangeListener(e->editCell(e.getValue()));
		
	}
	private void addOffice() {
		officegrid.asSingleSelect().clear();
		editOffice(new Office());
	}
	private void editOffice(Office office) {
		// TODO Auto-generated method stub
		cellform.setVisible(false);
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
		if(cell==null) {
			closeOfficeEditor();
		}else {
			cellform.setCell(cell);
			cellform.setVisible(true);
			addClassName("editing");
		}
	}
	private Component getContent() {
		// TODO Auto-generated method stub
		HorizontalLayout content=new HorizontalLayout(officegrid,cellgrid, form, cellform);
		content.setFlexGrow(1, officegrid);
		content.setFlexGrow(1, cellgrid);
		content.setFlexGrow(2, form);
		content.setFlexGrow(2, cellform);
		content.addClassName("content");
		content.setSizeFull();
		return content;
	}

	
	
}
