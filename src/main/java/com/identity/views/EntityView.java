package com.identity.views;



import com.identity.dbservice.DbService;
import com.identity.entity.District;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"SUPER"})
@PageTitle("Districts")
@Route(value = "NewEntity", layout = MainLayout.class)
public class EntityView extends VerticalLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Grid<District> grid=new Grid<>(District.class);
	DbService service;
	EntityForm form=new EntityForm(service);
	Button newButtton=new Button("New");
	public EntityView(DbService service) {
		this.service=service;
		// TODO Auto-generated constructor stub
		setSizeFull();
		configureForms();
		getUsergrid();
		closeEditor();
		newButtton.addClickListener(e->newDistrict());
		add(newButtton, getContent());
		form.populateState();
	}
	
	private Component getContent() {
		
		HorizontalLayout content=new HorizontalLayout(grid, form);
		content.setFlexGrow(1, grid);
		content.setFlexGrow(1, form);
		//content.setFlexGrow(1, constiform);
		getUsergrid();
		content.setSizeFull();
		return content;
	}
	public void getUsergrid() {
		grid.removeAllColumns();
		grid.addColumn(district->district.getDistrictId()).setHeader("Id").setSortable(true).setResizable(true);
		grid.addColumn(district->district.getDistrictName()).setHeader("Department").setSortable(true).setResizable(true);
		grid.addColumn(district->district.getState().getStateName()).setHeader("State").setSortable(true).setResizable(true);
		grid.setItems(service.getDistricts());
		grid.asSingleSelect().addValueChangeListener(e->editDistrict(e.getValue()));
		grid.setSizeFull();
		//return grid;
	}
	
	private void editDistrict(District user) {
		// TODO Auto-generated method stub
		form.setVisible(false);
		if (user == null) {
			form.setVisible(false);
		} else {
			form.setDistrict(user);
			form.setVisible(true);
			//yearform.setYear(year);
			
		}
	}

	private void newDistrict() {
		// TODO Auto-generated method stub
		form.setDistrict(new District());
		form.setVisible(true);
		// yearform.setYear(year);

	}
	private void configureForms() {
		form.setVisible(false);
		form=new EntityForm(service);
		form.setWidth("20%");
		form.addListener(EntityForm.SaveEvent.class, this::saveEntity);
		
		
	}
	public void saveEntity(EntityForm.SaveEvent event) {
		service.saveDistrict(event.getDistrict());
		updateGrids();
		closeEditor();
	}

	private void updateGrids() {
		grid.setItems(service.getDistricts());
	}
	private void closeEditor() {
		form.setDistrict(null);
		form.setVisible(false);

	}
}
