package com.nandini.guestbook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nandini.guestbook.entity.GuestEntry;

@Repository
public interface GuestEntryRepository extends JpaRepository <GuestEntry, Integer> {

	Optional<GuestEntry> findGuestEntryById(Integer id);

	@Transactional
	void deleteGuestEntryById(Integer id);

}
