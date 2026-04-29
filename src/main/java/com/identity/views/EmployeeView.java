package com.identity.views;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.identity.dbservice.DbService;
import com.identity.entity.District;
import com.identity.entity.Employee;
import com.identity.entity.MasterEvent;
import com.identity.util.NotificationUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import software.xdev.vaadin.grid_exporter.GridExporter;


@PageTitle("Personnel")
@Route(value = "", layout=MainLayout.class)
@RolesAllowed({ "USER", "SUPER", "ADMIN" })
public class EmployeeView extends VerticalLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5313017582568561763L;

	Grid<Employee> grid = new Grid<>(Employee.class);
	//Grid<Employee> employeegrid = new Grid<>(Employee.class);
	ComboBox<MasterEvent> event = new ComboBox<>("Select Event");
	TextField filterText = new TextField();
	ComboBox<District> district=new ComboBox<District>();
	private DbService dbservice;
	EmployeeForm form;
	TextField search=new TextField("Search");
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
		grid.removeAllColumns();
		grid.setItems(dbservice.findAllEmployees(filterText.getValue()));
		configureGrid();
	}
	private void updateList(District district) {
		// TODO Auto-generated method stub
		grid.removeAllColumns();
		grid.setItems(dbservice.findAllEmployees(filterText.getValue()));
		configureGrid();
	}
	private Component getToolbar() {
		// TODO Auto-generated method stub
		filterText.setPlaceholder("Filter by Name, Designation or Office");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e-> updateList());
		filterText.setWidth("350px");
		district.setItems(dbservice.getDistricts());
		district.setItemLabelGenerator(District::getDistrictName);
		district.addValueChangeListener(e->getDistrictData(district.getValue()));
		Button addButton=new  Button("Add New");
		Button impButton=new  Button("Import");
		Button expButton=new  Button("Print");
		addButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
		expButton.setIcon(new Icon(VaadinIcon.EXTERNAL_LINK));
		impButton.setIcon(new Icon(VaadinIcon.HARDDRIVE));
		addButton.addClickListener(e-> addContact());
		expButton.addClickListener(e-> export());
		impButton.addClickListener(e-> openImportDialog());
		district.setVisible(dbservice.isSuper());
		HorizontalLayout toolbar=new HorizontalLayout(filterText, addButton,impButton, expButton, district);
		return toolbar;
	}
	private void getDistrictData(District district) {
		// TODO Auto-generated method stub
		updateList(district);
	}

	private void openImportDialog() {

	    Dialog dialog = new Dialog();
	    dialog.setWidth("90vw");
	    dialog.setHeight("90vh");
	    dialog.addClassName("history-dialog");
	    dialog.setHeaderTitle("Import Data From Other Events");

	    search.setPlaceholder("Filter by Name, Designation or Office");
	    search.setClearButtonVisible(true);
	    search.setValueChangeMode(ValueChangeMode.LAZY);
	    Grid<Employee> employeegrid = new Grid<>(Employee.class);
	    event.setWidthFull();
	    event.setItems(dbservice.findEventsBydistrictAndNotDefault());
	    event.setItemLabelGenerator(MasterEvent::getEventName);
	    employeegrid.removeAllColumns();
	    employeegrid.addClassName("contact-employeegrid");
	    employeegrid.setSizeFull();
	    employeegrid.setSelectionMode(Grid.SelectionMode.MULTI);
	   

	    employeegrid.addColumn(Employee::getSerialNo)
	            .setSortable(true)
	            .setResizable(true)
	            .setHeader("Serial No");

	    employeegrid.addColumn(Employee::getFirstName)
	            .setSortable(true)
	            .setResizable(true)
	            .setHeader("First Name");

	    employeegrid.addColumn(Employee::getLastName)
	            .setSortable(true)
	            .setResizable(true)
	            .setHeader("Last Name");

	    employeegrid.addColumn(Employee::getDesignation)
	            .setSortable(true)
	            .setResizable(true)
	            .setHeader("Designation");

	    employeegrid.addColumn(employee -> {
	        if (employee.getOffice() == null) {
	            return "";
	        }
	        return employee.getOffice().getOfficeName();
	    }).setHeader("Office").setSortable(true).setResizable(true);

	    employeegrid.addColumn(employee -> {
	        if (employee.getEvent() == null) {
	            return "";
	        }
	        return employee.getEvent().getEventName();
	    }).setHeader("Event").setSortable(true).setResizable(true);

	    employeegrid.addColumn(Employee::getEnteredOn)
	            .setSortable(true)
	            .setResizable(true)
	            .setHeader("Entered On");

	    employeegrid.addColumn(Employee::getEnteredBy)
	            .setSortable(true)
	            .setResizable(true)
	            .setHeader("Entered By");

	    employeegrid.addColumn(employee -> {
	        if (employee.getDistrict() == null) {
	            return "";
	        }
	        return employee.getDistrict().getDistrictName();
	    }).setSortable(true)
	            .setResizable(true)
	            .setHeader("District")
	            .setVisible(dbservice.isSuper());

	    employeegrid.getColumns().forEach(col -> col.setAutoWidth(true));

	    event.addValueChangeListener(e -> search(employeegrid));

	    search.addValueChangeListener(e -> search(employeegrid));
	    event.setWidth("350px");
	    search.setWidthFull();

	    HorizontalLayout toolbar = new HorizontalLayout(event, search);
	    toolbar.setWidthFull();
	    toolbar.setSpacing(true);

	    toolbar.setFlexGrow(0, event);
	    toolbar.setFlexGrow(1, search);
	    VerticalLayout content = new VerticalLayout(toolbar, employeegrid);
	    content.setSizeFull();
	    content.setPadding(false);
	    content.setSpacing(true);
	    content.expand(employeegrid);

	    dialog.add(content);

	    Button closeButton = new Button("Close", e -> dialog.close());

	    Button importButton = new Button("Import", e -> {

	        MasterEvent sourceEvent = event.getValue();

	        if (sourceEvent == null) {
	            NotificationUtil.showError("Please select an event first");
	            return;
	        }

	        MasterEvent defaultEvent = dbservice.getDefaultEvent();

	        if (defaultEvent == null) {
	            NotificationUtil.showError("Default event has not been initialized. Please contact your Administrator");
	            return;
	        }

	        if (sourceEvent.getId() == defaultEvent.getId()) {
	            NotificationUtil.showError("Cannot import from the default event into the same default event");
	            return;
	        }

	        Set<Employee> selectedEmployees = employeegrid.getSelectedItems();

	        if (selectedEmployees == null || selectedEmployees.isEmpty()) {
	            NotificationUtil.showError("Please select at least one employee to import");
	            return;
	        }

	        importEmployees(defaultEvent, selectedEmployees);
	        employeegrid.deselectAll();
	        //dialog.close();
	    });

	    importButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

	    dialog.getFooter().add(closeButton, importButton);

	    dialog.setModal(true);
	    dialog.setCloseOnOutsideClick(false);
	    dialog.setCloseOnEsc(false);

	    dialog.open();
	}

	

	public void search(Grid<Employee> employeegrid) {

	    MasterEvent selectedEvent = event.getValue();

	    if (selectedEvent == null) {
	        employeegrid.setItems(List.of());
	        employeegrid.deselectAll();
	        return;
	    }

	    List<Employee> employees = dbservice.findAllEmployees(selectedEvent, search.getValue());

	    if (employees == null) {
	        employeegrid.setItems(List.of());
	    } else {
	        employeegrid.setItems(employees);
	    }

	    employeegrid.deselectAll();
	}
	@Transactional
	public void importEmployees(MasterEvent targetEvent, Set<Employee> selectedEmployees) {

	    try {
	        if (targetEvent == null) {
	            NotificationUtil.showError("Default event has not been initialized. Please contact your Administrator");
	            return;
	        }

	        if (selectedEmployees == null || selectedEmployees.isEmpty()) {
	            NotificationUtil.showError("Please select at least one employee");
	            return;
	        }

	        List<Employee> newEmployees = new ArrayList<Employee>();

	        long nextSerialNo = dbservice.findMaxSerialNo();

	        if (nextSerialNo == 0) {
	            nextSerialNo = 1;
	        } else {
	            nextSerialNo = nextSerialNo + 1;
	        }

	        for (Employee oldEmployee : selectedEmployees) {

	            Employee newEmployee = new Employee();

	            newEmployee.setSerialNo(nextSerialNo);
	            nextSerialNo++;

	            newEmployee.setFirstName(oldEmployee.getFirstName());
	            newEmployee.setLastName(oldEmployee.getLastName());
	            newEmployee.setDesignation(oldEmployee.getDesignation());
	            newEmployee.setOffice(oldEmployee.getOffice());
	            newEmployee.setDistrict(oldEmployee.getDistrict());
	            newEmployee.setCell(oldEmployee.getCell());
	            newEmployee.setState(oldEmployee.getState());
	            newEmployee.setDistrictmaster(oldEmployee.getDistrictmaster());
	            newEmployee.setPicture(oldEmployee.getPicture());
	            newEmployee.setEvent(targetEvent);

	            newEmployee.setEnteredOn(LocalDate.now());
	            newEmployee.setEnteredBy(dbservice.getloggeduser());

	            newEmployees.add(newEmployee);
	        }

	        dbservice.saveEmployee(newEmployees);
	        updateList();
	        NotificationUtil.showSuccess(newEmployees.size() + " employee(s) imported successfully");

	    } catch (Exception e) {
	        e.printStackTrace();
	        NotificationUtil.showError("Import failed: " + e.getMessage());
	    }
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
		//grid.setColumns("serialNo","firstName", "lastName", "designation");
		grid.addColumn(employee -> employee.getSerialNo()).setSortable(true).setResizable(true).setHeader("Serial No");
		grid.addColumn(employee -> employee.getFirstName()).setSortable(true).setResizable(true).setHeader("First Name");
		grid.addColumn(employee -> employee.getLastName()).setSortable(true).setResizable(true).setHeader("Last Name");
		grid.addColumn(employee -> employee.getDesignation()).setSortable(true).setResizable(true).setHeader("Designation");
    	grid.addColumn(employee -> employee.getOffice().getOfficeName()).setHeader("Office").setSortable(true).setResizable(true).setHeader("Office");
    	grid.addColumn(employee -> employee.getCell().getCellName()).setHeader("Cell").setFooter("Total Entries: "+dbservice.getEmployeeCount()).setSortable(true).setResizable(true);
    	grid.addColumn(employee -> employee.getEvent().getEventName()).setHeader("Event").setSortable(true).setResizable(true);
    	grid.addColumn(employee -> employee.getEnteredOn()).setSortable(true).setResizable(true).setHeader("Entered On");
    	grid.addColumn(employee -> employee.getEnteredBy()).setSortable(true).setResizable(true).setHeader("Entered By");
    	grid.addColumn(employee -> employee.getDistrict().getDistrictName()).setSortable(true).setResizable(true).setHeader("District").setVisible(dbservice.isSuper());
    	//grid.addColumn();
    	//grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    	//grid.addColumns("enteredOn", "enteredBy");
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
