package com.smoke.xiguazi.security;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MobileAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final String SPRING_SECURITY_FORM_PHONE_KEY = "phone";
    private final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    private boolean postOnly = true;

    protected MobileAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("*Authentication method not supported: " + request.getMethod());
        }

        String phone = obtainPhone(request);
        String password = obtainPassword(request);

        if (phone == null) {
            phone = "";
        }

        if (password == null) {
            password = "";
        }

        phone = phone.trim();

        MobileAuthenticationToken authRequest = new MobileAuthenticationToken(phone, password);

        setDetails(request, authRequest);

        return getAuthenticationManager().authenticate(authRequest);
    }

    private String obtainPhone(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_PHONE_KEY);
    }

    private String obtainPassword(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
    }

    private void setDetails(HttpServletRequest request, MobileAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
