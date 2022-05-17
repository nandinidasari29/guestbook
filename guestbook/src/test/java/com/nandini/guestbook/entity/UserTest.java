package com.nandini.guestbook.entity;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.google.code.beanmatchers.BeanMatchers;

class UserTest {

	@Test
	void testUser() {
		assertThat(User.class, allOf(BeanMatchers.hasValidBeanConstructor(),BeanMatchers.hasValidGettersAndSetters()));
	}

}
