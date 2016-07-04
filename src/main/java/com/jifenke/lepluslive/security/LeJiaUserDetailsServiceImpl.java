package com.jifenke.lepluslive.security;

import com.jifenke.lepluslive.jhipster.domain.entities.User;
import com.jifenke.lepluslive.jhipster.repository.UserRepository;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Created by wcg on 16/6/26.
 */
@Component("userDetailsService")
public class LeJiaUserDetailsServiceImpl implements LeJiaUserDetailsService {

    private final Logger log = LoggerFactory.getLogger(LeJiaUserDetailsService.class);

    @Inject
    private MerchantUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {
        throw new RuntimeException(
            new IllegalAccessException("Should call loadUserByOrganizationNameAndUsername"));
    }

    @Transactional
    public UserDetails loadUserByUsername(String userRole, final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();
        Optional<MerchantUser> userFromDatabase = userRepository.findByName(lowercaseLogin);
        return userFromDatabase.map(user -> {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
            grantedAuthorities.add(new SimpleGrantedAuthority(userRole));
            return new org.springframework.security.core.userdetails.User(lowercaseLogin,
                                                                          user.getPassword(),
                                                                          grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException(
            "User " + lowercaseLogin + " was not found in the " +
            "database"));
    }
}
