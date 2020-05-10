package com.smoke.xiguazi.service;

import com.smoke.xiguazi.model.po.DetectionItem;

import java.util.List;
import java.util.Map;

public interface RepositoryService {
    Map<Integer, String> getRoleIdToName();
    List<String> getCarMakeList();
    Map<String, String> getCarMakeMap();
    Map<String, String> getCityMap();
    List<DetectionItem> getDetectionItemList();
    Map<String, List<DetectionItem>> getDetectTypeItemListMap(List<DetectionItem> detectionItemList);
    Map<Integer, DetectionItem> getDetectIdItemMap(List<DetectionItem> detectionItemList);
}
