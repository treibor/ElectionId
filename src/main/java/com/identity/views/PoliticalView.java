package com.identity.views;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.identity.dbservice.DbService;
import com.identity.dbservice.DbServicePol;
import com.identity.entity.Employee;
import com.identity.entity.MasterEvent;
import com.identity.entity.Political;
import com.identity.util.NotificationUtil;
import com.identity.entity.Political;
import com.identity.views.PoliticalForm;
import com.identity.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
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

@PageTitle("Political")
@Route(value = "political", layout=MainLayout.class)
@RolesAllowed({ "USER", "SUPER", "ADMIN" })
public class PoliticalView extends VerticalLayout {
	Grid<Political> grid = new Grid<>(Political.class);
	
	TextField filterText = new TextField();
	ComboBox<MasterEvent> event = new ComboBox<>("Select Event");
	private DbServicePol dbservice;
	private DbService dbservic;
	PoliticalForm form;
	TextField search=new TextField("Search");
	//CandidateForm candform=new CandidateForm(dbservice);
	public PoliticalView(DbServicePol dbservice, DbService dbservic) {
		this.dbservice=dbservice;
		this.dbservic=dbservic;
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
		form.setPolitical(null);
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
		form=new PoliticalForm(dbservice.findPartyBydistrict(), dbservice.findConstBydistrict(), dbservice.findCandidateBydistrict(),dbservic.findCellsBydistrict(), dbservice, dbservic);
		form.setWidth("30%");
		form.addListener(PoliticalForm.SaveEvent.class, this::savePolitical);
		form.addListener(PoliticalForm.DeleteEvent.class, this::deletePolitical);
		form.addListener(PoliticalForm.CloseEvent.class, e->closeEditor());
	}
	
	public void savePolitical(PoliticalForm.SaveEvent event) {
		dbservice.savePolitical(event.getPolitical());
		updateList();
		closeEditor();
		
	}
	public void deletePolitical(PoliticalForm.DeleteEvent event) {
		dbservice.deletePolitical(event.getPolitical());
		updateList();
		closeEditor();
	}
	
	private void updateList() {
		// TODO Auto-generated method stub
		grid.setItems(dbservice.findAllPoliticals(filterText.getValue()));
		configureGrid();
	}
	private Component getToolbar() {
		// TODO Auto-generated method stub
		filterText.setPlaceholder("Filter By Name, Cell, Party or Candidate");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e-> updateList());
		filterText.setWidth("350px");
		Button addButton=new  Button("Add New");
		addButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
		addButton.addClickListener(e-> addContact());
		Button impButton=new  Button("Import");
		Button expButton=new  Button("Print");
		expButton.setIcon(new Icon(VaadinIcon.EXTERNAL_LINK));
		expButton.addClickListener(e-> export());
		impButton.setIcon(new Icon(VaadinIcon.HARDDRIVE));
		impButton.addClickListener(e-> openImportDialog());
		//HorizontalLayout toolbar=new HorizontalLayout(filterText, addButton, impButton,expButton);
		FlexLayout toolbar = new FlexLayout(
	            filterText,
	            addButton,
	            impButton,
	            expButton
	      
	    );

	    toolbar.setWidthFull();
	    toolbar.setFlexWrap(FlexLayout.FlexWrap.WRAP);
	    toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
	    toolbar.getStyle()
	            .set("gap", "var(--lumo-space-s)")
	            .set("padding", "var(--lumo-space-s) 0");

