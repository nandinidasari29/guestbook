package com.nandini.guestbook.entity;

import java.sql.Blob;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "GUEST_ENTRY")
public class GuestEntry {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;    
    @Column(name="text_entry")
    private String textEntry;
    @Column(name="image_entry")
	@Lob
	@JsonIgnore
	private Blob content;
    @Column(name="file_name")
    private String fileName;
    @NotEmpty
    @Column(name="created_by")
    private String createdBy;
    @Column(name="created_datetime")
    private Timestamp createdDate;
    @Column(name="updated_datetime")
    private Timestamp updatedDate;
	@Column(name="updated_by")
    private String updatedBy;
	@Column(name="status")
    private String status;
	
	public Integer getId() {
		return id;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public String getTextEntry() {
		return textEntry;
	}
	public Blob getContent() {
		return content;
	}
	public String getFileName() {
		return fileName;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public String getStatus() {
		return status;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public void setTextEntry(String textEntry) {
		this.textEntry = textEntry;
	}
	public void setContent(Blob content) {
		this.content = content;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public GuestEntry() {
		
	}
	public GuestEntry(Integer id, String textEntry, Blob content, String fileName, @NotEmpty String createdBy,
			Timestamp createdDate, Timestamp updatedDate, String updatedBy, String status) {
		super();
		this.id = id;
		this.textEntry = textEntry;
		this.content = content;
		this.fileName = fileName;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedBy;
		this.status = status;
	}

}
