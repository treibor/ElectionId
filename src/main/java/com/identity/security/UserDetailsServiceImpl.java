package com.identity.security;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.identity.entity.Users;
import com.identity.repository.UsersRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final UsersRepository userRepository;
	
	public UserDetailsServiceImpl(UsersRepository userRepository) {
        this.userRepository = userRepository;
        
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	//stem.out.println(loginform.code.getValue());
		//Users user = userRepository.findByUserName(username);
    	Users user = userRepository.findByUserName(username);
		if (user == null) {
			//audit.saveAudit("Login Failure", username);
			throw new UsernameNotFoundException("No user present with username: " + username);
		} else {
			//audit.saveAudit("Login Success", username);
			return new User(user.getUserName(), user.getPassword(), getAuthorities(user));
			
		}

	}
    private static List<GrantedAuthority> getAuthorities(Users user) {
        String role = user.getRole();
        // Create a SimpleGrantedAuthority with the role string
        //SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
        // Return a singleton list containing the authority
        return Collections.singletonList(authority);
    }
    

   

}
