package com.flexicore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flexicore.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


public class EntityCreationTest {

	public static final String TEST_ROLE = "testRole";
	public static final String TEST_USER = "testUser";
	public static final String TEST_ROLE_TO_USER = "testRoleToUser";
	private ObjectMapper objectMapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false).registerModule(new JavaTimeModule());


	@Test
	public void testBasicListSerialization() throws JsonProcessingException {
		Role testRole = new Role().setId(TEST_ROLE);
		SecurityUser testUser = new SecurityUser().setId(TEST_USER);
		RoleToUser roleToUser=new RoleToUser().setId(TEST_ROLE_TO_USER);
		roleToUser.setRole(testRole);
		roleToUser.setUser(testUser);
		List<Basic> list= Arrays.asList(testRole, testUser,roleToUser);
		String s = objectMapper.writeValueAsString(list);
		TypeReference<List<Basic>> typeReference= new TypeReference<>() {};
		List<Basic> reRead=objectMapper.readValue(s,typeReference);
		reRead.stream().filter(f->f.getId().equals(TEST_ROLE)&&f.getClass().equals(testRole.getClass())).findAny().orElseThrow(()->new RuntimeException(TEST_ROLE+" was not properly parsed"));
		reRead.stream().filter(f->f.getId().equals(TEST_USER)&&f.getClass().equals(testUser.getClass())).findAny().orElseThrow(()->new RuntimeException(TEST_USER+" was not properly parsed"));
		reRead.stream().filter(f->f.getId().equals(TEST_ROLE_TO_USER)&&f.getClass().equals(roleToUser.getClass())).findAny().orElseThrow(()->new RuntimeException(TEST_ROLE_TO_USER+" was not properly parsed"));


	}

	@Test
	public void testBasicSerialization() throws JsonProcessingException {
		Role testRole = new Role().setId("testRole");

		String s = objectMapper.writeValueAsString(testRole);
		Basic reRead=objectMapper.readValue(s,Basic.class);
		Assertions.assertEquals(reRead.getClass(), Role.class);

	}

}
