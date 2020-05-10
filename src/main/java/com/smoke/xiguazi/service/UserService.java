package com.smoke.xiguazi.service;

import com.smoke.xiguazi.model.po.UserInfo;
import com.smoke.xiguazi.model.vo.RegisterUser;
import com.smoke.xiguazi.model.vo.UserBaseInfoVo;
import com.smoke.xiguazi.model.vo.UserFavouriteVo;
import com.smoke.xiguazi.model.vo.UserinfoVo;

import javax.security.auth.login.CredentialException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {

    //  Integer register(RegisterUser user, Integer userRole);
    Integer register(HttpServletRequest request, RegisterUser user);

    String getRoleNameByRoleId(Integer roleId);

    UserinfoVo getUserinfoPage();

    UserinfoVo getUserinfoPage(String userId);

    void modifyUserBaseInfo(UserBaseInfoVo user);

    void modifyUserBaseInfo(UserBaseInfoVo user, String userId);

    void modifyPassword(String oldPass, String newPass) throws CredentialException;

    void modifyPassword(String oldPass, String newPass, String userId) throws CredentialException;

    List<UserFavouriteVo> getUserFavouriteList();

    List<UserFavouriteVo> getUserFavouriteList(String userId);

    List<UserInfo> findAll();
}
