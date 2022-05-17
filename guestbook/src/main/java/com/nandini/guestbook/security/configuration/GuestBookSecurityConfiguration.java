package com.nandini.guestbook.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nandini.guestbook.security.AuthFilter;
import com.nandini.guestbook.security.service.LoginUserDetailsService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class GuestBookSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	LoginUserDetailsService userDetailsService;

	@Autowired
	private AuthFilter authFilter;

	@Autowired
	private LoginAuthEntryPoint unauthorizedHandler;

	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		log.info("From GuestBookSecurityConfiguration");
		http.csrf().disable().authorizeRequests()
				.antMatchers("/authenticate", "/", "/*.css", "/*.js","/*.ico","/background.jpg","/approve.png","/reject.png","/edit.png", "/*.map", "/h2-console/*").permitAll()
				.antMatchers("**/admin/**").hasAuthority("ROLE_ADMIN").antMatchers("**/guest/**")
				.hasAuthority("ROLE_GUEST").anyRequest().authenticated().and().exceptionHandling()
				.authenticationEntryPoint(unauthorizedHandler).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

	}

}
