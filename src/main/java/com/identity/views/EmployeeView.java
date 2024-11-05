package com.identity.views;

import java.io.ByteArrayInputStream;

import com.identity.dbservice.DbService;
import com.identity.entity.Employee;
import com.identity.views.EmployeeForm;
import com.identity.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.StreamResource;

import jakarta.annotation.security.RolesAllowed;
import software.xdev.vaadin.grid_exporter.GridExporter;


@PageTitle("Personnel")
@Route(value = "", layout=MainLayout.class)
@RolesAllowed({ "USER", "SUPER", "ADMIN" })
public class EmployeeView extends VerticalLayout {
	Grid<Employee> grid = new Grid<>(Employee.class);
	
	//Grid<Employee> grid = new Grid<>();
	TextField filterText = new TextField();
	private DbService dbservice;
	EmployeeForm form;
	
	public EmployeeView(DbService dbservice) {
		this.dbservice=dbservice;
		addClassName("list-view");
		setSizeFull();
		configureGrid();
		configureForm();
		add (getToolbar(), getContent());
		updateList();
		closeEditor();
		//form.upload.
	}
	
		
	private void closeEditor() {
		// TODO Auto-generated method stub
		form.setEmployee(null);
		form.setVisible(false);
		//form.upload.re
		removeClassName("editing");
		
	}
	private Component getContent() {
		// TODO Auto-generated method stub
		HorizontalLayout content=new HorizontalLayout(grid,form);
		content.setFlexGrow(2, grid);
		content.setFlexGrow(1, form);
		content.addClassName("content");
		content.setSizeFull();
		return content;
	}
	private void configureForm() {
		// TODO Auto-generated method stub
		form=new EmployeeForm(dbservice.findAllOffices(), dbservice.findAllCells(), dbservice);
		form.setWidth("30%");
		form.addListener(EmployeeForm.SaveEvent.class, this::saveEmployee);
		form.addListener(EmployeeForm.DeleteEvent.class, this::deleteEmployee);
		form.addListener(EmployeeForm.CloseEvent.class, e->closeEditor());
	}
	
	public void saveEmployee(EmployeeForm.SaveEvent event) {
		dbservice.saveEmployee(event.getEmployee());
		//dbservice.saveEmployee(event.getEmployee().getPicture());
		//Employee emptoupdate= dbservice.
		updateList();
		closeEditor();
		
	}
	public void deleteEmployee(EmployeeForm.DeleteEvent event) {
		dbservice.deleteEmployee(event.getEmployee());
		updateList();
		closeEditor();
	}
	
	private void updateList() {
		// TODO Auto-generated method stub
		grid.setItems(dbservice.findAllEmployees(filterText.getValue()));
		configureGrid();
	}
	private Component getToolbar() {
		// TODO Auto-generated method stub
		filterText.setPlaceholder("Filter By Name");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e-> updateList());
		filterText.setWidth("350px");
		Button addButton=new  Button("Add New");
		Button expButton=new  Button("Export");
		addButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
		expButton.setIcon(new Icon(VaadinIcon.EXTERNAL_LINK));
		addButton.addClickListener(e-> addContact());
		expButton.addClickListener(e-> export());
		HorizontalLayout toolbar=new HorizontalLayout(filterText, addButton, expButton);
		return toolbar;
	}
	private void export() {
		GridExporter.newWithDefaults(grid).open();
	}
	private void addContact() {
		grid.asSingleSelect().clear();
		editContact(new Employee());
	}
	private void configureGrid() {
		//Column cellcoumn=new Column(grid, null, null);
		grid.addClassName("contact-grid");
		grid.setSizeFull();
		//grid.addColumn(employee->employee.getEid()).setHeader("Id No");
		grid.setColumns("serialNo","firstName", "lastName", "designation");
    	grid.addColumn(employee -> employee.getOffice().getOfficeName()).setHeader("Office").setSortable(true);
    	grid.addColumn(employee -> employee.getCell().getCellName()).setHeader("Cell").setFooter("Total Entries: "+dbservice.getEmployeeCount()).setSortable(true);
    	//grid.addColumn();
    	grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    	grid.addColumns("enteredOn", "enteredBy");
    	grid.getColumns().forEach(col-> col.setAutoWidth(true));
		grid.asSingleSelect().addValueChangeListener(e-> editContact(e.getValue()));
		
	}
	private void editContact(Employee employee) {
		if(employee==null) {
			closeEditor();
		} else {
			//form.upload.getElement().setPropertyJson("files", Json.createArray());
			form.setEmployee(employee);
			form.removeImage();
			byte[] picture = employee.getPicture();
			if(picture!=null)
			{	
				StreamResource resource = new StreamResource("image.jpg", () -> new ByteArrayInputStream(picture));
				Image image = new Image(resource, "No Image");
				form.addImage(image);
			}else {
				form.removeImage();
			}
			form.setVisible(true);
			addClassName("editing");
		}
	}
}
