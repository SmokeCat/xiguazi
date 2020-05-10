package com.smoke.xiguazi.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MobileAuthenticationToken extends AbstractAuthenticationToken {

    //    手机号
    private final Object principal;
    //    密码
    private Object credentials;
    //  id
    private String id;

    public MobileAuthenticationToken(Object phone, Object password) {
        super(null);
        this.principal = phone;
        this.credentials = password;
        setAuthenticated(false);
    }

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public MobileAuthenticationToken(Object phone, Object id,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = phone;
        this.credentials = "*";
        this.id = (String) id;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("*Cannot set this token to trusted - use constructor which takes a " +
                    "GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public String getId(){
        return this.id;
    }

    private void setId(String id){
        this.id = id;
    }
}
