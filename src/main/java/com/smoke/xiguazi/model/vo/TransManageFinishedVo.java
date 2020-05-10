package com.smoke.xiguazi.model.vo;

import java.time.LocalDateTime;

public record TransManageFinishedVo(String transId, String title, String ownerPhone, String buyerPhone, String city,
                                    LocalDateTime transferTime) {}