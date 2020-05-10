package com.smoke.xiguazi.model.po;

import java.time.LocalDateTime;

public record TransactionContract(String transId, String partyA, String partyB, String partyC, String contractFile,
                                  LocalDateTime createTime, LocalDateTime updateTime) {}
