package com.jifenke.lepluslive.security;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wcg on 16/6/25. 自定义认证授权规则拦截器
 */
public class LeJiaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SPRING_SECURITY_FORM_USER_ROLE_KEY = "userRole";

    private String userRole = SPRING_SECURITY_FORM_USER_ROLE_KEY;

    private boolean postOnly = true;

    @Override public Authentication attemptAuthentication(HttpServletRequest request,
                                                          HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                "Authentication method not supported: " + request.getMethod());
        }

        String userRole = obtainUserRole(request);
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        if (userRole == null) {
            userRole = "";
        }

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        userRole = userRole.trim();
        username = username.trim();

        LeJiaAuthenticationToken authRequest =
            new LeJiaAuthenticationToken(userRole,
                                                    username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainUserRole(HttpServletRequest request) {
        return request.getParameter("userRole");
    }

    @Override public void setPostOnly(boolean postOnly) {
        super.setPostOnly(postOnly);
        this.postOnly = postOnly;
    }

    public void setUserRole(String organizationNameParameter) {
        this.userRole = userRole;
    }




}
