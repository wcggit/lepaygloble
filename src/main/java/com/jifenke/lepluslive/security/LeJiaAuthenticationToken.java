package com.jifenke.lepluslive.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by wcg on 16/6/25.
 */
public class LeJiaAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String userRole;

    public LeJiaAuthenticationToken(String userRole,Object principal, Object credentials) {
        super(principal, credentials);
        this.userRole = userRole;
    }

    public LeJiaAuthenticationToken(Object principal, Object credentials,
                                    Collection<? extends GrantedAuthority> authorities,
                                    String userRole) {
        super(principal, credentials, authorities);
        this.userRole = userRole;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
