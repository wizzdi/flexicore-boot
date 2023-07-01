package com.wizzdi.security.bearer.jwt.testUser;

import com.wizzdi.flexicore.security.request.SecurityUserFilter;

import java.util.Set;

public class TestUserFilter extends SecurityUserFilter {

    private Set<String> usernames;

    public Set<String> getUsernames() {
        return usernames;
    }

    public <T extends TestUserFilter> T setUsernames(Set<String> usernames) {
        this.usernames = usernames;
        return (T) this;
    }
}
