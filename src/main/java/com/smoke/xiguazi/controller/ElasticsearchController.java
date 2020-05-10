package com.smoke.xiguazi.controller;

import com.smoke.xiguazi.service.ElasticsearchService;
import com.smoke.xiguazi.utils.ConstUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("es")
@Slf4j
public class ElasticsearchController {

    ElasticsearchService elasticsearchService;

    @Autowired
    public ElasticsearchController(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    @GetMapping("rebuild")
    public String rebuild(){
        return "redirect:/es/rebuild/" + ConstUtil.TRANS_INDEX_NAME;
    }

    @GetMapping("rebuild/{index}")
    public String rebuildIndex(@PathVariable("index") String index) throws IOException {
        boolean result = elasticsearchService.rebuildIndex(index);
        return "rebuild " + index + ": " + result;
    }
}
