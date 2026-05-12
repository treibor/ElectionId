package com.identity.dbservice;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.identity.entity.Candidate;
import com.identity.entity.Constituency;
import com.identity.entity.District;
import com.identity.entity.Districtmaster;
import com.identity.entity.MasterEvent;
import com.identity.entity.Party;
import com.identity.entity.Political;
import com.identity.entity.State;
import com.identity.entity.Users;
import com.identity.repository.CandidateRepository;
import com.identity.repository.ConstituencyRepository;
import com.identity.repository.DistrictRepository;
import com.identity.repository.DistrictmasterRepository;
import com.identity.repository.EventRepository;
import com.identity.repository.PartyRepository;
import com.identity.repository.PoliticalRepository;
import com.identity.repository.StateRepository;
import com.identity.repository.UsersRepository;
import com.identity.util.NotificationUtil;
import com.vaadin.flow.component.notification.Notification;

@Service
public class DbServicePol {
	@Autowired
	AuditService auditService;
	private final CandidateRepository canrepo;
	private final ConstituencyRepository conrepo;
	private final PartyRepository prepo;
	private final DistrictRepository drepo;
	private final StateRepository srepo;
	private final UsersRepository urepo;
	private final PoliticalRepository polrepo;
	private final DistrictmasterRepository dmrepo;
	private final EventRepository eventrepo;
	Notification notify=new Notification();
	public DbServicePol(CandidateRepository canrepo, ConstituencyRepository conrepo, PartyRepository prepo, DistrictRepository drepo, StateRepository srepo, UsersRepository urepo, PoliticalRepository polrepo,DistrictmasterRepository dmrepo,EventRepository eventrepo) {
		this.canrepo = canrepo;
		this.conrepo = conrepo;
		this.prepo = prepo;
		this.drepo=drepo;
		this.srepo=srepo;
		this.urepo=urepo;
		this.polrepo=polrepo;
		this.dmrepo=dmrepo;
		this.eventrepo = eventrepo;
	}
	public Users getUser() {
		return urepo.findByUserNameAndEnabled(getloggeduser(), true);
	}
	public boolean isAdmin() {
		if (getUser().getRole().equals("ADMIN") || getUser().getRole().equals("SUPER")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isSuper() {
		if (getUser().getRole().equals("SUPER")) {
			return true;
		} else {
			return false;
		}
	}
	public MasterEvent getDefaultEvent() {
		MasterEvent event = eventrepo.findByDistrictAndIsdefault(getLoggedDistrict(), true);

		if (event == null) {
			NotificationUtil.showError("Event Has Not Been Initialized. Please contact your Administrator");
			return null;
		}

		return event;
	}
	public List<MasterEvent> getEvents() {
		return eventrepo.findByDistrict(getLoggedDistrict());
	}
	
	public List<MasterEvent> findEventsBydistrict() {
		return eventrepo.findByDistrict(getLoggedDistrict());
	}
	public List<MasterEvent> findEventsBydistrictAndNotDefault() {
		return eventrepo.findByDistrictAndIsdefaultOrderByEventNameAsc(getLoggedDistrict(),false);
	}
	
	public long getPoliticalCount() {
		return polrepo.countBydistrictAndEvent(getLoggedDistrict(), getDefaultEvent());
	}
	public Districtmaster getDistrictMasterByLabel(String label) {
		return dmrepo.findByDistrictAndMasterLabel(getLoggedDistrict(), label);
		//return null;
	}
	
	public Candidate getCandidateComboBox(Party party, Constituency consti){
		return canrepo.getCandidateComboBox(party, consti);
	}
	
	public Candidate getCandidateDetails(String name) {
		return canrepo.findByCandidateName(name);
	}
	public District getDistrictMaster() {
		return drepo.findByDistrictId(getLoggedDistrict().getDistrictId());
		//return null;
	}
	public void saveDistrict(District district) {
		if (district==null) {
			NotificationUtil.showError("No Employee To Save");
			return;
		}
		drepo.save(district);
		
	}
	
	//**** Begin Report Query
	public List <Political> findPoliticalByRange(long from, long to, String type){
		return polrepo.getRangeQuery(from, to, getDistrictMaster(), getDistrictMasterByLabel(type), getDefaultEvent());
	}
	public List <Political> findPoliticalByDate(LocalDate from, LocalDate to, String type){
		return polrepo.getDatesQuery(from, to, getDistrictMaster(), getDistrictMasterByLabel(type), getDefaultEvent());
	}
	public List <Political> findPoliticalByParty(Party party, String type){
		return polrepo.getPartyQuery(party, getDistrictMaster(), getDistrictMasterByLabel(type), getDefaultEvent());
	}
	public List <Political> findPoliticalByCandidate(Candidate candi, String type){
		return polrepo.getCandiQuery(candi, getDistrictMaster(), getDistrictMasterByLabel(type), getDefaultEvent());
	}
	public List <Political> findPoliticalByConstituency(Constituency consti, String type){
		return polrepo.getConstiQuery(consti, getDistrictMaster(), getDistrictMasterByLabel(type), getDefaultEvent());
	}
	
	//****** End Report Query
	
	
	public List <Political> findPoliticalBydistrict(){
		return polrepo.findByDistrict(getLoggedDistrict());
	}
	
	public List<Political> findAllPoliticals(String filterText) {
		
		District district = getLoggedDistrict();

		MasterEvent event = getDefaultEvent();

		if (event == null) {
			return Collections.emptyList();
		}

		if (filterText == null || filterText.trim().isEmpty()) {
			return polrepo.findByDistrictAndEventOrderBySerialNoDesc(district, event);
		}

		return polrepo.search(filterText.trim(), district, event);
	}
	public List<Political> findAllPoliticals(MasterEvent event, String filterText) {
		District district = getLoggedDistrict();
		if (event == null) {
			return Collections.emptyList();
		}

		if (filterText == null || filterText.trim().isEmpty()) {
			return polrepo.findByDistrictAndEventOrderBySerialNoDesc(district, event);
		}

		return polrepo.search(filterText.trim(), district, event);
	}
	public void savePolitical(Political political) {
		if (political==null) {
			NotificationUtil.showError("No Record To Save");
			return;
		}
		political.setEvent(getDefaultEvent());
		polrepo.save(political);
		auditService.saveAudit("Save", "Political Agent", political.getPoliticalId()+" - " +political.getFirstName()+" - "+political.getLastName(), getloggeduser());
	}
	public void savePolitical(List<Political> employees) {
	    if (employees == null || employees.isEmpty()) {
	        Notification.show("No Political Agent To Save");
	        return;
	    }

	    if (getDefaultEvent() == null) {
	        NotificationUtil.showError("Event Has Not Been Initialized. Please contact your Administrator");
	        return;
	    }

	    for (Political political : employees) {
	        political.setEvent(getDefaultEvent());
	    }

	    List<Political> savedPoliticalList = polrepo.saveAll(employees);

	    for (Political political : savedPoliticalList) {
	        auditService.saveAudit(
	                "Bulk Save",
	                "Political Agent",
	                political.getPoliticalId() + " - " 
	                        + political.getFirstName()+" - "+political.getLastName(),
	                getloggeduser()
	        );
	    }
	}
	public void deletePolitical(Political political) {
		try {
			auditService.saveAudit("Delete", "Political Agent", political.getPoliticalId()+" - " +political.getFirstName()+" - "+political.getLastName(), getloggeduser());
			polrepo.delete(political);
		} catch (Exception e) {
			// TODO: handle exception
			NotificationUtil.showError("Unable to Delete Constituency. Error Code: " + e);
		}
	}
	public List <Party> findPartyBydistrict(){
		return prepo.findByDistrict(getLoggedDistrict());
	}
	
	public List <Constituency> findConstBydistrict(){
		return conrepo.findByDistrict(getLoggedDistrict());
	}
	
	public List <Candidate> findCandidateBydistrict(){
		return canrepo.findByDistrict(getLoggedDistrict());
	}
	public List <Candidate> findCandidateByParty(Party party){
		return canrepo.getCandidatesList(getLoggedDistrict(), party);
	}
	public List <Candidate> findCandidateByConsti(Constituency consti){
		return canrepo.getCandidatesList(getLoggedDistrict(), consti);
	}
	public long findMaxSerialNo() {
		
		try {
			//System.out.println("Returned:"+erepo.findMaxSerial(getLoggedDistrict()));
			return polrepo.findMaxSerial(getLoggedDistrict(), getDefaultEvent());
		} catch (NullPointerException e) {
			//e.printStackTrace();
			return (long) 0;
			
		}
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
	
	public void saveParty(Party party) {
		if (party==null) {
			NotificationUtil.showError("No Office To Save");
			return;
		}
		prepo.save(party);
		auditService.saveAudit("Save", "Party", party.getPartyId()+" - " +party.getPartyName(), getloggeduser());
	}
	public void deleteParty(Party party) {
		try {
			auditService.saveAudit("Delete", "Party", party.getPartyId()+" - " +party.getPartyName(), getloggeduser());
			prepo.delete(party);
		} catch (Exception e) {
			// TODO: handle exception
			NotificationUtil.showError("Unable to Delete Office. Error Code: " + e);
		}
	}
	
	public void saveConstituency(Constituency consti) {
		if(consti==null) {
			NotificationUtil.showError("Select Contituency " );
		}
		conrepo.save(consti);
	}
	
	public void deleteConstituency(Constituency consti) {
		try {
			auditService.saveAudit("Delete", "Constituency", consti.getConstituencyId()+" - " +consti.getConstituencyName(), getloggeduser());
			conrepo.delete(consti);
		} catch (Exception e) {
			// TODO: handle exception
			NotificationUtil.showError("Unable to Delete Constituency. Error Code: " + e);
		}
	}
	public void saveCandidate(Candidate consti) {
		if(consti==null) {
			NotificationUtil.showError("No Candidate To Save");
			return;
		}
		canrepo.save(consti);
		auditService.saveAudit("Save", "Candidate", consti.getCandId()+" - " +consti.getCandidateName(), getloggeduser());
	}
	
	public void deleteCandidate(Candidate consti) {
		try {
			auditService.saveAudit("Delete", "Candidate", consti.getCandId()+" - " +consti.getCandidateName(), getloggeduser());
			canrepo.delete(consti);
		} catch (Exception e) {
			// TODO: handle exception
			NotificationUtil.showError("Unable to Delete Candidate. Error Code: " + e);
		}
	}
	
}