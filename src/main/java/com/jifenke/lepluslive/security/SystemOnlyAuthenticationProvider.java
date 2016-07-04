package com.jifenke.lepluslive.security;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

/**
 * Created by wcg on 16/6/26.
 */
public class SystemOnlyAuthenticationProvider extends DaoAuthenticationProvider {

    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private String userNotFoundEncodedPassword;

    private UserCache userCache = new LejiaUserCache();
    private boolean forcePrincipalAsString = false;
    protected boolean hideUserNotFoundExceptions = true;
    private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();


    public SystemOnlyAuthenticationProvider(
        UserDetailsService userDetailsService,
        Md5PasswordEncoder passwordEncoder) {
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        Assert.isInstanceOf(LeJiaAuthenticationToken.class, authentication,
                            messages.getMessage(
                                "AbstractUserDetailsAuthenticationProvider.onlySupports",
                                "Only OrganizationUserAuthenticationToken is supported"));

        LeJiaAuthenticationToken token =
            (LeJiaAuthenticationToken) authentication;

        // Determine username
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED"
                                                                  : authentication.getName();

        boolean cacheWasUsed = true;
        UserDetails user = this.userCache.getUserFromCache(username);

        if (user == null) {
            cacheWasUsed = false;

            try {
                user = retrieveLeJiaUser(token.getUserRole(), username,
                                         (UsernamePasswordAuthenticationToken) authentication);
            } catch (UsernameNotFoundException notFound) {
                logger.warn(
                    "User '" + username + "' in organization '" + token.getUserRole()
                    + "' not found");

                if (hideUserNotFoundExceptions) {
                    throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials",
                        "Bad credentials"));
                } else {
                    throw notFound;
                }
            }

            Assert.notNull(user,
                           "retrieveOrganizationUser returned null - a violation of the interface contract");
        }

        try {
            preAuthenticationChecks.check(user);
            additionalAuthenticationChecks(user,
                                           (UsernamePasswordAuthenticationToken) authentication);
        } catch (AuthenticationException exception) {
            if (cacheWasUsed) {
                // There was a problem, so try again after checking
                // we're using latest data (i.e. not from the cache)
                cacheWasUsed = false;
                user = retrieveLeJiaUser(token.getUserRole(), username,
                                         (UsernamePasswordAuthenticationToken) authentication);
                preAuthenticationChecks.check(user);
                additionalAuthenticationChecks(user,
                                               (UsernamePasswordAuthenticationToken) authentication);
            } else {
                throw exception;
            }
        }

        if (!cacheWasUsed) {
            this.userCache.putUserInCache(user);
        }

        Object principalToReturn = user;

        if (forcePrincipalAsString) {
            principalToReturn = user.getUsername();
        }

        return createSuccessAuthentication(principalToReturn, authentication, user);
    }

    private UserDetails retrieveLeJiaUser(String userRole, String username,
                                          UsernamePasswordAuthenticationToken authentication) {
        UserDetails loadedUser;

        LeJiaUserDetailsService userDetailsService =
            (LeJiaUserDetailsService) this.getUserDetailsService();
        try {
            loadedUser = userDetailsService
                .loadUserByUsername(userRole, username);
        } catch (UsernameNotFoundException notFound) {
            if (authentication.getCredentials() != null) {
                String presentedPassword = authentication.getCredentials().toString();
                getPasswordEncoder().isPasswordValid(userNotFoundEncodedPassword,
                                                     presentedPassword, null);
            }
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(
                repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (LeJiaAuthenticationToken.class
                    .isAssignableFrom(authentication));
    }

    @Override
    public void setUserCache(UserCache userCache) {
        Assert.isInstanceOf(LeJiaAuthenticationToken.class, userCache,
                            messages.getMessage(
                                "AbstractUserDetailsAuthenticationProvider.onlySupports",
                                "Only OrganizationUserCache is supported"));
        super.setUserCache(userCache);
    }

    @Override
    public void setPasswordEncoder(Object passwordEncoder) {
        super.setPasswordEncoder(passwordEncoder);

        this.userNotFoundEncodedPassword = getPasswordEncoder().encodePassword(
            USER_NOT_FOUND_PASSWORD, null);
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {

        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                logger.warn("User account is locked");

                throw new LockedException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.locked",
                    "User account is locked"));
            }

            if (!user.isEnabled()) {
                logger.warn("User account is disabled");

                throw new DisabledException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.disabled",
                    "User is disabled"));
            }

            if (!user.isAccountNonExpired()) {
                logger.warn("User account is expired");

                throw new AccountExpiredException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.expired",
                    "User account has expired"));
            }
        }
    }

    private class LejiaUserCache implements UserCache {

        @Override
        public UserDetails getUserFromCache(String username) {
            return null;
        }

        @Override
        public void putUserInCache(UserDetails user) {
        }

        @Override
        public void removeUserFromCache(String username) {
        }
    }

}
