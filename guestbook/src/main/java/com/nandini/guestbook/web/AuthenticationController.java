package com.nandini.guestbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.nandini.guestbook.security.AuthRequest;
import com.nandini.guestbook.security.service.JwtService;
import com.nandini.guestbook.security.service.LoginUserDetailsService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private LoginUserDetailsService userDetailsService;

	@Autowired
	private JwtService jwtService;

	@PostMapping(value = "/authenticate")
	public String createAuthenticationToken(Model model,
             @ModelAttribute("login") AuthRequest authenticationRequest)
			throws Exception {
		
		try {
			log.info("From AuthenticationController");
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch(DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		}
		catch(BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
		UserDetails userdetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		String token = jwtService.generateToken(userdetails);
		model.addAttribute("token",token);
		model.addAttribute("user", authenticationRequest.getUsername());
		if(userdetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return "admin";
		}else {
			return "guest";
		}
		
	}

}
