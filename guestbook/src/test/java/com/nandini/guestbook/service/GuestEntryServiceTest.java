package com.nandini.guestbook.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.nandini.guestbook.entity.GuestEntry;
import com.nandini.guestbook.repository.GuestEntryRepository;
import com.nandini.guestbook.serviceImpl.GuestEntryServiceImpl;

@ExtendWith(MockitoExtension.class)
class GuestEntryServiceTest {

	GuestEntry guestBookEntry1 = new GuestEntry(1, "First Entry", null, null, "guest",
			new Timestamp(System.currentTimeMillis()), null, null, null);
	
	@InjectMocks 
	GuestEntryService guestEntryService = new GuestEntryServiceImpl();
		
	@Mock
	GuestEntryRepository guestEntryRepository;
	
	@Test
	void testCreateEntry() throws Exception {
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	    Authentication authentication = Mockito.mock(Authentication.class);
	    User user1 = Mockito.mock(User.class);
	    Mockito.when(authentication.getPrincipal()).thenReturn(user1);
	    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
	    SecurityContextHolder.setContext(securityContext);
		
		when(guestEntryRepository.save(any(GuestEntry.class))).thenReturn(guestBookEntry1);

		String content = "Test Entry";
		MockMultipartFile file = new MockMultipartFile("imageEntry", "testfile.txt", MediaType.IMAGE_JPEG_VALUE,
				content.getBytes());
		guestEntryService.createEntry(content,file);
		verify(guestEntryRepository, times(1)).save(Mockito.any(GuestEntry.class));

	}

}
