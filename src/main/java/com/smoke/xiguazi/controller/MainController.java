package com.smoke.xiguazi.controller;

import com.smoke.xiguazi.model.vo.RegisterUser;
import com.smoke.xiguazi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
@SessionScope
public class MainController {
    private final UserService userService;

    @Autowired
    public MainController(UserService userService){
        this.userService = userService;
    }

    /**
     * 主页
     * @return  主页
     */
    @GetMapping("/")
    public String index(){
        return "index";
    }

    /**
     * 获取登陆页面
     * @return  登陆页面
     */
    @GetMapping("login")
    public String login(){
        return "login";
    }

    /**
     * 退出登陆
     * @param request
     * @param response
     * @return  主页
     */
    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    /**
     * 注册新用户，处理注册表单
     * @param request
     * @param user
     * @return  主页
     */
    @PostMapping("register")
    public String register(HttpServletRequest request, @ModelAttribute("user") RegisterUser user){
        userService.register(request, user);
        return "redirect:/";
    }
}
