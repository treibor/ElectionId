package com.identity.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.identity.entity.Candidate;
import com.identity.entity.Cell;
import com.identity.entity.Constituency;
import com.identity.entity.District;
import com.identity.entity.Districtmaster;
import com.identity.entity.Employee;
import com.identity.entity.MasterEvent;
import com.identity.entity.Party;
//import com.identity.entity.Employee;
import com.identity.entity.Political;

public interface PoliticalRepository extends JpaRepository<Political, Long>{
	List<Political> findByDistrict(District district);
	List<Political> findByDistrictAndEventOrderBySerialNoDesc(District district, MasterEvent event);
	long countBydistrict(District district);
	long countBydistrictAndEvent(District district, MasterEvent event);
	@Query("select Max(c.serialNo) from Political c where c.district= :district and  c.event=:masterEvent")
	Long findMaxSerial(@Param ("district") District district,@Param("masterEvent") MasterEvent event);
	
	
	String a = "select c from Political c where (:district is null or c.district = :district) and (c.event=:masterEvent) and (";
	String a1="select c from Political c where c.district= :district and(";
	String a2 = "lower(c.party.partyName) like lower(concat('%', :searchTerm, '%')) ";
	String a3 = "or lower(c.cell.cellName) like lower(concat('%', :searchTerm, '%')) ";
	String a4 = "or lower(c.candidate.candidateName) like lower(concat('%', :searchTerm, '%')) ";
	String a5 = "or lower(c.constituency.constituencyName) like lower(concat('%', :searchTerm, '%')) ";
	String a6 = "or lower(c.firstName) like lower(concat('%', :searchTerm, '%')) ";
	String a7 = "or lower(c.lastName) like lower(concat('%', :searchTerm, '%')))";
	
	@Query(a + a2+ a3+ a4 + a5 +a6 +a7)
	List<Political> search(@Param("searchTerm") String searchTerm, @Param("district") District district, @Param("masterEvent") MasterEvent event);
	
	
	//@Query(a1+a2+a4+a5)
	//List<Political> search(@Param("searchTerm") String searchTerm, @Param("district") District district);
	
	@Query("Select a from Political a where a.serialNo>= :from and a.serialNo <= :to and a.district=:district and a.districtmaster=:districtmaster  and  a.event=:masterEvent order by a.serialNo ASC")
	List<Political> getRangeQuery(@Param("from") long from, @Param("to") long to, @Param("district") District district, @Param ("districtmaster") Districtmaster districtmaster, @Param("masterEvent") MasterEvent event);
	
	@Query("Select a from Political a where a.enteredOn >= :from and a.enteredOn <=:to and a.district=:district and a.districtmaster=:districtmaster and  a.event=:masterEvent order by a.serialNo ASC")
	List<Political> getDatesQuery(@Param("from") LocalDate from, @Param("to") LocalDate to, @Param("district") District district, @Param ("districtmaster") Districtmaster districtmaster, @Param("masterEvent") MasterEvent event);

	@Query("Select a from Political a where a.party=:party and a.district=:district and a.districtmaster=:districtmaster and  a.event=:masterEvent order by a.serialNo ASC")
	List<Political> getPartyQuery(@Param("party") Party party, @Param("district") District district, @Param ("districtmaster") Districtmaster districtmaster, @Param("masterEvent") MasterEvent event);
	
	@Query("Select a from Political a where a.constituency=:constituency and a.district=:district and a.districtmaster=:districtmaster and  a.event=:masterEvent order by a.serialNo ASC")
	List<Political> getConstiQuery(@Param("constituency") Constituency constituency, @Param("district") District district, @Param ("districtmaster") Districtmaster districtmaster, @Param("masterEvent") MasterEvent event);
	
	@Query("Select a from Political a where a.candidate=:candidate and a.district=:district and a.districtmaster=:districtmaster and  a.event=:masterEvent order by a.serialNo ASC")
	List<Political> getCandiQuery(@Param("candidate") Candidate candidate, @Param("district") District district, @Param ("districtmaster") Districtmaster districtmaster, @Param("masterEvent") MasterEvent event);
}
