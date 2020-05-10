package com.smoke.xiguazi.model.dto;

import com.smoke.xiguazi.model.po.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class AuthUser implements UserDetails {

    private final transient String userId;
    private final transient String phone;
    private final transient String password;
    private transient String roleName;

    public AuthUser(UserInfo userInfo) {
        this.userId = userInfo.getUserId();
        this.phone = userInfo.getPhone();
        this.password = userInfo.getPasswd();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getId(){
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setRoleName(String roleName){
        this.roleName = roleName;
    }
}
