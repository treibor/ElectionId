package com.identity.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.identity.entity.Cell;
import com.identity.entity.District;
import com.identity.entity.Districtmaster;
import com.identity.entity.Employee;
import com.identity.entity.Office;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	// List<Employee> findAll();
	long countBydistrict(District district);
	List<Employee> findBylastName(String lname);
	List<Employee> findBydistrict(District district);
	Employee findByeid(long eid);
	List<Employee> findBySerialNoBetweenAndDistrict(long fromSerial, long toSerial, District district);
	// Employee findByeidoid();
	String a1="select c from Employee c where c.district= :district and(";
	String a2="lower(c.firstName) like lower(concat('%', :searchTerm, '%')) ";
	String a3="or lower(c.lastName) like lower(concat('%', :searchTerm, '%')))";
	@Query(a1+a2+a3)
	List<Employee> search(@Param("searchTerm") String searchTerm, @Param("district") District district);
	
	@Query("select Max(c.serialNo) from Employee c where c.district= :district")
	Long findMaxSerial(@Param ("district") District district);
	
/*
	
*/	
	@Query("select  c, d, e,f,g  from Employee c join c.office d join c.cell e  join c.district f join f.state g join c.districtmaster h where c.office=:office and c.district=:district and c.districtmaster=:districtmaster order by c.serialNo ASC")
	List<Employee> getreportQueryOffice(@Param("office") Office office, @Param("district") District district,  @Param ("districtmaster") Districtmaster districtmaster);
	
	@Query("select  c, d, e,f,g  from Employee c join c.office d join c.cell e  join c.district f join f.state g join c.districtmaster h where c.cell=:cell and c.district=:district and c.districtmaster=:districtmaster order by c.serialNo ASC")
	List<Employee> getreportQueryCell(@Param("cell") Cell cell, @Param("district") District district,  @Param ("districtmaster") Districtmaster districtmaster);
	
	
	@Query("select  c, d, e,f,g,h  from Employee c join c.office d join c.cell e  join c.district f join f.state g join c.districtmaster h where c.serialNo>= :from and c.serialNo<= :to and c.district= :district and c.districtmaster=:districtmaster order by c.serialNo ASC")
	List<Employee> getreportQueryRange(@Param ("district") District district, @Param ("from") long from,  @Param ("to") long to,  @Param ("districtmaster") Districtmaster districtmaster);
	
	
	@Query("select  c, d, e,f,g  from Employee c join c.office d join c.cell e  join c.district f join f.state g join c.districtmaster h where c.enteredOn >= :from and c.enteredOn<=:to and c.district=:district and c.districtmaster=:districtmaster order by c.serialNo ASC")
	List<Employee> getreportQueryDates(@Param ("district") District district, @Param ("from") LocalDate fromRange, @Param ("to") LocalDate toRange,  @Param ("districtmaster") Districtmaster districtmaster);
	//List<Employee> getreportQueryRange(Districtmaster districtMaster, long fromSerial, long toSerial);
}
