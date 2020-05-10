package com.smoke.xiguazi.service.impl;

import com.smoke.xiguazi.mapper.*;
import com.smoke.xiguazi.model.po.*;
import com.smoke.xiguazi.model.vo.DetectionResultFormVo;
import com.smoke.xiguazi.model.vo.DetectionTaskVo;
import com.smoke.xiguazi.model.vo.TransManageFinishedVo;
import com.smoke.xiguazi.model.vo.TransManageVo;
import com.smoke.xiguazi.security.MobileAuthenticationToken;
import com.smoke.xiguazi.service.ConsultantService;
import com.smoke.xiguazi.service.ElasticsearchService;
import com.smoke.xiguazi.utils.ConstUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ConsultantServiceImpl implements ConsultantService {
    private final DetectionTaskMapper detectionTaskMapper;
    private final CarInfoMapper carInfoMapper;
    private final TransactionInfoMapper transactionInfoMapper;
    private final UserInfoMapper userInfoMapper;
    private final DetectionResultMapper detectionResultMapper;
    private final ReservationMapper reservationMapper;
    private final TransactionContractMapper transactionContractMapper;

    private final ElasticsearchService elasticsearchService;

    private final List<DetectionItem> detectionItemList;

    /**
     * 储存静态资源的路径
     */
    @Value("${resource_path}")
    private String resource_path;

    public ConsultantServiceImpl(DetectionTaskMapper detectionTaskMapper, CarInfoMapper carInfoMapper,
                                 TransactionInfoMapper transactionInfoMapper, UserInfoMapper userInfoMapper,
                                 DetectionResultMapper detectionResultMapper, ReservationMapper reservationMapper,
                                 TransactionContractMapper transactionContractMapper,
                                 ElasticsearchService elasticsearchService, List<DetectionItem> detectionItemList) {
        this.detectionTaskMapper = detectionTaskMapper;
        this.carInfoMapper = carInfoMapper;
        this.transactionInfoMapper = transactionInfoMapper;
        this.userInfoMapper = userInfoMapper;
        this.detectionResultMapper = detectionResultMapper;
        this.reservationMapper = reservationMapper;
        this.transactionContractMapper = transactionContractMapper;
        this.elasticsearchService = elasticsearchService;
        this.detectionItemList = detectionItemList;
    }

    /**
     * 获取车检任务列表
     * 当前用户
     *
     * @return
     */
    @Override
    public List<DetectionTaskVo> getDetectionTaskList() {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: getCarPage()");
        }
        return getDetectionTaskList(userId);
    }

    /**
     * 获取车检任务列表
     * 指定用户
     *
     * @param consultantId 指定用户
     * @return
     */
    @Override
    public List<DetectionTaskVo> getDetectionTaskList(String consultantId) {
        List<DetectionTask> detectionTaskList = detectionTaskMapper.findByConsultantId(consultantId);
        List<DetectionTaskVo> detectionTaskVoList = new ArrayList<>();

        for (DetectionTask detectionTask : detectionTaskList) {

            //  组装车检任务
            String transId = detectionTask.transId();
            String carId = transactionInfoMapper.findCarIdByTransId(detectionTask.transId());
            String title = carInfoMapper.getConcatTitleById(carId);
            String address = detectionTask.address();
            String ownerPhone = transactionInfoMapper.findOwnerPhoneBytransId(transId);
            String note = detectionTask.note();
            LocalDate meetDate = detectionTask.meetDate();

            detectionTaskVoList.add(new DetectionTaskVo(transId, carId, title, address, ownerPhone, note, meetDate));
        }
        return detectionTaskVoList;
    }

    @Override
    public List<TransManageVo> getTransManageList() {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: getCarPage()");
        }
        return getTransManageList(userId);
    }

    @Override
    public List<TransManageVo> getTransManageList(String consultantId) {
        List<TransactionInfo> transInfoList = transactionInfoMapper.findUnfinishedByConsultantIdAndStatus(consultantId);
        List<TransManageVo> transManageVoList = new ArrayList<>();

        for (TransactionInfo transInfo : transInfoList) {
            List<Reservation> reservationList = reservationMapper.findByTrans(transInfo.getTransId());
            if (reservationList.size() > 0) {
                for (Reservation reservation : reservationList) {
                    String transId = transInfo.getTransId();
                    String bookerId = reservation.bookerId();
                    String address = reservation.address();
                    LocalDate meetDate = reservation.meetDate();
                    String ownerPhone = userInfoMapper.findPhoneById(transInfo.getOwnerId());
                    String bookerPhone = userInfoMapper.findPhoneById(reservation.bookerId());
                    Integer status = transInfo.getTransStatus();

                    transManageVoList.add(new TransManageVo(transId, bookerId, address, meetDate, ownerPhone,
                            bookerPhone, status));
                }
            }
        }

        return transManageVoList;
    }

    @Override
    public List<TransManageFinishedVo> getTransManageFinishedList() {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: getCarPage()");
        }
        return getTransManageFinishedList(userId);
    }

    @Override
    public List<TransManageFinishedVo> getTransManageFinishedList(String consultantId) {
        List<TransactionInfo> transInfoList = transactionInfoMapper.findFinishedByConsultantId(consultantId);
        List<TransManageFinishedVo> transManageVoList = new ArrayList<>();

        for (TransactionInfo transInfo : transInfoList) {
            String transId = transInfo.getTransId();
            String title = carInfoMapper.getConcatTitleById(transInfo.getCarId());
            String ownerPhone = userInfoMapper.findPhoneById(transInfo.getOwnerId());
            String buyerPhone = userInfoMapper.findPhoneById(transInfo.getBuyerId());
            String city = transInfo.getCity();
            LocalDateTime updateTime = transactionInfoMapper.findUpdateTimeByTransId(transId);

            transManageVoList.add(new TransManageFinishedVo(transId, title, ownerPhone, buyerPhone, city, updateTime));
        }

        return transManageVoList;
    }

    /**
     * 获取车检表单
     * 当前用户
     *
     * @param transId
     * @return
     */
    @Override
    public DetectionResultFormVo getDetectionForm(String transId) {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: getCarPage()");
        }
        return getDetectionForm(transId, userId);
    }

    /**
     * 获取车检表单
     * 指定用户
     *
     * @param transId
     * @param consultantId 指定用户
     * @return
     */
    @Override
    public DetectionResultFormVo getDetectionForm(String transId, String consultantId) {
        TransactionInfo transInfo = transactionInfoMapper.findByTransId(transId);
        CarInfo carInfo = carInfoMapper.findById(transInfo.getCarId());
        String ownerPhone = userInfoMapper.findPhoneById(transInfo.getOwnerId());
        List<DetectionResult> detectionResultList = new ArrayList<>();

        for (DetectionItem detectionItem : detectionItemList) {
            detectionResultList.add(new DetectionResult(transInfo.getTransId(), detectionItem.itemId(), consultantId,
                    0, "", null, null));
        }

        return new DetectionResultFormVo(consultantId, transInfo, carInfo, ownerPhone, detectionResultList);
    }

    @Override
    public void saveDetectionForm(DetectionResultFormVo detectionResultForm) {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: getCarPage()");
        }
        saveDetectionForm(detectionResultForm, userId);
    }

    @Override
    public void saveDetectionForm(DetectionResultFormVo detectionResultForm, String consultantId) {
        //  检测用户合法性
        if (!consultantId.equals(detectionResultForm.getConsultantId())) {
            log.debug("current consultantId: " + consultantId + " form consultantId: " + detectionResultForm.getConsultantId());
            throw new IllegalArgumentException("saveDetectionForm() error: consultantId illegal!");
        }

        TransactionInfo transInfo = detectionResultForm.getTransactionInfo();
        CarInfo carInfo = detectionResultForm.getCarInfo();
        List<DetectionResult> detectionResultList = detectionResultForm.getDetectionResultList();

        //  更新检测结果,预约地址和价格
        transactionInfoMapper.updateDetectionByTransId(transInfo.getAddress(), transInfo.getPrice(),
                transInfo.getTransId());
        //  更新车辆参数
        carInfoMapper.updateById(carInfo);
        //  添加detection_result
        if (detectionResultList != null) {
            for (DetectionResult detectionResult : detectionResultList) {
                if (Integer.valueOf(1).equals(detectionResult.getProblemLevel())) {
                    detectionResult.setConsultantId(consultantId);
                    detectionResult.setTransId(transInfo.getTransId());
                    detectionResultMapper.insert(detectionResult);
                }
            }
        }

    }

    /**
     * 保存上传的图片，并更新交易状态
     *
     * @param transId
     * @param files
     */
    @Override
    public void uploadPic(String transId, List<MultipartFile> files) throws IOException {
        String picDir = resource_path + ConstUtil.CAR_PIC_PREFIX;
        StringBuilder pictureBuilder = new StringBuilder(512);

        //  保存图片到本地
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);

            if (file.isEmpty()) {
                return;
            }
            String originalFilename = file.getOriginalFilename();
            int split = originalFilename.lastIndexOf(".");
            String suffix = originalFilename.substring(split); //".jpg"
            String fileName = transId + "-" + i + suffix;
            File dest = new File(picDir + fileName);

