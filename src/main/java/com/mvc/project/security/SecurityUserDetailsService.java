package com.mvc.project.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mvc.project.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService{

	private UserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var o = repo.findByEmail(username);
		return o.map(SecurityUserDetails::new)
				.orElseThrow(()-> new UsernameNotFoundException("User doesn't exists"));
	}

}
