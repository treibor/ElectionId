package com.identity.views;


import com.identity.dbservice.DbService;
import com.identity.entity.District;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"SUPER"})
@PageTitle("Districts")
@Route(value = "District", layout = MainLayout.class)
public class DistrictView extends HorizontalLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Grid<District> grid=new Grid<>(District.class);
	DbService service;
	UsersForm form=new UsersForm(service);
	public DistrictView(DbService service) {
		this.service=service;
		// TODO Auto-generated constructor stub
		setSizeFull();
		configureForms();
		getUsergrid();
		closeEditor();
		add(getContent());
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
		grid.addColumn(district->district.getDistrictName()).setHeader("User Name").setSortable(true).setResizable(true);
		grid.addColumn(district->district.getState().getStateName()).setHeader("Role").setSortable(true).setResizable(true);
		grid.setItems(service.getDistricts());
		grid.asSingleSelect().addValueChangeListener(e->editUser(e.getValue()));
		grid.setSizeFull();
		//return grid;
	}
	
	private void editUser(District user) {
		// TODO Auto-generated method stub
		form.setVisible(false);
		if (user == null) {
			form.setVisible(false);
		} else {
			//form.setUsers(user);
			form.setVisible(true);
			//yearform.setYear(year);
			
		}
	}
	private void configureForms() {
		form.setVisible(false);
		form=new UsersForm(service);
		form.setWidth("20%");
		form.addListener(UsersForm.SaveEvent.class, this::saveUser);
		
		
	}
	public void saveUser(UsersForm.SaveEvent event) {
		service.saveUser(event.getUsers());
		updateGrids();
		closeEditor();
	}

	private void updateGrids() {
		grid.setItems(service.getDistricts());
	}
	private void closeEditor() {
		form.setUsers(null);
		form.setVisible(false);

	}
}