//            拼接数据库字符串
            pictureBuilder.append(i == 0 ? "" : ";");
            pictureBuilder.append(fileName);

            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //  保存到数据库
        transactionInfoMapper.updatePictureByTransId(pictureBuilder.toString(), transId);

        //  完成检测任务
        detectionTaskMapper.updateStatusByTransId(ConstUtil.DETEDTION_TASK_STATUS_FINISHED, transId);

        //  更新状态，发布到elasticsearch中
        String docId = releaseTrans(transId);
        log.debug("uploadPic() - new document id:\n" + docId);
    }

    @Override
    public void uploadContract(String transId, String bookerId, MultipartFile file) throws IOException {
        TransactionInfo transInfo = transactionInfoMapper.findByTransId(transId);

        //  处理file
        String filePath = resource_path + ConstUtil.CONTRACT_PREFIX;
        if(file.isEmpty()){
            return;
        }

        String originalFilename = file.getOriginalFilename();
        int split = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(split); //".jpg" ".png"
        String fileName = "contract-" + transId + suffix;
        File dest = new File(filePath+fileName);

        try {
//            保存文件到本地
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //  修改交易信息
        TransactionContract contract = new TransactionContract(transId, transInfo.getOwnerId(), bookerId, transInfo.getConsultantId(), fileName,
                null, null);

        //  添加合同
        transactionContractMapper.insert(contract);
        //  更改交易信息,状态设置为已签约,添加买家
        transactionInfoMapper.signedTrans(bookerId, ConstUtil.TRANS_STATUS_SIGNED, transId);
        //  从elasticsearch中删除
        elasticsearchService.deleteDocument(transId);
    }

    /**
     * 更新交易状态为发布中，将交易信息添加到elasticsearch中
     *
     * @param transId
     * @return
     */
    @Override
    public String releaseTrans(String transId) throws IOException {
        //  设置为发布中状态
        transactionInfoMapper.updateStatusByTransId(ConstUtil.TRANS_STATUS_RELEASED, transId);

        //  分配顾问
        assignTransConsultant(transId);

        //  添加到elasticsearch,返回docId
        return elasticsearchService.addDocument(transId);
    }

    @Override
    public void deleteReservation(String transId, String bookerId) {
        reservationMapper.deleteByKey(transId, bookerId);
    }

    @Override
    public void confirmReservation(String transId, String bookerId) {
        String address = transactionInfoMapper.findAddressByTransId(transId);
        reservationMapper.updateAddressByKey(address, transId, bookerId);
    }

    @Override
    public void confirmTransfer(String transId) {
         transactionInfoMapper.updateStatusByTransId(ConstUtil.TRANS_STATUS_TRANSFERRED, transId);
    }

    /**
     * 为发布的交易分配顾问
     *
     * @param transId
     */
    private void assignTransConsultant(String transId) {
        String consultantId = "11";
        transactionInfoMapper.updateConsultantByTransId(consultantId, transId);
    }
}