	    // Let search field take available space on wide screens
	    toolbar.setFlexGrow(1, filterText);

	    
		return toolbar;
	}
	
	private void openImportDialog() {

	    Dialog dialog = new Dialog();
	    dialog.setWidth("90vw");
	    dialog.setHeight("90vh");
	    dialog.addClassName("history-dialog");
	    dialog.setHeaderTitle("Import Data From Other Events");

	    search.setPlaceholder("Filter By Name, Cell, Party or Candidate");
	    search.setClearButtonVisible(true);
	    search.setValueChangeMode(ValueChangeMode.LAZY);

	    event.setWidthFull();
	    event.setItems(dbservice.findEventsBydistrictAndNotDefault());
	    event.setItemLabelGenerator(MasterEvent::getEventName);
	    Grid<Political> employeegrid = new Grid<>(Political.class);
	    employeegrid.addClassName("contact-employeegrid");
	    employeegrid.setSizeFull();
	    employeegrid.setSelectionMode(Grid.SelectionMode.MULTI);
	    employeegrid.removeAllColumns();

	    employeegrid.addColumn(Political::getSerialNo)
	            .setSortable(true)
	            .setResizable(true)
	            .setHeader("Serial No");

	    employeegrid.addColumn(Political::getFirstName)
	            .setSortable(true)
	            .setResizable(true)
	            .setHeader("First Name");

	    employeegrid.addColumn(Political::getLastName)
	            .setSortable(true)
	            .setResizable(true)
	            .setHeader("Last Name");

	    employeegrid.addColumn(employee->employee.getCell().getCellName())
	            .setSortable(true)
	            .setResizable(true)
	            .setHeader("Cell");

	    employeegrid.addColumn(employee -> {
	        if (employee.getCandidate().getCandidateName() == null) {
	            return "";
	        }
	        return employee.getCandidate().getCandidateName();
	    }).setHeader("Candidate").setSortable(true).setResizable(true);
	    employeegrid.addColumn(employee -> {
	        if (employee.getParty().getPartyName() == null) {
	            return "";
	        }
	        return employee.getParty().getPartyName();
	    }).setHeader("Party").setSortable(true).setResizable(true);
	    employeegrid.addColumn(employee -> {
	        if (employee.getEvent() == null) {
	            return "";
	        }
	        return employee.getEvent().getEventName();
	    }).setHeader("Event").setSortable(true).setResizable(true);

	    employeegrid.addColumn(Political::getEnteredOn)
	            .setSortable(true)
	            .setResizable(true)
	            .setHeader("Entered On");

	    employeegrid.addColumn(Political::getEnteredBy)
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

	        Set<Political> selectedEmployees = employeegrid.getSelectedItems();

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

	

	public void search(Grid<Political> employeegrid) {

	    MasterEvent selectedEvent = event.getValue();

	    if (selectedEvent == null) {
	        employeegrid.setItems(List.of());
	        employeegrid.deselectAll();
	        return;
	    }

	    List<Political> employees = dbservice.findAllPoliticals(selectedEvent, search.getValue());

	    if (employees == null) {
	        employeegrid.setItems(List.of());
	    } else {
	        employeegrid.setItems(employees);
	    }

	    employeegrid.deselectAll();
	}
	@Transactional
	public void importEmployees(MasterEvent targetEvent, Set<Political> selectedEmployees) {

	    try {
	        if (targetEvent == null) {
	            NotificationUtil.showError("Default event has not been initialized. Please contact your Administrator");
	            return;
	        }

	        if (selectedEmployees == null || selectedEmployees.isEmpty()) {
	            NotificationUtil.showError("Please select at least one employee");
	            return;
	        }

	        List<Political> newEmployees = new ArrayList<Political>();

	        long nextSerialNo = dbservice.findMaxSerialNo();

	        if (nextSerialNo == 0) {
	            nextSerialNo = 1;
	        } else {
	            nextSerialNo = nextSerialNo + 1;
	        }

	        for (Political oldEmployee : selectedEmployees) {

	        	Political newEmployee = new Political();

	            newEmployee.setSerialNo(nextSerialNo);
	            nextSerialNo++;

	            newEmployee.setFirstName(oldEmployee.getFirstName());
	            newEmployee.setLastName(oldEmployee.getLastName());
	            newEmployee.setCandidate(oldEmployee.getCandidate());
	            newEmployee.setParty(oldEmployee.getParty());
	            newEmployee.setDistrict(oldEmployee.getDistrict());
	            newEmployee.setCell(oldEmployee.getCell());
	            newEmployee.setState(oldEmployee.getState());
	            newEmployee.setDistrictmaster(oldEmployee.getDistrictmaster());
	            newEmployee.setPicture(oldEmployee.getPicture());
	            newEmployee.setConstituency(oldEmployee.getConstituency());
	            newEmployee.setEvent(targetEvent);
	            
	            newEmployee.setEnteredOn(LocalDate.now());
	            newEmployee.setEnteredBy(dbservice.getloggeduser());

	            newEmployees.add(newEmployee);
	        }

	        dbservice.savePolitical(newEmployees);
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
		CandidateForm candform =new CandidateForm(dbservice);
		grid.asSingleSelect().clear();
		candform.updateComboxes();
		editContact(new Political());
	}
	private void configureGrid() {
		grid.addClassName("contact-grid");
		grid.setSizeFull();
		grid.removeAllColumns();
		//grid.setColumns("serialNo","firstName", "lastName");
		grid.addColumn(political -> political.getSerialNo()).setHeader("Serial No").setSortable(true).setResizable(true);
		grid.addColumn(political -> political.getFirstName()).setHeader("First Name").setSortable(true).setResizable(true);
		grid.addColumn(political -> political.getLastName()).setHeader("Last Name").setSortable(true).setResizable(true);
		grid.addColumn(political -> political.getCell().getCellName()).setHeader("Cell").setSortable(true).setResizable(true);
		grid.addColumn(political -> political.getCandidate().getCandidateName()).setHeader("Candidate").setSortable(true).setResizable(true);
    	grid.addColumn(political -> political.getParty().getPartyName()).setHeader("Party").setSortable(true).setResizable(true);
    	grid.addColumn(political -> political.getConstituency().getConstituencyName()).setHeader("Constituency").setSortable(true).setResizable(true).setFooter("Total Entries: "+ dbservice.getPoliticalCount());
    	grid.addColumn(political -> political.getEvent().getEventName()).setHeader("Event").setSortable(true);
    	grid.addColumn(political -> political.getEnteredBy()).setHeader("Entered By").setSortable(true).setResizable(true);
    	grid.addColumn(political -> political.getEnteredOn()).setHeader("Entered On").setSortable(true).setResizable(true);
    	//grid.addColumns("enteredOn", "enteredBy");
    	//grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
    	grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,GridVariant.LUMO_WRAP_CELL_CONTENT);
    	grid.getColumns().forEach(col-> col.setAutoWidth(true));
		grid.asSingleSelect().addValueChangeListener(e-> editContact(e.getValue()));
	}
	private void editContact(Political political) {
		if(political==null) {
			closeEditor();
		} else {
			//form.upload.getElement().setPropertyJson("files", Json.createArray());
			form.setPolitical(political);
			form.removeImage();
			byte[] picture = political.getPicture();
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
