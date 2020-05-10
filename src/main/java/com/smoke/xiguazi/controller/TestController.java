package com.smoke.xiguazi.controller;

import com.alibaba.fastjson.JSON;
import com.smoke.xiguazi.mapper.UserInfoMapper;
import com.smoke.xiguazi.model.po.UserInfo;
import com.smoke.xiguazi.security.MobileAuthenticationToken;
import com.smoke.xiguazi.service.TransactionService;
import com.smoke.xiguazi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("test")
@Slf4j
public class TestController {
    private final Logger logger = LogManager.getLogger(TestController.class);

    private final UserService userService;
    private final TransactionService transactionService;
    private final UserInfoMapper userInfoMapper;


    private final AtomicInteger count;

    @Autowired
    TestController(UserService userService, TransactionService transactionService, UserInfoMapper userInfoMapper) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.userInfoMapper = userInfoMapper;

        this.count = new AtomicInteger(0);
    }

    @GetMapping("")
    public String test() {
        return "test /test";
    }

    @GetMapping("hello")
    public String hello() {
        return "hello world";
    }

    @GetMapping("log")
    public String log() {
        logger.debug("Debugging log");
        logger.info("Info log");
        logger.warn("Hey, This is a warning!");
        logger.error("Oops! We have an Error. OK");
        logger.fatal("Damn! Fatal error. Please fix me.");

        log.debug("@Slf4j debug");
//        return "test log4j2";
        return log.getName();
    }

    @GetMapping("user-list")
    public String userList() {
        List<UserInfo> all = userService.findAll();

//        显示所有user_id
//        StringBuilder stringBuilder = new StringBuilder();
//
//        for (UserInfo userInfo : all) {
//            stringBuilder.append("id: ");
//            stringBuilder.append(userInfo.getUserId());
//            stringBuilder.append("\n");
//        }
//        return stringBuilder.toString();

        return JSON.toJSONString(all);
    }

    @GetMapping("get-id")
    public String getId() {
        return SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                token.getId() : "null";
    }

    @GetMapping("get-pass")
    public String getPass() {
        return SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
    }

    @GetMapping("mysql")
    public String mysql() {
        log.debug("transaction_info count: " + transactionService.countTransactionInfo());

        return "mysql info";
    }

    @GetMapping("null-instanceof")
    public String nullInstanceof() {
        String string = null;
        //  结果为false
        return string instanceof String s ? s : "something wrong";
    }

    @GetMapping("null-record")
    public String nullRecord() {
        String address = null;
//        DetectionTaskVo detectionTaskVo = new DetectionTaskVo(null, null, null, address, null, null, null);
//        return detectionTaskVo.address()==null ? "null" : "not null";
//        return detectionTaskVo.address();
        return address;
    }

    @GetMapping("null-column")
    public String nullColumn() {
        UserInfo userInfo = userInfoMapper.findById("1");
        String email = userInfo.getEmail();
        return email == null ? "null" : "not null";
    }

    @GetMapping("math")
    public String math() {
        log.debug("" + (-11L / 12));
        return "";
    }

    @GetMapping("controller")
    public String controller() {
        return this.toString();
    }

    @GetMapping("count-add-one")
    public String countAddOne() {
        Integer value = this.count.addAndGet(1);
        return String.valueOf(value);
    }
}
