package com.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.identity.entity.District;
import com.identity.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
	//Users findByUserName(String username);
	Users findByUserNameAndEnabled(String username, boolean enabled);
	
	List<Users> findByDistrict(District district);
	List<Users> findAll();
	
	@Query("select Max(c.userId) from Users c ")
	Long findMaxSerial();
}
