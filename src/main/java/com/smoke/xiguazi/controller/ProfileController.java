package com.smoke.xiguazi.controller;

import com.smoke.xiguazi.model.vo.*;
import com.smoke.xiguazi.service.TransactionService;
import com.smoke.xiguazi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.login.CredentialException;
import java.util.List;

@Controller
@RequestMapping("profile")
@SessionScope
@Slf4j
public class ProfileController {
    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired
    public ProfileController(UserService userService, TransactionService transactionService){
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @ModelAttribute("phone")
    String userPhone(){
        //获取当前用户phone
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    /**
     * 用户信息页
     * @return  重定向到基本信息
     */
    @GetMapping("")
    public String profile(){
        return "redirect:/profile/userinfo";
    }

    /**
     * 基本信息
     * @return
     */
    @GetMapping("userinfo")
    public ModelAndView userinfo(){
        //  视图名
        ModelAndView mav = new ModelAndView("profile/userinfo");

        //  获取渲染数据
        UserinfoVo userinfo = userService.getUserinfoPage();
        log.debug("/profile/userinfo - userinfoVo:\n" + userinfo);

        //  装配渲染数据
        mav.addObject("user", userinfo);
        return mav;
    }

    /**
     * 修改基本信息页
     * @return
     */
    @GetMapping("userinfo/modify")
    public ModelAndView modify(){
        //  视图名
        ModelAndView mav = new ModelAndView("profile/modify");

        //  获取渲染数据
        UserinfoVo userinfo = userService.getUserinfoPage();
        log.debug("/profile/userinfo/modify - userinfoVo:\n" + userinfo);

        //  装配渲染数据
        mav.addObject("user", userinfo);
        return mav;
    }

    /**
     * 修改密码页面
     * @return
     */
    @GetMapping("userinfo/modify/password")
    public ModelAndView modifyPass(){
        return new ModelAndView("profile/modify-pass");
    }

    @PostMapping("userinfo/modify/password")
    public String modifyPass(@RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass) throws CredentialException {

        userService.modifyPassword(oldPass, newPass);
        return "redirect:/logout";
    }

    /**
     * 处理修改基本信息表单
     * @param userBaseInfoVo
     * @return
     */
    @PostMapping("userinfo/modify")
    public String modifyForm(@ModelAttribute UserBaseInfoVo userBaseInfoVo){
        //  业务逻辑
        userService.modifyUserBaseInfo(userBaseInfoVo);

        return "redirect:/profile/userinfo";
    }

    /**
     * 用户收藏列表
     * @return
     */
    @GetMapping("favourite")
    public ModelAndView favourite(){
        //  视图名
        ModelAndView mav = new ModelAndView("profile/favourite");

        //  获取渲染数据
        List<UserFavouriteVo> userFavouriteList = userService.getUserFavouriteList();
        log.debug("/profile/favourite - userFavouriteList:\n" + userFavouriteList);

        //  装配渲染数据
        mav.addObject("favourites", userFavouriteList);
        return mav;
    }

    /**
     * 移除收藏的trans
     *
     * @param transId
     * @return
     */
    @GetMapping("/favourite/remove/{transId}")
    public String removeFavourite(@PathVariable("transId") String transId) {
        //  业务逻辑
        transactionService.removeFavourite(transId);

        //  转发页面
        return "redirect:/profile/favourite";
    }

    /**
     * 重定向到卖车列表
     * @return
     */
    @GetMapping("trans")
    public String trans(){
        return "redirect:/profile/trans/sell";
    }

    /**
     * 用户卖车交易列表
     *
     * @return
     */
    @GetMapping("trans/sell")
    public ModelAndView sellList(){
        //  视图名
        ModelAndView mav = new ModelAndView("profile/trans-sell");

        //  获取出售交易列表
        List<SellTransVo> sellTransList = transactionService.getSellTransList();

        mav.addObject("sellTransList", sellTransList);
        return mav;
    }

    /**
     * 用户买车交易列表
     * @return
     */
    @GetMapping("trans/buy")
    public ModelAndView buyList(){
        //  视图名
        ModelAndView mav = new ModelAndView("profile/trans-buy");
        //  获取购买交易列表
        List<BuyTransVo> buyTransList = transactionService.getBuyTransList();

        mav.addObject("buyTransList", buyTransList);
        return mav;
    }

    /**
     * 获取看车预约列表
     * @return
     */
    @GetMapping("trans/reserve")
    public ModelAndView reservationList(){
        //  视图名
        ModelAndView mav = new ModelAndView("profile/trans-reserve");
        //  获取看车预约列表
        List<ReservationVo> reservationList = transactionService.getReservationList();

        mav.addObject("reservationList", reservationList);
        return mav;
    }

    /**
     * 删除看车预约
     * @param transId
     * @return
     */
    @GetMapping("reservation/delete/{transId}")
    public String deleteReservation(@PathVariable("transId") String transId){
        //  业务逻辑
        transactionService.deleteReservation(transId);
        //  转发到预约列表页
        return "redirect:/profile/trans/reserve";
    }

}
