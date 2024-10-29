package com.identity.dbservice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.identity.entity.Cell;
import com.identity.entity.District;
import com.identity.entity.Districtmaster;
import com.identity.entity.Employee;
import com.identity.entity.Office;
import com.identity.entity.State;
import com.identity.entity.Users;
import com.identity.repository.CellRepository;
import com.identity.repository.DistrictmasterRepository;
import com.identity.repository.DistrictRepository;
import com.identity.repository.EmployeeRepository;
import com.identity.repository.OfficeRepository;
import com.identity.repository.StateRepository;
import com.identity.repository.UsersRepository;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;

@Service
public class DbService {
	private final EmployeeRepository erepo;
	private final OfficeRepository orepo;
	private final CellRepository crepo;
	private final DistrictRepository drepo;
	private final StateRepository srepo;
	private final UsersRepository urepo;
	private final DistrictmasterRepository dmrepo;
	
	//Notification notify=new Notification();
	public DbService(EmployeeRepository erepo, OfficeRepository orepo, CellRepository crepo, DistrictRepository drepo, StateRepository srepo, UsersRepository urepo, DistrictmasterRepository dmrepo) {
		this.erepo = erepo;
		this.orepo = orepo;
		this.crepo = crepo;
		this.drepo=drepo;
		this.srepo=srepo;
		this.urepo=urepo;
		this.dmrepo=dmrepo;
	}
	/*
	public List<Employee> getEmployeesForReportByRange(long from, long to){
		return erepo.getreportQueryRange(getLoggedDistrict(), from, to); 
	}
	*/
	
	public long getEmployeeCount() {
		return erepo.countBydistrict(getLoggedDistrict());
	}
	public boolean isAdmin() {
		if(getUser().getRole().equals("ADMIN")||getUser().getRole().equals("SUPER")) {
			return true;
		}else {
			return false; 
		}
	}
	public List<State> findAllStates(){
		return srepo.findAll();
	}
	public List<District> findAllDistricts(){
		return drepo.findAll();
	}
	public List<Employee> getEmployeesByOffice(Office office, String label){
		return erepo.getreportQueryOffice(office, getLoggedDistrict(), getDistrictMasterByLabel(label)); 
		
	}
	
	public List<Employee> getEmployeesByCell(Cell cell, String label){
		return erepo.getreportQueryCell(cell, getLoggedDistrict(),getDistrictMasterByLabel(label)); 
	}
	public List<Employee> getEmployeesBetweenSerials(long fromSerial, long toSerial, String label){
		//return erepo.findBySerialNoBetweenAndDistrict(fromSerial, toSerial, getLoggedDistrict());
		return erepo.getreportQueryRange(getLoggedDistrict(), fromSerial, toSerial, getDistrictMasterByLabel(label));
	}
	public List<Employee> getEmployeesBetweenDates(LocalDate fromSerial, LocalDate toSerial, String label){
		return erepo.getreportQueryDates(getLoggedDistrict(),fromSerial, toSerial, getDistrictMasterByLabel(label));
	}
	
	/*public Districtmaster getDistrictMaster() {
		return dmrepo.findByDistrict(getLoggedDistrict());
		//return null;
	}*/
	
	public Districtmaster getDistrictMasterDistrict() {
		return dmrepo.findByDistrict(getLoggedDistrict());
		//return null;
	}
	public Districtmaster getDistrictMasterByLabel(String label) {
		return dmrepo.findByDistrictAndMasterLabel(getLoggedDistrict(), label);
		//return null;
	}
	public void saveDistrict(District district) {
		if (district==null) {
			Notification.show("No Entity To Save");
			return;
		}
		drepo.save(district);
		
	}
	public void saveDistrictMaster(Districtmaster districtmaster) {
		if (districtmaster==null) {
			Notification.show("No Employee To Save");
			return;
		}
		dmrepo.save(districtmaster);
		
	}
	public List<Employee> findEmployeesBydistrict(){
		return erepo.findBydistrict(getLoggedDistrict());
	}

	public long findMaxSerialNo() {
		
		try {
			//System.out.println("Returned:"+erepo.findMaxSerial(getLoggedDistrict()));
			return erepo.findMaxSerial(getLoggedDistrict());
		} catch (NullPointerException e) {
			//e.printStackTrace();
			return (long) 0;
			
		}
	}
	
	public List <Cell> findCellsBydistrict(){
		return crepo.findByDistrict(getLoggedDistrict());
	}
	
	public List <Office> findOfficesBydistrict(){
		return orepo.findByDistrict(getLoggedDistrict());
	}
	
	public String getloggeduser(){
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}
	public District getLoggedDistrict() {
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		String user=auth.getName();
		Users userobject=urepo.findByUserNameAndEnabled(user, true);
		return userobject.getDistrict();
		//auth=null;
	}
	public State getLoggedState() {
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		Users userobject=urepo.findByUserNameAndEnabled(auth.getName(), true);
		return userobject.getState();
	}
	 public Users getUser() {
		 return urepo.findByUserNameAndEnabled(getloggeduser(), true);
	 }
	 
	 public List<Users> getUsersByDistrict() {
		 if(getUser().getRole().equals("SUPER")){
			 return urepo.findAll();
		 }else {
			 return urepo.findByDistrict(getLoggedDistrict());
		 }
		 
	 } 
	 public List<District> getDistricts(){
		 return drepo.findAll();
	 }
	 public Users getUserByName(String username) {
		 return urepo.findByUserNameAndEnabled(username, true);
	 }
	public List<Employee> findAllEmployees(String filterText) {
		if (filterText == null || filterText.isEmpty()) {
			return findEmployeesBydistrict();
		} else {
			//return erepo.search(filterText, getLoggedDistrict());
			return erepo.search(filterText, getLoggedDistrict());
		}
	}
	
	public long employeeCount() {
		//erepo.
		return erepo.count();
	}
	
	
	public void deleteEmployee(Employee employee) {
		erepo.delete(employee);
	}
	
	public void saveUser(Users user) {
		if (user==null) {
			Notification.show("Fail Fail Fail-7734");
			return;
		}
		urepo.save(user);
		
	}
	public long getMaxUserId() {
		return urepo.findMaxSerial();
	}
	
	public void saveEmployee(Employee employee) {
		if (employee==null) {
			Notification.show("No Employee To Save");
			return;
		}
		erepo.save(employee);
		
	}
	public void saveOffice(Office office) {
		if (office==null) {
			Notification.show("No Office To Save");
			return;
		}
		orepo.save(office);
		
	}
	public void deleteOffice(Office office) {
		try {
		orepo.delete(office);
		}catch (Exception e) {
			// TODO: handle exception
			Notification.show("Unable to Delete Office. Error Code: "+e, 5000, Position.TOP_CENTER);
		}
	}
	
	public List<Office> findAllOffices(){
		return orepo.findAll();
	}
	public void saveCell(Cell cell) {
		if(cell==null) {
			Notification.show("No Cell Entered");
			return;
		}
		crepo.save(cell);
	}
	
	public void deleteCell(Cell cell) {
		try {
			crepo.delete(cell);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Notification.show("Unable to Delete Cell. Error Code: "+e, 5000, Position.TOP_CENTER);
			//e.printStackTrace();
		}
	}
	public List<Cell> findAllCells(){
		return crepo.findAll();
	}
	public List<Employee> findAllEmployees(){
		return erepo.findAll();
	}
	
}
