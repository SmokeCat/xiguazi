package com.smoke.xiguazi.service.impl;

import com.alibaba.fastjson.JSON;
import com.smoke.xiguazi.mapper.CarInfoMapper;
import com.smoke.xiguazi.mapper.TransactionInfoMapper;
import com.smoke.xiguazi.model.po.CarInfo;
import com.smoke.xiguazi.model.po.TransactionInfo;
import com.smoke.xiguazi.model.vo.SearchPageVo;
import com.smoke.xiguazi.model.vo.TransSearchParam;
import com.smoke.xiguazi.model.vo.TransVo;
import com.smoke.xiguazi.service.ElasticsearchService;
import com.smoke.xiguazi.utils.ConstUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ElasticsearchServiceImpl implements ElasticsearchService {
    private final RestHighLevelClient client;
    private final CarInfoMapper carInfoMapper;
    private final TransactionInfoMapper transactionInfoMapper;

    Map<String, String> carMakeMap;
    Map<String, String> cityMap;

    @Autowired
    public ElasticsearchServiceImpl(
            @Qualifier("restHighLevelClient") RestHighLevelClient restHighLevelClient, CarInfoMapper carInfoMapper,
            TransactionInfoMapper transactionInfoMapper, Map<String, String> carMakeMap, Map<String, String> cityMap) {
        this.client = restHighLevelClient;
        this.carInfoMapper = carInfoMapper;
        this.transactionInfoMapper = transactionInfoMapper;
        this.carMakeMap = carMakeMap;
        this.cityMap = cityMap;
    }

    /**
     * 重建index
     * @param index
     * @return
     * @throws IOException
     */
    @Override
    public boolean rebuildIndex(String index) throws IOException {
        //  判断index是否存在
        boolean exists = existsIndex(index);

        //  如果存在则先删除index
        if(exists){
            boolean deleteResult = deleteIndex(index);
        }

        //  创建index
        boolean isAcknowledged = createIndex(index);

        return isAcknowledged;
    }

    /**
     *  判断index是否存在
     * @param index
     * @return  boolean
     * @throws IOException
     */
    @Override
    public boolean existsIndex(String index) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        return exists;
    }

    /**
     * 删除index
     * @param index
     * @return
     * @throws IOException
     */
    @Override
    public boolean deleteIndex(String index) throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
        deleteIndexRequest.timeout(TimeValue.timeValueSeconds(2));
        AcknowledgedResponse deleteIndexResponse = client.indices().delete(deleteIndexRequest,
                RequestOptions.DEFAULT);
        return deleteIndexResponse.isAcknowledged();
    }

    /**
     * 创建index
     * @param index
     * @return
     * @throws IOException
     */
    @Override
    public boolean createIndex(String index) throws IOException {
        if (ConstUtil.TRANS_INDEX_NAME.equals(index)) {
            return createXiguaziIndex();
        }
        // 创建index
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }

    /**
     *
     * 获取购买页交易列表
     * @param params        筛选条件
     * @param indexPage     当前页数
     * @return  筛选搜索结果
     * @throws IOException
     */
    @Override
    public SearchPageVo searchTrans(TransSearchParam params, Long indexPage) throws IOException {
        Integer pageSize = ConstUtil.TRANS_SEARCH_PAGE_SIZE;
        Long pageFrom = (indexPage - 1) * pageSize;

        //  初始化返回交易列表
        List<TransVo> transVOList = new ArrayList<>();

        //  构建查询请求
        SearchRequest searchRequest = new SearchRequest(ConstUtil.TRANS_INDEX_NAME);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(pageFrom.intValue());
        sourceBuilder.size(pageSize);

        //  设置筛选条件
        boolean hasClauses = setQueryParam(params, sourceBuilder);

        //  查看请求体
        log.debug("sourceBuilder:\n" + sourceBuilder.toString());

        //  执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        //  装配结果
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {
            String sourceAsString = documentFields.getSourceAsString();
            transVOList.add(JSON.parseObject(sourceAsString, TransVo.class));
        }

        // 总分页数
        long totalPage = (searchResponse.getHits().getTotalHits().value - 1) / ConstUtil.TRANS_SEARCH_PAGE_SIZE + 1;
        //  如果indexPage超出totalPage，则设置indexPage为totalPage
        indexPage = Math.min(indexPage, totalPage);

        //  构造返回结果
        return new SearchPageVo(transVOList, indexPage, totalPage);
    }

    /**
     * 添加document到elasticsearch中
     * @param transId
     * @return  doc id
     * @throws IOException
     */
    @Override
    public String addDocument(String transId) throws IOException {
        //  装配对象
        TransactionInfo transInfo = transactionInfoMapper.findByTransId(transId);
        CarInfo carInfo = carInfoMapper.findById(transInfo.getCarId());
        TransVo transVO = new TransVo(transInfo, carInfo);

        //  构造请求
        IndexRequest indexRequest = new IndexRequest(ConstUtil.TRANS_INDEX_NAME);
        indexRequest.source(JSON.toJSONString(transVO), XContentType.JSON);

        //  执行请求
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

        return indexResponse.getId();
    }

    /**
     * 从elasticsearch中删除document
     * @param transId
     * @return  status
     * @throws IOException
     */
    @Override
    public Integer deleteDocument(String transId) throws IOException {
        //  获取document id
        String docId = getDocId(transId);

        //  构造请求并执行
        DeleteRequest deleteRequest = new DeleteRequest(ConstUtil.TRANS_INDEX_NAME, docId);
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        return deleteResponse.status().getStatus();
    }

    /**
     * 创建车辆搜索index
     * @return
     * @throws IOException
     */
    private boolean createXiguaziIndex() throws IOException {
        //  创建trans index
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(ConstUtil.TRANS_INDEX_NAME);
        //  获取source文件的json字符串
        String source = getXiguaziIndexSource();
        createIndexRequest.source(source, XContentType.JSON);
        //  执行请求
        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);

        //  填充数据
        fillXiguaziIndex();

        return createIndexResponse.isAcknowledged();
    }

    /**
     * 将发布中交易信息填充到index
     */
    private void fillXiguaziIndex() throws IOException {
        //  获取发布中的交易
        List<TransVo> releasedTrans = getReleasedTrans();

        //  批量添加document
        BulkRequest bulkRequest = new BulkRequest(ConstUtil.TRANS_INDEX_NAME);
        bulkRequest.timeout(TimeValue.timeValueSeconds(5));
        for (TransVo transVO : releasedTrans) {
            bulkRequest.add(new IndexRequest(ConstUtil.TRANS_INDEX_NAME)
                    .source(JSON.toJSONString(transVO), XContentType.JSON));
        }
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        log.debug("bulkResponse.hasFailures:\n" + bulkResponse.hasFailures());
    }

    /**
     * 获取发布中的交易
     * @return
     */
    private List<TransVo> getReleasedTrans(){
        List<TransactionInfo> releasedTransList = transactionInfoMapper.findByStatus(ConstUtil.TRANS_STATUS_RELEASED);
        List<TransVo> transVOList = new ArrayList<>();
        for (TransactionInfo transactionInfo : releasedTransList) {
            transVOList.add(new TransVo(transactionInfo, carInfoMapper.findById(transactionInfo.getCarId())));
        }
        return transVOList;
    }

    /**
     * 获取index mapping 定义
     * @return
     * @throws IOException
     */
    private String getXiguaziIndexSource() throws IOException {
        File file = ResourceUtils.getFile(ConstUtil.TRANS_INDEX_MAPPING_JSON_FILE_PATH);
        StringBuilder stringBuilder = new StringBuilder();
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            int num;
            while ((num = fr.read()) != -1) {
                stringBuilder.append((char) num);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fr.close();
        }
        return stringBuilder.toString();
    }

    /**
     * 根据筛选条件设置请求
     * @param params    筛选条件
     *                      -city               城市
     *                      -make               厂商
     *                      -mileage            行驶里程    range
     *                      -enginedisplament   发动机排量   range
     *                      -transmission       自动挡 1=自动挡，0=手动挡
     *                      -price              价格  range
     * @param sourceBuilder 请求构造
     * @return  如果构造了筛选条件，返回true
     */
    private boolean setQueryParam(TransSearchParam params, SearchSourceBuilder sourceBuilder) {
        String city = params.getCity();
        String make = params.getMake();
        String mileage = params.getMileage();
        String enginedisplament = params.getEnginedisplament();
        String transmission = params.getTransmission();
        String price = params.getPrice();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 设置 make、city、transmission 搜索条件
        if(!"all".equals(make)){
            boolQueryBuilder.must(QueryBuilders.matchQuery("car.make", carMakeMap.get(make)));
        }
        if (!"all".equals(city)){
            boolQueryBuilder.must(QueryBuilders.matchQuery("city", cityMap.get(city)));
        }
        if (!"all".equals(transmission)){
            boolQueryBuilder.must(QueryBuilders.matchQuery("car.isAutomaticTransmission",
                    "1".equals(params.getTransmission())));
        }

        //  设置 mileage、enginedisplament、price 搜索范围
        mileageRangeQuery(boolQueryBuilder, mileage);
        enginedisplamentRangeQuery(boolQueryBuilder, enginedisplament);
        priceRangeQuery(boolQueryBuilder, price);

        //  设置排序条件
        sourceBuilder.sort(new FieldSortBuilder("createTime").order(SortOrder.DESC));

        //  如果boolQueryBuilder没有设置条件则跳过
        boolean hasClauses = boolQueryBuilder.hasClauses();
        if(hasClauses){
            sourceBuilder.query(boolQueryBuilder);
        }

        log.debug("boolQueryBuilder.hasClauses():\n" + hasClauses);
        log.debug("boolQueryBuilder:\n"+boolQueryBuilder.toString());

        return hasClauses;
    }

    /**
     * 设置mileage搜索条件
     * @param boolQueryBuilder
     * @param mileage
     */
    private void mileageRangeQuery(BoolQueryBuilder boolQueryBuilder, String mileage) {
        if(!"all".equals(mileage)){
            switch (mileage){
                case "0":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("car.mileage").gte(0).lte(10000));
                    break;
                case "1":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("car.mileage").gte(10000).lte(30000));
                    break;
                case "2":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("car.mileage").gte(30000).lte(50000));
                    break;
                case "3":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("car.mileage").gte(50000).lte(80000));
                    break;
                case "4":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("car.mileage").gte(80000));
                    break;
            }
        }
    }

    /**
     * 设置enginedisplament搜索条件
     * @param boolQueryBuilder
     * @param enginedisplament
     */
    private void enginedisplamentRangeQuery(BoolQueryBuilder boolQueryBuilder, String enginedisplament) {
        if(!"all".equals(enginedisplament)){
            switch (enginedisplament){
                case "0":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("car.engineDisplacement").gte(0F).lte(1.0F));
                    break;
                case "1":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("car.engineDisplacement").gte(1.0F).lte(1.6F));
                    break;
                case "2":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("car.engineDisplacement").gte(1.6F).lte(2.0F));
                    break;
                case "3":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("car.engineDisplacement").gte(2.0F).lte(3.0F));
                    break;
                case "4":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("car.engineDisplacement").gte(3.0F).lte(4.0F));
                    break;
                case "5":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("car.engineDisplacement").gte(4.0F));
                    break;
            }
        }
    }

    /**
     * 设置price搜索条件
     * @param boolQueryBuilder
     * @param price
     */
    private void priceRangeQuery(BoolQueryBuilder boolQueryBuilder, String price) {
        if(!"all".equals(price)){
            switch (price){
                case "0":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(0L).lte(30000L));
                    break;
                case "1":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(30000L).lte(50000L));
                    break;
                case "2":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(50000L).lte(70000L));
                    break;
                case "3":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(70000L).lte(90000L));
                    break;
                case "4":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(90000L).lte(120000L));
                    break;
                case "5":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(120000L).lte(160000L));
                    break;
                case "6":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(160000L).lte(200000L));
                    break;
                case "7":
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(200000L));
                    break;
            }
        }
    }

    /**
     * 获取document id
     * @param transId
     * @return  document id
     */
    private String getDocId(String transId) throws IOException {
        //  构造请求
        SearchRequest searchRequest = new SearchRequest(ConstUtil.TRANS_INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("transId", transId));
        searchRequest.source(searchSourceBuilder);

        //  执行请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHit[] hits = searchResponse.getHits().getHits();
        if(hits.length == 1){
            return hits[0].getId();
        }

        //  error
        log.error("get document id error:\n" + searchResponse.toString());
        return null;
    }
}
