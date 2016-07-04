package com.jifenke.lepluslive.security;

import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

/**
 * Created by edison on 7/17/15.
 */
public class LeJiaSecurityContextRepositoryImpl extends HttpSessionSecurityContextRepository
    implements LeJiaSecurityContextRepository {

    private String springSecurityContextKey = SPRING_SECURITY_CONTEXT_KEY;

    @Override public String getSpringSecurityContextKey() {
        return springSecurityContextKey;
    }

    @Override public void setSpringSecurityContextKey(String springSecurityContextKey) {
        super.setSpringSecurityContextKey(springSecurityContextKey);
        this.springSecurityContextKey = springSecurityContextKey;
    }
}
