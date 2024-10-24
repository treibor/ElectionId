package com.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.identity.entity.District;
import com.identity.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
	//Users findByUserName(String username);
	Users findByUserNameAndEnabled(String username, boolean enabled);
	
	@Query("select Max(c.userId) from Users c ")
	Long findMaxSerial();
}
