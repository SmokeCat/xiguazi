package com.smoke.xiguazi.security;

import com.smoke.xiguazi.mapper.UserInfoMapper;
import com.smoke.xiguazi.model.dto.AuthUser;
import com.smoke.xiguazi.model.po.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class MobileUserDetailService implements UserDetailsService {

    private UserInfoMapper userInfoMapper;
    private Map<Integer, String> roleIdToName;

    @Autowired
    public MobileUserDetailService(UserInfoMapper userInfoMapper, Map<Integer, String> roleIdToName){
        this.userInfoMapper = userInfoMapper;
        this.roleIdToName = roleIdToName;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public AuthUser loadUserByPhone(String phone) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoMapper.findByPhone(phone);

        if (userInfo == null) {
            throw new UsernameNotFoundException("账号不存在，请重新输入");
        }

        AuthUser authUser = new AuthUser(userInfo);
        authUser.setRoleName("ROLE_" + roleIdToName.get(userInfo.getUserRole()));
        return authUser;
    }

}
