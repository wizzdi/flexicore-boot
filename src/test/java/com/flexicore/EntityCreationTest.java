package com.flexicore;

import com.flexicore.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class EntityCreationTest {


	@Test
	public void testRoleCreation() {
		Assertions.assertNotNull(new Role("test", null).getId());

	}

}
