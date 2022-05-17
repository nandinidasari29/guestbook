package com.nandini.guestbook.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.web.multipart.MultipartFile;

public interface GuestEntryService {

	void createEntry(String textEntry, MultipartFile file)  throws SerialException, SQLException, IOException;

}
