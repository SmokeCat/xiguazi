package com.smoke.xiguazi.service;

import com.smoke.xiguazi.model.vo.SearchPageVo;
import com.smoke.xiguazi.model.vo.TransSearchParam;

import java.io.IOException;

public interface ElasticsearchService {

    boolean rebuildIndex(String index) throws IOException;

    boolean existsIndex(String index) throws IOException;

    boolean deleteIndex(String index) throws IOException;

    boolean createIndex(String index) throws IOException;

    SearchPageVo searchTrans(TransSearchParam params, Long indexPage) throws IOException;

    String addDocument(String transId) throws IOException;

    Integer deleteDocument(String transId) throws IOException;
}
