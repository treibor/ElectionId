package com.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.identity.entity.Candidate;
import com.identity.entity.Cell;
import com.identity.entity.Constituency;
import com.identity.entity.District;
import com.identity.entity.Employee;
import com.identity.entity.Party;

public interface CandidateRepository extends JpaRepository<Candidate, Long>{
	List<Candidate> findByDistrict(District district);
	Candidate findByCandidateName(String name); 
	
	
	@Query("select  c  from Candidate c where c.party=:party and c.constituency=:consti")
	Candidate  getCandidateComboBox(@Param("party") Party party, @Param("consti") Constituency consti);
	
	
	
	
}
