package com.wizzdi.flexicore.common.user.config;

import com.flexicore.model.User;
import com.wizzdi.flexicore.common.user.request.CommonUserCreate;
import com.wizzdi.flexicore.common.user.request.CommonUserFilter;
import com.wizzdi.flexicore.common.user.service.CommonUserService;
import com.wizzdi.flexicore.security.interfaces.DefaultUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Collections;

@Configuration
public class AdminConfig {

  @Autowired
  @Lazy
  private CommonUserService commonUserService;

  @Value("${admin.username:admin@flexicore.com}")
  private String username;

  @Value("${admin.password:#{T(java.util.UUID).randomUUID().toString()}}")
  private String password;

  @Bean
  public DefaultUserProvider<User>
      defaultSecurityUserProvider() {
    return securityUserCreate ->
            commonUserService
            .listAllUsers(new CommonUserFilter().setEmails(Collections.singleton(username)), null)
            .stream()
            .findFirst()
            .orElseGet(
                () ->
                        commonUserService.createUser(
                        new CommonUserCreate()
                            .setEmail(username)
                            .setPassword(password)
                            .setTenant(securityUserCreate.getTenant())
                            .setIdForCreate(securityUserCreate.getIdForCreate())
                            .setName(securityUserCreate.getName()),
                        null));
  }
}
