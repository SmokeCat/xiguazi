package com.smoke.xiguazi.utils;

public class ConstUtil {
    //  sys_role id
    public static final Integer ROLE_USER_ID = 1;
    public static final Integer ROLE_ADMIN_ID = 10;
    public static final Integer ROLE_CONSULTANT_ID = 11;
    public static final Integer ROLE_TEMPORARY_ID = 20;
    public static final Integer ROLE_CANCELED_ID = 21;

    //  trans_info status
    public static final Integer TRANS_STATUS_WAIT_DETECTION = 0;    //  等待车检
    public static final Integer TRANS_STATUS_RELEASED = 1;          //  发布中
    public static final Integer TRANS_STATUS_SIGNED = 2;            //  已签订合同
    public static final Integer TRANS_STATUS_TRANSFERRED = 3;       //  已过户
    public static final Integer TRANS_STATUS_CANCELED = 4;          //  已取消

    //  detection_taks status
    public static final Integer DETEDTION_TASK_STATUS_UNASSIGNED = 0;   //  为分配车检员
    public static final Integer DETEDTION_TASK_STATUS_ASSIGNED = 1;   //  已分配车检员,等待车检
    public static final Integer DETEDTION_TASK_STATUS_FINISHED = 2;   //  车检已完成

    //  默认密码
    public static final String DEFAULT_PASSWORD = "123456";

    // elasticsearch index name
    public static final String TRANS_INDEX_NAME = "trans";
//    public static final String TRANS_INDEX_NAME = "xiguazi";

    //  trans index mapping define file
    public static final String TRANS_INDEX_MAPPING_JSON_FILE_PATH = "classpath:trans-index-source.json";

    //  trans index page size
    public static final Integer TRANS_SEARCH_PAGE_SIZE = 12;

    //  下架车辆图片名
    public static final String DISABLED_CAR_PICTURE = "_car-disabled.png";

    //  车辆图片前缀
    public static final String CAR_PIC_PREFIX = "images/carpic/";

    //  合同文件前缀
    public static  final String CONTRACT_PREFIX = "contract/";
}
