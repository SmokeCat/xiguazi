package com.smoke.xiguazi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class MobileAuthenticationManager implements AuthenticationManager {

    @Autowired
    MobileAuthenticationProvider mobileAuthenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication result = mobileAuthenticationProvider.authenticate(authentication);
        if(result == null){
            throw new ProviderNotFoundException("*Authentication failed!");
        }
        return result;
    }
}
