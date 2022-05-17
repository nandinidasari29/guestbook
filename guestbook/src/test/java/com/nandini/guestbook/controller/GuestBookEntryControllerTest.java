package com.nandini.guestbook.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.nandini.guestbook.service.GuestEntryService;

@AutoConfigureMockMvc
@SpringBootTest
class GuestBookEntryControllerTest {

	@Autowired
    MockMvc mockMvc;
    
    @MockBean
    GuestEntryService guestEntryService;
    
	@Test
    @WithUserDetails(value="guest")
	void testCreateNewEntry() throws Exception {
		String content = "Test Entry";
		MockMultipartFile file = new MockMultipartFile("imageEntry", "testfile.txt", MediaType.IMAGE_JPEG_VALUE, content.getBytes());
        
		mockMvc.perform(MockMvcRequestBuilders
                .multipart("/guest/create")
                .file(file)).andExpect(status().isOk());
	}
	
	@Test
    @WithUserDetails(value="guest")
	void testCreateNewEntry_failure() throws Exception {
		String content = "Test Entry";
		MockMultipartFile file = new MockMultipartFile("imageEntry", "testfile.txt", MediaType.IMAGE_JPEG_VALUE, content.getBytes());

		Mockito.doThrow(new RuntimeException()).when(guestEntryService).createEntry(null, file);
		mockMvc.perform(MockMvcRequestBuilders.multipart("/guest/create").file(file)).andExpect(status().isBadRequest());
		
	}

}
