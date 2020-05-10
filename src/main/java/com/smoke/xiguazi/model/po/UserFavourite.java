package com.smoke.xiguazi.model.po;

import java.time.LocalDateTime;

public record UserFavourite(String userId, String transId, LocalDateTime createTime, LocalDateTime updateTime) {}

/*
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFavourite {
    private String userId;
    private String transId;
    private java.time.LocalDateTime createTime;
    private LocalDateTime updateTime;

 */