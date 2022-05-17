package com.nandini.guestbook.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.nandini.guestbook.security.AuthRequest;

@AutoConfigureMockMvc
@SpringBootTest
class AuthenticationControllerTest {
	
	@Autowired
    MockMvc mockMvc;

	@Test
    public void authenticateTest() throws Exception {
    	AuthRequest authenticationRequest = new AuthRequest();
    	authenticationRequest.setUsername("guest");
    	authenticationRequest.setPassword("guest");
    	
        mockMvc.perform(MockMvcRequestBuilders
                .post("/authenticate")
                .param("username", authenticationRequest.getUsername())
                .param("password", authenticationRequest.getPassword())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());
    }
	

}
