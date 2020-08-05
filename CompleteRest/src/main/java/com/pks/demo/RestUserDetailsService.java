package com.pks.demo;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pks.demo.exception.ProductNotFoundException;
import com.pks.demo.model.RestUser;
import com.pks.demo.model.RestUserService;
@Service
public class RestUserDetailsService implements UserDetailsService {
	 PasswordEncoder passwordEncoder;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		RestUserService restUserService = new RestUserService();
		RestUser restUser = restUserService.getUser(username);
		if(restUser == null) {
			throw new ProductNotFoundException("No user found with the username: "+username);
		}
		
		return new User(restUser.getUsername(),restUser.getPassword(),new ArrayList<>());
	}
	
	

}
