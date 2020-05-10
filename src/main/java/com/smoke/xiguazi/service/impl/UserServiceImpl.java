package com.smoke.xiguazi.service.impl;

import com.smoke.xiguazi.mapper.*;
import com.smoke.xiguazi.model.dto.AuthUser;
import com.smoke.xiguazi.model.po.CarInfo;
import com.smoke.xiguazi.model.po.TransactionInfo;
import com.smoke.xiguazi.model.po.UserInfo;
import com.smoke.xiguazi.model.vo.RegisterUser;
import com.smoke.xiguazi.model.vo.UserBaseInfoVo;
import com.smoke.xiguazi.model.vo.UserFavouriteVo;
import com.smoke.xiguazi.model.vo.UserinfoVo;
import com.smoke.xiguazi.security.MobileAuthenticationManager;
import com.smoke.xiguazi.security.MobileAuthenticationToken;
import com.smoke.xiguazi.service.UserService;
import com.smoke.xiguazi.utils.ConstUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final SysRoleMapper sysRoleMapper;
    private final UserInfoMapper userInfoMapper;
    private final UserFavouriteMapper userFavouriteMapper;
    private final TransactionInfoMapper transactionInfoMapper;
    private final CarInfoMapper carInfoMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MobileAuthenticationManager mobileAuthenticationManager;

    @Autowired
    public UserServiceImpl(SysRoleMapper sysRoleMapper, UserInfoMapper userInfoMapper,
                           UserFavouriteMapper userFavouriteMapper, TransactionInfoMapper transactionInfoMapper,
                           CarInfoMapper carInfoMapper, BCryptPasswordEncoder bCryptPasswordEncoder,
                           MobileAuthenticationManager mobileAuthenticationManager) {
        this.sysRoleMapper = sysRoleMapper;
        this.userInfoMapper = userInfoMapper;
        this.userFavouriteMapper = userFavouriteMapper;
        this.transactionInfoMapper = transactionInfoMapper;
        this.carInfoMapper = carInfoMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mobileAuthenticationManager = mobileAuthenticationManager;
    }

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    @Override
    public Integer register(HttpServletRequest request, RegisterUser user) {
        //  加密密码
        UserInfo userInfo = new UserInfo(user.phone(), bCryptPasswordEncoder.encode(user.password()), ConstUtil.ROLE_USER_ID);
        Integer result = userInfoMapper.insert(userInfo);

        //  自动登陆
        MobileAuthenticationToken token = new MobileAuthenticationToken(user.phone(), user.password());
        try {
            token.setDetails(new AuthUser(userInfo));
            Authentication authentication = mobileAuthenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());
        } catch (AuthenticationException e) {
            log.error("Authentication failed: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取用户role_name
     *
     * @param roleId
     * @return
     */
    @Override
    public String getRoleNameByRoleId(Integer roleId) {
        return sysRoleMapper.findNameById(roleId);
    }

    /**
     * 获取用户基本信息
     * 当前用户
     *
     * @return
     */
    @Override
    public UserinfoVo getUserinfoPage() {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: getUserinfoPage()");
        }
        return getUserinfoPage(userId);
    }

    /**
     * 获取用户基本信息
     * 指定用户
     *
     * @param userId 指定用户
     * @return
     */
    @Override
    public UserinfoVo getUserinfoPage(String userId) {
        UserInfo userInfo = userInfoMapper.findById(userId);
        return new UserinfoVo(userInfo.getUserName(), userInfo.getGender(), userInfo.getEmail(),
                userInfo.getAddress(), getRoleNameByRoleId(userInfo.getUserRole()));
    }

    /**
     * 修改用户基本信息
     * 当前用户
     *
     * @param user
     */
    @Override
    public void modifyUserBaseInfo(UserBaseInfoVo user) {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: modifyUserBaseInfo()");
        }
        modifyUserBaseInfo(user, userId);
    }

    /**
     * 修改用户基本信息
     * 指定用户
     *
     * @param userId 指定用户
     * @return
     */
    @Override
    public void modifyUserBaseInfo(UserBaseInfoVo user, String userId) {
        Integer result = userInfoMapper.updateBaseInfoById(user.userName(), user.email(), user.gender(),
                user.address(), userId);
        if (result != 1) {
            log.error("modifyUserBaseInfo() error");
        }
    }

    /**
     * 修改当前用户密码
     * @param oldPass
     * @param newPass
     */
    @Override
    public void modifyPassword(String oldPass, String newPass) throws CredentialException {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: modifyUserBaseInfo()");
        }
        modifyPassword(oldPass, newPass, userId);
    }

    /**
     * 修改指定用户密码
     * @param oldPass
     * @param newPass
     * @param userId
     */
    @Override
    public void modifyPassword(String oldPass, String newPass, String userId) throws CredentialException {
        String originPass = userInfoMapper.findPasswdById(userId);

        boolean matches = bCryptPasswordEncoder.matches(oldPass, originPass);
        if(!matches){
            throw new CredentialException("modifyPassword(): wrong passwd!");
        }

        userInfoMapper.updatePasswdById(bCryptPasswordEncoder.encode(newPass), userId);
    }

    /**
     * 获取用户收藏列表
     * 当前用户
     *
     * @return
     */
    @Override
    public List<UserFavouriteVo> getUserFavouriteList() {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: getUserFavouriteList()");
        }
        return getUserFavouriteList(userId);
    }

    /**
     * 获取用户收藏列表
     * 指定用户
     *
     * @param userId 指定用户
     * @return
     */
    @Override
    public List<UserFavouriteVo> getUserFavouriteList(String userId) {
        List<String> transList = userFavouriteMapper.findTransIdByUserId(userId);
        List<UserFavouriteVo> userFavouriteVoList = new ArrayList<>();
        for (String transId : transList) {
            TransactionInfo transInfo = transactionInfoMapper.findByTransId(transId);
            CarInfo carInfo = carInfoMapper.findById(transId);

            String carPicture = transInfo.getTransStatus() == 1 ? transInfo.getCarPicture().split(";")[0] : ConstUtil.DISABLED_CAR_PICTURE;
            String title = String.join(" ", carInfo.getMake(), carInfo.getModel(), transInfo.getCity(),
                    carInfo.getModelYear() + "款");
            Boolean isInvalid = !transInfo.getTransStatus().equals(1);

            UserFavouriteVo userFavouriteVo = new UserFavouriteVo(transId, transInfo.getCity(),
                    transInfo.getPrice().toString(), carPicture, title, isInvalid);

            userFavouriteVoList.add(userFavouriteVo);
        }

        return userFavouriteVoList;
    }

    /**
     * 获取所有用户列表
     *
     * @return
     */
    @Override
    public List<UserInfo> findAll() {
        return userInfoMapper.findAll();
    }
}
