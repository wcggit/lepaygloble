package com.jifenke.lepluslive.security;

import com.jifenke.lepluslive.global.config.Constants;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantUserService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Inject
    private MerchantService merchantService;

    @Override
    public String getCurrentAuditor() {
        MerchantUser user = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        String userName = user.getName();
        return (userName != null ? userName : Constants.SYSTEM_ACCOUNT);
    }
}
