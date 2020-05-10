package com.smoke.xiguazi.model.vo;

import java.util.List;

public record SearchPageVo(List<TransVo> transList, Long indexPage, Long totalPage) {}
