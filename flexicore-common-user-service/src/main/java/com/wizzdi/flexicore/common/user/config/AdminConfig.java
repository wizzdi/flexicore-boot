package com.wizzdi.flexicore.common.user.config;

import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.common.user.request.CommonUserCreate;
import com.wizzdi.flexicore.common.user.service.CommonUserService;
import com.wizzdi.flexicore.security.interfaces.DefaultUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

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
  public DefaultUserProvider<SecurityUser> defaultSecurityUserProvider() {
    return securityUserCreate ->
            commonUserService.createUserPlain(
                    new CommonUserCreate(securityUserCreate)
                            .setEmail(username)
                            .setPassword(password),
                        null);
  }
}
