package com.wizzdi.flexicore.common.user.config;

import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.common.user.request.CommonUserCreate;
import com.wizzdi.flexicore.common.user.service.CommonUserService;
import com.wizzdi.flexicore.security.interfaces.DefaultUserProvider;
import com.wizzdi.flexicore.security.service.DefaultObjectsCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@AutoConfigureBefore(DefaultObjectsCreator.class)
public class AdminConfig {


  @Value("${admin.username:admin@flexicore.com}")
  private String username;

  @Value("${admin.password:#{T(java.util.UUID).randomUUID().toString()}}")
  private String password;

  @Bean
  public DefaultUserProvider<SecurityUser> defaultSecurityUserProvider(@Lazy CommonUserService commonUserService) {
    return securityUserCreate ->
            commonUserService.createUserPlain(
                    new CommonUserCreate(securityUserCreate)
                            .setEmail(username)
                            .setPassword(password),
                        null);
  }
}
