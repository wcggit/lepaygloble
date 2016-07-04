package com.jifenke.lepluslive.security;

import org.springframework.security.web.context.SecurityContextRepository;

/**
 * Created by edison on 7/17/15.
 */
public interface LeJiaSecurityContextRepository extends SecurityContextRepository {
    String getSpringSecurityContextKey();
}
