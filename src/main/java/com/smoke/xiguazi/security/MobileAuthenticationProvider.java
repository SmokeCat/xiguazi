package com.smoke.xiguazi.security;

import com.smoke.xiguazi.model.dto.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MobileAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private MobileUserDetailService mobileUserDetailService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobileAuthenticationToken token = (MobileAuthenticationToken) authentication;

        String phone = (token.getPrincipal() == null) ? "NONE_PHONE"
                : (String) token.getPrincipal();
        String password = (token.getCredentials() == null) ? "NONE_PASS"
                : (String) token.getCredentials();

        log.debug("phone: " + phone);
        log.debug("password: " + password);

//        获取认证用户
        AuthUser userDetails = retrieveUser(phone, (MobileAuthenticationToken) authentication);

        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("*Null userdetails");
        }

//        验证密码
        boolean isValid = bCryptPasswordEncoder.matches(password, userDetails.getPassword());
        if (!isValid) {
            throw new BadCredentialsException("密码错误，请重新输入");
        }

        return createSuccessAuthentication(phone, userDetails);
    }

    private AuthUser retrieveUser(String phone, MobileAuthenticationToken authentication) {
        return mobileUserDetailService.loadUserByPhone((String) phone);
    }

    private Authentication createSuccessAuthentication(Object principal, AuthUser user) {
        MobileAuthenticationToken result = new MobileAuthenticationToken(principal, user.getId(), user.getAuthorities());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
