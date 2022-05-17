package com.nandini.guestbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nandini.guestbook.service.GuestEntryService;

@RequestMapping("/guest")
@RestController
public class GuestBookEntryController {
	
	@Autowired
	GuestEntryService guestEntryService;
	
	@PostMapping(value="/create", consumes= {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> createNewEntry(@RequestPart(name="textEntry", required=false) String textEntry, @RequestPart(name="imageEntry", required=false) MultipartFile file) {
        try {        	
        	guestEntryService.createEntry(textEntry, file);
        	return ResponseEntity.status(HttpStatus.OK).body("Entry Created");
        }
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
    }
	
	

}
