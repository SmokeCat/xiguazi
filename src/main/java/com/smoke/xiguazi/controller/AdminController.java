package com.smoke.xiguazi.controller;

import com.smoke.xiguazi.service.AdminService;
import com.smoke.xiguazi.utils.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * 创建顾问账号，使用默认密码
     * @param phone
     * @return
     */
    @GetMapping("consultant/add/{phone}")
    public String addNewConsultant(@PathVariable("phone") String phone){
        adminService.addConsultant(phone);
        return "phone: " + phone + "\npassword: " + ConstUtil.DEFAULT_PASSWORD;
    }
}
