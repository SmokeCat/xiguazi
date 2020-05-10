package com.smoke.xiguazi.config;

import com.smoke.xiguazi.model.po.DetectionItem;
import com.smoke.xiguazi.service.RepositoryService;
import com.smoke.xiguazi.utils.UtilsMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan(basePackageClasses = {UtilsMarker.class})
public class RepositoryConfiguration {

    private final RepositoryService repositoryService;

    @Autowired
    RepositoryConfiguration(RepositoryService repositoryService){
        this.repositoryService = repositoryService;
    }

    /**
     * BCrypt 加密
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * key = role_id, value = role_name
     * @return
     */
    @Bean
    Map<Integer, String> roleIdToName(){
        return repositoryService.getRoleIdToName();
    }

    /**
     * 厂商列表
     * @return
     */
    @Bean
    List<String> carMakeList(){
        List<String> carMakeList = repositoryService.getCarMakeList();
        return carMakeList;
    }

    /**
     * 汽车厂商Map, key = id, value = make
     * @return
     */
    @Bean("carMakeMap")
    Map<String, String> carMakeMap(){
        return repositoryService.getCarMakeMap();
    }

    /**
     * 交易车辆城市Map, key = id, value = city
     * @return
     */
    @Bean("cityMap")
    Map<String, String> cityMap(){
        return repositoryService.getCityMap();
    }

    @Bean
    List<DetectionItem> detectionItemList(){
        return repositoryService.getDetectionItemList();
    }

    @Bean("detectTypeItemListMap")
    @Autowired
    Map<String, List<DetectionItem>> detectTypeItemListMap(List<DetectionItem> detectionItemList){
        return repositoryService.getDetectTypeItemListMap(detectionItemList);
    }

    @Bean
    @Autowired
    Map<Integer, DetectionItem> detectIdItemMap(List<DetectionItem> detectionItemList){
        return repositoryService.getDetectIdItemMap(detectionItemList);
    }
}
