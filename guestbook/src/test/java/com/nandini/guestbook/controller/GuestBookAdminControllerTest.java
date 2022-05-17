package com.nandini.guestbook.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nandini.guestbook.entity.GuestEntry;
import com.nandini.guestbook.service.AdminService;

@AutoConfigureMockMvc
@SpringBootTest
class GuestBookAdminControllerTest {
	GuestEntry guestBookEntry1 = new GuestEntry(1, "First Entry", null, null, "guest",
			new Timestamp(System.currentTimeMillis()), null, null, null);
	GuestEntry guestBookEntry2 = new GuestEntry(2, "Second Entry", null, null, "guest",
			new Timestamp(System.currentTimeMillis()), null, null, null);

	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	@MockBean
	AdminService adminService;

	@Test
	@WithUserDetails(value = "admin")
	void testFindEntryById() throws Exception {
		Mockito.when(adminService.findEntryById(guestBookEntry1.getId())).thenReturn(guestBookEntry1);

		mockMvc.perform(MockMvcRequestBuilders.get("/admin/find/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	@WithUserDetails(value = "admin")
	void testFindEntryById_noresult() throws Exception {
		Mockito.when(adminService.findEntryById(5)).thenThrow(new NoResultException(""));

		mockMvc.perform(MockMvcRequestBuilders.get("/admin/find/5").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithUserDetails(value = "admin")
	void testFindEntryById_servererror() throws Exception {
		Mockito.when(adminService.findEntryById(5)).thenThrow(new RuntimeException());

		mockMvc.perform(MockMvcRequestBuilders.get("/admin/find/5").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

	@Test
	@WithUserDetails(value = "admin")
	void testGetAllEntries() throws Exception {
		List<GuestEntry> records = new ArrayList<>(Arrays.asList(guestBookEntry1, guestBookEntry2));

		Mockito.when(adminService.findAllEntries()).thenReturn(records);

		mockMvc.perform(MockMvcRequestBuilders.get("/admin/listall").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	@WithUserDetails(value = "admin")
	void testGetAllEntries_servererror() throws Exception {

		Mockito.when(adminService.findAllEntries()).thenThrow(new RuntimeException());

		mockMvc.perform(MockMvcRequestBuilders.get("/admin/listall").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

	@Test
	@WithUserDetails(value = "admin")
	void testUpdateEntry() throws Exception {
		String content = "Test Entry";
		MockMultipartFile file = new MockMultipartFile("imageEntry", "testfile.txt", MediaType.IMAGE_JPEG_VALUE,
				content.getBytes());

		mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/update/1").file(file)).andExpect(status().isOk());
	}

	@Test
	@WithUserDetails(value = "admin")
	void testUpdateEntry_nocontent() throws Exception {
		String content = "Test Entry";
		MockMultipartFile file = new MockMultipartFile("imageEntry", "testfile.txt", MediaType.IMAGE_JPEG_VALUE,
				content.getBytes());

		Mockito.doThrow(new NoResultException("")).when(adminService).updateEntry(null, file, 5);
		mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/update/5").file(file))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithUserDetails(value = "admin")
	void testUpdateEntry_servererror() throws Exception {
		String content = "Test Entry";
		MockMultipartFile file = new MockMultipartFile("imageEntry", "testfile.txt", MediaType.IMAGE_JPEG_VALUE,
				content.getBytes());

		Mockito.doThrow(new RuntimeException()).when(adminService).updateEntry(null, file, 5);
		mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/update/5").file(file))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithUserDetails(value = "admin")
	void testDeleteEntryById() throws Exception { 
		Mockito.when(adminService.findEntryById(guestBookEntry1.getId())).thenReturn((guestBookEntry1));
        
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/admin/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
	}
	
	@Test
	@WithUserDetails(value = "admin")
	void testDeleteEntryById_nocontent() throws Exception {         
        Mockito.doThrow(new NoResultException("")).when(adminService).deleteEntryById(5);
        
    	mockMvc.perform(MockMvcRequestBuilders
                .delete("/admin/delete/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
	}
	
	@Test
	@WithUserDetails(value = "admin")
	void testDeleteEntryById_servererror() throws Exception {         
        Mockito.doThrow(new RuntimeException()).when(adminService).deleteEntryById(5);
        
    	mockMvc.perform(MockMvcRequestBuilders
                .delete("/admin/delete/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
	}

	@Test
	@WithUserDetails(value = "admin")
	void testApproveEntry() throws Exception {
		guestBookEntry1.setStatus("Approved");
    	
        mockMvc.perform(MockMvcRequestBuilders
                .post("/admin/approve/1")
                .contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON)
        		.content(this.mapper.writeValueAsString(guestBookEntry1)))
        		.andExpect(status().isOk());
	}
	
	@Test
	@WithUserDetails(value = "admin")
	void testApproveEntry_nocontent() throws Exception {         
        Mockito.doThrow(new NoResultException("")).when(adminService).approveEntry(5);
        
    	mockMvc.perform(MockMvcRequestBuilders
                .post("/admin/approve/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
	}
	
	@Test
	@WithUserDetails(value = "admin")
	void testApproveEntry_servererror() throws Exception {         
        Mockito.doThrow(new RuntimeException()).when(adminService).approveEntry(5);
        
    	mockMvc.perform(MockMvcRequestBuilders
                .post("/admin/approve/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
	}


	@Test
	@WithUserDetails(value = "admin")
	void testgetImage_noresult() throws Exception {
		Mockito.when(adminService.findEntryById(5)).thenThrow(new NoResultException(""));

		mockMvc.perform(MockMvcRequestBuilders.get("/admin/getimage/5").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithUserDetails(value = "admin")
	void testgetImage_servererror() throws Exception {
		Mockito.when(adminService.findEntryById(5)).thenThrow(new RuntimeException());

		mockMvc.perform(MockMvcRequestBuilders.get("/admin/getimage/5").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}
}
