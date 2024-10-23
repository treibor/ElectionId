package com.identity.views;

import com.identity.dbservice.DbService;
import com.identity.dbservice.DbServicePol;
import com.identity.entity.Candidate;
import com.identity.entity.Cell;
import com.identity.entity.Constituency;
import com.identity.entity.Office;
import com.identity.entity.Party;
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
@Route(value = "masterpolitical", layout=MainLayout.class)
@RolesAllowed({ "USER", "SUPER", "ADMIN" })
public class PoliticalMasterView extends VerticalLayout {
	Grid <Party> partygrid=new Grid<>(Party.class);
	Grid <Constituency> constgrid=new Grid<>(Constituency.class);
	Grid <Candidate> candgrid=new Grid<>(Candidate.class);
	private DbServicePol dbservice;
	PartyForm partyform;
	ConstituencyForm constform;
	CandidateForm candform;
	
	public PoliticalMasterView(DbServicePol dbservice) {
		this.dbservice=dbservice;
		setSizeFull();
		configureGrid();
		configureForm();
		add(getToolbar(),getContent());
		updateList();
		closePartyEditor();
		closeConstituencyEditor();
		closeCandidateEditor();
	}
	
	private void closePartyEditor() {
		// TODO Auto-generated method stub
		partyform.setParty(null);
		partyform.setVisible(false);
		candform.updateComboxes();
		
	}
	private void closeConstituencyEditor() {
		// TODO Auto-generated method stub
		constform.setConstituency(null);
		constform.setVisible(false);
		candform.updateComboxes();
	}
	private void closeCandidateEditor() {
		// TODO Auto-generated method stub
		candform.setCandidate(null);
		candform.setVisible(false);
		candform.updateComboxes();
	}
	private void configureForm() {
		// TODO Auto-generated method stub
		partyform=new PartyForm(dbservice);
		constform=new ConstituencyForm(dbservice);
		candform=new CandidateForm(dbservice);
		partyform.setMaxWidth("20em");
		constform.setMaxWidth("20em");
		candform.setMaxWidth("20em");
		partyform.addListener(PartyForm.SaveEvent.class, this::saveParty);
		partyform.addListener(PartyForm.DeleteEvent.class, this::deleteParty);
		partyform.addListener(OfficeForm.CloseEvent.class, e->closePartyEditor());
		
		constform.addListener(ConstituencyForm.SaveEvent.class, this::saveConstituency);
		constform.addListener(ConstituencyForm.DeleteEvent.class, this::deleteConstituency);
		candform.addListener(CandidateForm.SaveEvent.class, this::saveCandidate);
		candform.addListener(CandidateForm.DeleteEvent.class, this::deleteCandidate);
	}
	
	
	private Component getToolbar() {
		// TODO Auto-generated method stub
		Button addPartybutton=new  Button("New Party", new Icon(VaadinIcon.FLAG));
		Button addConstbutton=new  Button("New Constituency", new Icon(VaadinIcon.PYRAMID_CHART));
		Button addCandbutton=new  Button("New Candidate",new Icon(VaadinIcon.USER_CLOCK));
		
		addPartybutton.addClickListener(e-> addParty());
		addConstbutton.addClickListener(e-> addConstituency());
		addCandbutton.addClickListener(e-> addCandidate());
		HorizontalLayout toolbar=new HorizontalLayout(addPartybutton, addConstbutton, addCandbutton);
		return toolbar;
	}
	
