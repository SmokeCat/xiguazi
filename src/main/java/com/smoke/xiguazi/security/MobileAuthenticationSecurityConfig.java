package com.smoke.xiguazi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class MobileAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private AuthenticationSuccessHandler successHandler = new MobileAuthenticationSuccessHandler();
    private AuthenticationFailureHandler failureHandler = new MobileAuthenticationFailureHandler();

    @Autowired
    MobileAuthenticationManager mobileAuthenticationManager;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
//        初始化filter
        MobileAuthenticationFilter filter = new MobileAuthenticationFilter();
        filter.setAuthenticationManager(mobileAuthenticationManager);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);

//        初始化provider
        MobileAuthenticationProvider provider = new MobileAuthenticationProvider();

//        添加配置
        builder.authenticationProvider(provider).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
