package com.identity.views;

import java.io.ByteArrayInputStream;

import com.identity.dbservice.DbService;
import com.identity.dbservice.DbServicePol;
import com.identity.entity.Political;
import com.identity.entity.Political;
import com.identity.views.PoliticalForm;
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
import com.vaadin.flow.server.StreamResource;

import jakarta.annotation.security.RolesAllowed;
import software.xdev.vaadin.grid_exporter.GridExporter;

@PageTitle("Political")
@Route(value = "political", layout=MainLayout.class)
@RolesAllowed({ "USER", "SUPER", "ADMIN" })
public class PoliticalView extends VerticalLayout {
	Grid<Political> grid = new Grid<>(Political.class);
	//Grid<Political> grid = new Grid<>();
	TextField filterText = new TextField();
	private DbServicePol dbservice;
	private DbService dbservic;
	PoliticalForm form;
	
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
		form.setWidth("25em");
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
		filterText.setPlaceholder("Filter By Name");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e-> updateList());
		filterText.setWidth("350px");
		Button addButton=new  Button("Add New");
		addButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
		addButton.addClickListener(e-> addContact());
		Button expButton=new  Button("Export");
		expButton.setIcon(new Icon(VaadinIcon.EXTERNAL_LINK));
		expButton.addClickListener(e-> export());
		HorizontalLayout toolbar=new HorizontalLayout(filterText, addButton, expButton);
		return toolbar;
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
    	//grid.addColumn(political -> political.getCandidate().getCandidateName()).setHeader("Candidate").setSortable(true);
    	grid.addColumn(political -> political.getEnteredBy()).setHeader("Entered By").setSortable(true).setResizable(true);
    	grid.addColumn(political -> political.getEnteredOn()).setHeader("Entered On").setSortable(true).setResizable(true);
    	//grid.addColumns("enteredOn", "enteredBy");
    	//grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
    	grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
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
