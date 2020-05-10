package com.smoke.xiguazi.model.po;

import com.smoke.xiguazi.model.vo.RegisterUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String userId;
    private String phone;
    private String passwd;
    private String userName;
    private String email;
    private Integer gender;
    private String address;
    private Integer userRole;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public UserInfo(String phone, String passwd, Integer userRole){
        this.phone = phone;
        this.passwd = passwd;
        this.userRole = userRole;
    }
}
