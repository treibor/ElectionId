package com.identity.security;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.identity.entity.Users;
import com.identity.repository.UsersRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	@Autowired
	private UsersRepository urepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		//System.out.println("Username:"+username);
		Users user= urepo.findByUserName(username);
		//System.out.println("User Object:"+user);
		if(user==null) {
			throw new UsernameNotFoundException(username +"not found.");
		}
		return new CustomUserDetails(user);
	}

}
