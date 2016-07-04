package com.jifenke.lepluslive.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Authenticate a user from the database.
 */
public interface LeJiaUserDetailsService
    extends org.springframework.security.core.userdetails.UserDetailsService {

    public UserDetails loadUserByUsername(String userRole, final String login);
}
