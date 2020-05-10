package com.smoke.xiguazi.model.vo;

import java.time.LocalDate;

public record TransManageVo(String transId, String bookerId, String address, LocalDate meetDate, String ownerPhone,
                            String bookerPhone, Integer status) {
}
