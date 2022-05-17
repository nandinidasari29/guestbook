package com.nandini.guestbook.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.springframework.web.multipart.MultipartFile;

import com.nandini.guestbook.entity.GuestEntry;

public interface AdminService {

	List<GuestEntry> findAllEntries();

	GuestEntry findEntryById(Integer id);

	void deleteEntryById(Integer id);

	void updateEntry(String textEntry,MultipartFile file,Integer id)  throws IOException, SerialException, SQLException;

	void approveEntry(Integer id);

}
