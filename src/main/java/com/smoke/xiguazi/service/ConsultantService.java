package com.smoke.xiguazi.service;

import com.smoke.xiguazi.model.vo.DetectionResultFormVo;
import com.smoke.xiguazi.model.vo.DetectionTaskVo;
import com.smoke.xiguazi.model.vo.TransManageFinishedVo;
import com.smoke.xiguazi.model.vo.TransManageVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ConsultantService {

    List<DetectionTaskVo> getDetectionTaskList();

    List<DetectionTaskVo> getDetectionTaskList(String consultantId);

    List<TransManageVo> getTransManageList();

    List<TransManageVo> getTransManageList(String consultantId);

    List<TransManageFinishedVo> getTransManageFinishedList();

    List<TransManageFinishedVo> getTransManageFinishedList(String consultantId);

    DetectionResultFormVo getDetectionForm(String transId);

    DetectionResultFormVo getDetectionForm(String transId, String consultantId);

    void saveDetectionForm(DetectionResultFormVo detectionResultForm);

    void saveDetectionForm(DetectionResultFormVo detectionResultForm, String consultantId);

    void uploadPic(String transId, List<MultipartFile> files) throws IOException;

    void uploadContract(String transId, String bokerId, MultipartFile file) throws IOException;

    String releaseTrans(String transId) throws IOException;

    void deleteReservation(String transId, String bookerId);

    void confirmReservation(String transId, String bookerId);

    void confirmTransfer(String transId);
}
