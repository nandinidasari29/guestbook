package com.nandini.guestbook.controller;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nandini.guestbook.entity.GuestEntry;
import com.nandini.guestbook.service.AdminService;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/admin")
@RestController
@Slf4j
public class GuestBookAdminController {

	@Autowired
	AdminService adminService;

	@GetMapping("/find/{id}")
	public ResponseEntity<Object> findEntryById(@PathVariable("id") Integer id) {
		try {
			GuestEntry entry = adminService.findEntryById(id);
			return ResponseEntity.status(HttpStatus.OK).body(entry);
		} catch (NoResultException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/listall")
	public ResponseEntity<Object> getAllEntries() {
		try {
			List<GuestEntry> list = adminService.findAllEntries();
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

	}

	@PostMapping(value = "/update/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<String> updateEntry(@RequestPart(name = "textEntry", required = false) String textEntry,
			@RequestPart(name = "imageEntry", required = false) MultipartFile file, @PathVariable Integer id) {
		try {	
			adminService.updateEntry(textEntry,file,id);
			return ResponseEntity.status(HttpStatus.OK).body("Updated Successfully");

		} catch (NoResultException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteEntryById(@PathVariable("id") Integer id) {

		try {
			adminService.deleteEntryById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Deleted Sucessfully");
		} catch (NoResultException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/approve/{id}")
	public ResponseEntity<String> approveEntry(@PathVariable Integer id) {
		try {
			adminService.approveEntry(id);
			return ResponseEntity.status(HttpStatus.OK).body("Entry is Approved");
		} catch (NoResultException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping(value = "/getimage/{id}", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE })
	public ResponseEntity<Object> getImage(@PathVariable("id") Integer id) {
		try {
			GuestEntry entry = adminService.findEntryById(id);
			byte[] content = entry.getContent().getBytes(1L, (int) entry.getContent().length());
			return ResponseEntity.ok().contentLength(content.length)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + entry.getFileName())
					.body(content);
		} catch (NoResultException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
