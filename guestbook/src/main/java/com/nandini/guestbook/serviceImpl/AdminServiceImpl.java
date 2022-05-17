package com.nandini.guestbook.serviceImpl;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nandini.guestbook.entity.GuestEntry;
import com.nandini.guestbook.repository.GuestEntryRepository;
import com.nandini.guestbook.service.AdminService;
import com.nandini.guestbook.utils.GuestBookConstants;

@Service("AdminService")
public class AdminServiceImpl implements AdminService{
	
	@Autowired
    private GuestEntryRepository guestEntryRepository;
	
	public List<GuestEntry> findAllEntries() {
		return guestEntryRepository.findAll();
	}

	public GuestEntry findEntryById(Integer id) {
		Optional<GuestEntry> entry =  guestEntryRepository.findGuestEntryById(id);
    	if(entry.isPresent()) {
	    		return entry.get();
	    }
    	throw new NoResultException("No entry found");
	}

	public void deleteEntryById(Integer id) {
		Optional<GuestEntry> entry =  guestEntryRepository.findGuestEntryById(id);
    	if(entry.isPresent()) {
    		guestEntryRepository.deleteGuestEntryById(id);
    		return;
	    }
    	throw new NoResultException("No entry found");
		
	}

	public void updateEntry(String textEntry,MultipartFile file,Integer id) throws IOException, SerialException, SQLException {
		Optional<GuestEntry> entryById = guestEntryRepository.findById(id);
		if(entryById.isPresent()) {
			GuestEntry entry = entryById.get();
			entry.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
			entry.setUpdatedBy(((org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
	        
			if (StringUtils.hasText(textEntry)) {
				entry.setTextEntry(textEntry);
				entry.setContent(null);
			} else {
				entry.setTextEntry(null);
				entry.setFileName(file.getOriginalFilename());
				byte[] bytes = file.getBytes();
				Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
				entry.setContent(blob);
			}
			guestEntryRepository.save(entry);
		}else {
			throw new NoResultException("No guest entry found");
		}
		
		
	}

	public void approveEntry(Integer id) {
		Optional<GuestEntry> entryById = guestEntryRepository.findGuestEntryById(id);
    	if(entryById.isPresent()) {
    		GuestEntry entry = entryById.get();
    		entry.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
    		entry.setStatus(GuestBookConstants.APPROVED);
        	guestEntryRepository.save(entry);
        	return;
    	}
    	throw new NoResultException("No entry found");
		
	}

}
