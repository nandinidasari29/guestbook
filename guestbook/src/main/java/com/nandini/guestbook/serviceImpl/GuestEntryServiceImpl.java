package com.nandini.guestbook.serviceImpl;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nandini.guestbook.entity.GuestEntry;
import com.nandini.guestbook.repository.GuestEntryRepository;
import com.nandini.guestbook.service.GuestEntryService;
import com.nandini.guestbook.utils.GuestBookConstants;

@Service("GuestEntryService")
public class GuestEntryServiceImpl implements GuestEntryService{
	
	@Autowired
    private GuestEntryRepository guestEntryRepository;

	public void createEntry(String textEntry, MultipartFile file) throws SerialException, SQLException, IOException {
		GuestEntry entry = new GuestEntry();
		entry.setTextEntry(textEntry);
    	entry.setFileName(file.getOriginalFilename());
    	byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        entry.setContent(blob);
        entry.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        entry.setStatus(GuestBookConstants.PENDING);
        entry.setCreatedBy(((org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        guestEntryRepository.save(entry);		
	}

}
