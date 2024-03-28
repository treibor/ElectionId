package com.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.identity.entity.District;
import com.identity.entity.Party;

public interface PartyRepository extends JpaRepository<Party, Long>{

	List <Party> findByDistrict(District district);
		
}
