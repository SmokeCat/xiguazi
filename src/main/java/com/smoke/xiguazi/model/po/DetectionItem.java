package com.smoke.xiguazi.model.po;

import java.time.LocalDateTime;

public record DetectionItem(Integer itemId, String detectionType, String content, LocalDateTime createTime,
                            LocalDateTime updateTime) {}
