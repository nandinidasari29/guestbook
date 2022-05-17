package com.nandini.guestbook.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.nandini.guestbook.entity.GuestEntry;
import com.nandini.guestbook.repository.GuestEntryRepository;
import com.nandini.guestbook.serviceImpl.AdminServiceImpl;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

	GuestEntry guestBookEntry1 = new GuestEntry(1, "First Entry", null, null, "guest",
			new Timestamp(System.currentTimeMillis()), null, null, null);
	GuestEntry guestBookEntry2 = new GuestEntry(2, "Second Entry", null, null, "guest",
			new Timestamp(System.currentTimeMillis()), null, null, null);

	@InjectMocks
	AdminService adminService = new AdminServiceImpl();

	@Mock
	GuestEntryRepository guestEntryRepository;

	@Test
	void testFindAllEntries() throws Exception {
		List<GuestEntry> records = new ArrayList<>(Arrays.asList(guestBookEntry1, guestBookEntry2));
		Mockito.when(guestEntryRepository.findAll()).thenReturn(records);
		List<GuestEntry> entries = adminService.findAllEntries();
		assertEquals(entries.size(), records.size());
	}

	@Test
	void testFindEntryById() throws Exception {
		Mockito.when(guestEntryRepository.findGuestEntryById(1)).thenReturn(Optional.ofNullable(guestBookEntry1));
		GuestEntry entry = adminService.findEntryById(1);
		assertNotNull(entry);
	}

	@Test
	void testDeleteEntryById() throws Exception {
		Mockito.when(guestEntryRepository.findGuestEntryById(1)).thenReturn(Optional.ofNullable(guestBookEntry1));
		adminService.deleteEntryById(1);
		verify(guestEntryRepository, times(1)).deleteGuestEntryById(1);
	}

	@Test
	void testUpdateEntry() throws Exception {
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	    Authentication authentication = Mockito.mock(Authentication.class);
	    User user1 = Mockito.mock(User.class);
	    Mockito.when(authentication.getPrincipal()).thenReturn(user1);
	    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
	    SecurityContextHolder.setContext(securityContext);
	    
		Mockito.when(guestEntryRepository.findById(1)).thenReturn(Optional.ofNullable(guestBookEntry1));
		adminService.updateEntry("test",null,1);
		verify(guestEntryRepository, times(1)).save(any(GuestEntry.class));
	}

	@Test
	void testApproveEntry() throws Exception {
		Mockito.when(guestEntryRepository.findGuestEntryById(1)).thenReturn(Optional.ofNullable(guestBookEntry1));
		adminService.approveEntry(1);
		verify(guestEntryRepository, times(1)).save(any(GuestEntry.class));
	}

}
