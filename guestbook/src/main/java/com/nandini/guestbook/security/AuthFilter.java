package com.nandini.guestbook.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nandini.guestbook.security.service.JwtService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthFilter extends OncePerRequestFilter {
	@Autowired
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
			String jwtToken = extractJwtFromRequest(request);
			log.info("From AuthFilter");
			try {
			if(StringUtils.hasText(jwtToken) && jwtService.validateToken(jwtToken)) {
				UserDetails userDetails = new User(jwtService.getUsernameFromToken(jwtToken), "",
						jwtService.getRolesFromToken(jwtToken));

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			} else {
				log.info("Cannot set the Security Context");
			}
			}
			catch(BadCredentialsException e) {
				byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", e.getMessage()));

				response.getOutputStream().write(body);
				return;
			}catch(Exception e) {
				byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", e.getMessage()));

				response.getOutputStream().write(body);
				return;
			}
		chain.doFilter(request, response);
	}

	private String extractJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
}
