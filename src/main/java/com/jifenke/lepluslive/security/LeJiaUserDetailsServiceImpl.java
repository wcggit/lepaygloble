package com.jifenke.lepluslive.security;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.partner.repository.PartnerManagerRepository;
import com.jifenke.lepluslive.partner.repository.PartnerRepository;

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

import javax.inject.Inject;

/**
 * Created by wcg on 16/6/26.
 */
@Component("userDetailsService")
public class LeJiaUserDetailsServiceImpl implements LeJiaUserDetailsService {

    private final Logger log = LoggerFactory.getLogger(LeJiaUserDetailsService.class);

    @Inject
    private MerchantUserRepository userRepository;

    @Inject
    private PartnerRepository partnerRepository;

    @Inject
    private PartnerManagerRepository partnerManagerRepository;

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
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority(userRole));
        if ("merchant".equals(userRole)) {
            Optional<MerchantUser> userFromDatabase = userRepository.findByName(lowercaseLogin);
            return userFromDatabase.map(user -> {
                return new org.springframework.security.core.userdetails.User(user.getMerchantSid(),
                                                                              user.getPassword(),
                                                                              grantedAuthorities);
//                return new org.springframework.security.core.userdetails.User(user.getMerchantSid(),
            }).orElseThrow(() -> new UsernameNotFoundException(
                "User " + lowercaseLogin + " was not found in the " +
                "database"));
        } else if ("partner".equals(userRole)) {
            Optional<Partner> userFromDatabase = partnerRepository.findByName(lowercaseLogin);
            return userFromDatabase.map(user -> {
                return new org.springframework.security.core.userdetails.User(
                    userFromDatabase.get().getPartnerSid(),
                    user.getPassword(),
                    grantedAuthorities);
            }).orElseThrow(() -> new UsernameNotFoundException(
                "User " + lowercaseLogin + " was not found in the " +
                "database"));
        } else {
            Optional<PartnerManager>
                userFromDatabase =
                partnerManagerRepository.findByName(lowercaseLogin);
            return userFromDatabase.map(user -> {
                return new org.springframework.security.core.userdetails.User(
                    userFromDatabase.get().getPartnerManagerSid(),
                    user.getPassword(),
                    grantedAuthorities);
            }).orElseThrow(() -> new UsernameNotFoundException(
                "User " + lowercaseLogin + " was not found in the " +
                "database"));
        }

    }
}
