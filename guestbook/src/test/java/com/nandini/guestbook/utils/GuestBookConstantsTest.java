package com.nandini.guestbook.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GuestBookConstantsTest {

	@Test
	void test() {
		new GuestBookConstants();
		assertEquals(GuestBookConstants.APPROVED,GuestBookConstants.APPROVED);
		assertEquals(GuestBookConstants.PENDING,GuestBookConstants.PENDING);
		assertEquals(GuestBookConstants.GUESTBOOK_TEMPLATE,GuestBookConstants.GUESTBOOK_TEMPLATE);
	}

}
