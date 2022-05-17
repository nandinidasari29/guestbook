package com.nandini.guestbook.security.service;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nandini.guestbook.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("from LoginUserDetailsService");
		List<SimpleGrantedAuthority> roles = null;
		Optional<com.nandini.guestbook.entity.User> optional =  userRepository.findUserByUsername(username);
		if(optional.isPresent()) {
			com.nandini.guestbook.entity.User user = optional.get();
			roles=Arrays.asList(new SimpleGrantedAuthority(user.getRole()));
			return new User(user.getUsername(),user.getPassword(),roles);
		}
		throw new UsernameNotFoundException("User not found with the name "+ username);
	} 

}
