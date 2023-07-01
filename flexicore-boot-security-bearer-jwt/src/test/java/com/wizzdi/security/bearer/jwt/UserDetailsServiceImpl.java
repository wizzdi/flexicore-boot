package com.wizzdi.security.bearer.jwt;

import com.wizzdi.security.adapter.FlexicoreUserDetails;
import com.wizzdi.security.bearer.jwt.testUser.TestUserFilter;
import com.wizzdi.security.bearer.jwt.testUser.TestUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static java.lang.String.format;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final TestUserService testUserService;

    public UserDetailsServiceImpl(TestUserService testUserService) {
        this.testUserService = testUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         return testUserService.listAllTestUsers(new TestUserFilter().setUsernames(Collections.singleton(username)),null)
                .stream().findFirst()
                .map(f->new FlexicoreUserDetails(f.getId(),f.getUsername(),f.getPassword()))
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                format("User: %s, not found", username)
                        )
                );
    }
}