	private void updateList() {
		// TODO Auto-generated method stub
		partygrid.setItems(dbservice.findPartyBydistrict());
		constgrid.setItems(dbservice.findConstBydistrict());
		candgrid.setItems(dbservice.findCandidateBydistrict());
	}

	
	private void configureGrid() {
		partygrid.setMaxWidth("20%");
		partygrid.setHeight("100%");
		constgrid.setMaxWidth("30%");
		constgrid.setHeight("100%");
		candgrid.setMaxWidth("50%");
		candgrid.setHeight("100%");
		//constgrid.setSizeFull();
		//candgrid.setSizeFull();
		partygrid.setColumns("partyName");
		constgrid.setColumns("constituencyName", "constituencyColour");
		candgrid.setColumns("candidateName");
		candgrid.addColumn(cand ->cand.getConstituency().getConstituencyName()).setHeader("Constituency");
		candgrid.addColumn(cand ->cand.getParty().getPartyName()).setHeader("Party");
    	partygrid.getColumns().forEach(col-> col.setAutoWidth(true));
    	constgrid.getColumns().forEach(col-> col.setAutoWidth(true));
    	candgrid.getColumns().forEach(col-> col.setAutoWidth(true));
    	partygrid.asSingleSelect().addValueChangeListener(e-> editParty(e.getValue()));
    	constgrid.asSingleSelect().addValueChangeListener(e->editConstituency(e.getValue()));
    	candgrid.asSingleSelect().addValueChangeListener(e->editCandidate(e.getValue()));
		
	}
	private void addParty() {
		partygrid.asSingleSelect().clear();
		editParty(new Party());
	}
	private void editParty(Party party) {
		// TODO Auto-generated method stub
		constform.setVisible(false);
		candform.setVisible(false);
		if(party==null) {
			closePartyEditor();
		}else {
			partyform.setParty(party);
			partyform.setVisible(true);
			//addClassName("editing");
		}
	}
	private void deleteParty(PartyForm.DeleteEvent event) {
		partygrid.asSingleSelect().clear();
		dbservice.deleteParty(event.getParty());
		updateList();
		closePartyEditor();
	}
	private void addConstituency() {
		constgrid.asSingleSelect().clear();
		editConstituency(new Constituency());
	}
	private void editConstituency(Constituency consti) {
		// TODO Auto-generated method stub
		partyform.setVisible(false);
		candform.setVisible(false);
		if(consti==null) {
			closeConstituencyEditor();
		}else {
			constform.setConstituency(consti);
			constform.setVisible(true);
			addClassName("editing");
		}
	}
	public void deleteConstituency(ConstituencyForm.DeleteEvent event) {
		constgrid.asSingleSelect().clear();
		dbservice.deleteConstituency(event.getConstituency());
		updateList();
		closeConstituencyEditor();
	}
	
	private void addCandidate() {
		candgrid.asSingleSelect().clear();
		editCandidate(new Candidate());
	}
	private void editCandidate(Candidate candi) {
		// TODO Auto-generated method stub
		partyform.setVisible(false);
		constform.setVisible(false);
		if(candi==null) {
			closeCandidateEditor();
		}else {
			candform.setCandidate(candi);
			candform.setVisible(true);
			addClassName("editing");
		}
	}
	public void deleteCandidate(CandidateForm.DeleteEvent event) {
		candgrid.asSingleSelect().clear();
		dbservice.deleteCandidate(event.getCandidate());
		updateList();
		closeCandidateEditor();
	}
	public void saveParty(PartyForm.SaveEvent event) {
		dbservice.saveParty(event.getParty());
		updateList();
		closePartyEditor();
		
	}
	
	public void saveConstituency(ConstituencyForm.SaveEvent event) {
		dbservice.saveConstituency(event.getConstituency());
		updateList();
		closeConstituencyEditor();
	}
	
	public void saveCandidate(CandidateForm.SaveEvent event) {
		dbservice.saveCandidate(event.getCandidate());
		updateList();
		closeCandidateEditor();
	}
	private Component getContent() {
		// TODO Auto-generated method stub
		HorizontalLayout content=new HorizontalLayout(partygrid,constgrid, candgrid, partyform, constform, candform);
		content.setFlexGrow(1, partygrid);
		content.setFlexGrow(1, constgrid);
		content.setFlexGrow(2, candgrid);
		content.setFlexGrow(1, partyform);
		content.setFlexGrow(1, constform);
		content.setFlexGrow(1, candform);
		content.addClassName("content");
		content.setSizeFull();
		return content;
	}

	
	
}
