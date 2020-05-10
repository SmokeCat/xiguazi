package com.smoke.xiguazi.service.impl;

import com.smoke.xiguazi.mapper.*;
import com.smoke.xiguazi.model.factory.SellTransVo0Factory;
import com.smoke.xiguazi.model.factory.SellTransVo1Factory;
import com.smoke.xiguazi.model.factory.SellTransVo2Factory;
import com.smoke.xiguazi.model.po.*;
import com.smoke.xiguazi.model.vo.*;
import com.smoke.xiguazi.security.MobileAuthenticationToken;
import com.smoke.xiguazi.service.TransactionService;
import com.smoke.xiguazi.utils.ConstUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionInfoMapper transactionInfoMapper;
    private final CarInfoMapper carInfoMapper;
    private final UserInfoMapper userInfoMapper;
    private final DetectionResultMapper detectionResultMapper;
    private final ReservationMapper reservationMapper;
    private final UserFavouriteMapper userFavouriteMapper;
    private final DetectionTaskMapper detectionTaskMapper;
    private final TransactionContractMapper transactionContractMapper;

    Map<String, String> carMakeMap;
    Map<String, String> cityMap;


    @Autowired
    public TransactionServiceImpl(TransactionInfoMapper transactionInfoMapper, CarInfoMapper carInfoMapper,
                                  UserInfoMapper userInfoMapper, DetectionResultMapper detectionResultMapper,
                                  ReservationMapper reservationMapper, UserFavouriteMapper userFavouriteMapper,
                                  DetectionTaskMapper detectionTaskMapper,
                                  TransactionContractMapper transactionContractMapper,
                                  Map<String, String> carMakeMap, Map<String, String> cityMap) {
        this.transactionInfoMapper = transactionInfoMapper;
        this.carInfoMapper = carInfoMapper;
        this.userInfoMapper = userInfoMapper;
        this.detectionResultMapper = detectionResultMapper;
        this.reservationMapper = reservationMapper;
        this.userFavouriteMapper = userFavouriteMapper;
        this.detectionTaskMapper = detectionTaskMapper;
        this.transactionContractMapper = transactionContractMapper;

        this.carMakeMap = carMakeMap;
        this.cityMap = cityMap;
    }

    /**
     * 获取车辆信息页数据
     * 当前用户
     *
     * @param transId
     * @return
     */
    @Override
    public CarPageVo getCarPage(String transId) {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: getCarPage()");
        }
        return getCarPage(transId, userId);
    }

    /**
     * 获取车辆信息页数据
     * 指定用户
     *
     * @param transId
     * @param userId  指定用户
     * @return
     */
    @Override
    public CarPageVo getCarPage(String transId, String userId) {
        //  获取相关信息
        TransactionInfo transInfo = transactionInfoMapper.findByTransId(transId);
        CarInfo carInfo = carInfoMapper.findById(transInfo.getCarId());

        //  获取detection_result Map
        List<DetectionResult> detectionResults = detectionResultMapper.findByTransId(transId);
        Map<Integer, DetectionResult> detectionResultMap = new HashMap<>();
        for (DetectionResult result : detectionResults) {
            detectionResultMap.put(result.getItemId(), result);
        }

        //  如果车辆已下架，添加下架图片
        if (transInfo.getTransStatus() != 1) {
            String carPicture = ConstUtil.DISABLED_CAR_PICTURE + ";" + transInfo.getCarPicture();
            transInfo.setCarPicture(carPicture);
        }

        //  组装结果
        return new CarPageVo(transInfo, carInfo, detectionResultMap,
                userInfoMapper.findPhoneById(transInfo.getConsultantId()), transInfo.getTransStatus(),
                isInFavourite(transId, userId), isReserved(transId, userId));
    }

    @Override
    public void sellCar(SellFormVo sellForm) {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: getCarPage()");
        }
        sellCar(sellForm, userId);
    }

    @Override
    public void sellCar(SellFormVo sellForm, String ownerId) {
        String make = carMakeMap.get(sellForm.make());
        String city = cityMap.get(sellForm.city());
        Long mileage = Long.valueOf(sellForm.mileage());

        String[] meetDateArray = sellForm.meetDate().split("-");
        Integer year = Integer.valueOf(meetDateArray[0]);
        Integer month = Integer.valueOf(meetDateArray[1]);
        Integer day = Integer.valueOf(meetDateArray[2]);
        LocalDate meetDate = LocalDate.of(year, month, day);

        //  添加车辆
        CarInfo carInfo = new CarInfo(ownerId, make, sellForm.model(), sellForm.licensePlateYear(), mileage);
        carInfoMapper.insert(carInfo);

        //  添加交易
        TransactionInfo transInfo = new TransactionInfo(carInfo.getCarId(), ownerId, city);
        transactionInfoMapper.insert(transInfo);

        //  分配车检员
        String consultantId = assignDetectionConsultant(transInfo.getTransId());
        DetectionTask detectionTask = new DetectionTask(transInfo.getTransId(), consultantId, sellForm.address(), meetDate, null,
                ConstUtil.DETEDTION_TASK_STATUS_ASSIGNED, null, null);
        detectionTaskMapper.insert(detectionTask);
    }

    /**
     * 添加收藏
     * 当前用户
     *
     * @param transId
     * @return
     */
    @Override
    public Integer addFavourite(String transId) {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: addFavourite()");
        }
        return addFavourite(transId, userId);
    }

    /**
     * 添加收藏
     * 指定用户
     *
     * @param transId
     * @param userId  指定用户
     * @return
     */
    @Override
    public Integer addFavourite(String transId, String userId) {
        return userFavouriteMapper.insert(userId, transId);
    }

    /**
     * 移除收藏
     * 当前用户
     *
     * @param transId
     * @return
     */
    @Override
    public Integer removeFavourite(String transId) {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: removeFavourite()");
        }
        return removeFavourite(transId, userId);
    }

    /**
     * 移除收藏
     * 指定用户
     *
     * @param transId
     * @param userId  指定用户
     * @return
     */
    @Override
    public Integer removeFavourite(String transId, String userId) {
        return userFavouriteMapper.deleteByKey(userId, transId);
    }

    /**
     * 用户添加看车预约
     * 当前用户
     *
     * @param transId
     * @param meetDateStr
     * @return
     */
    @Override
    public Integer addReservation(String transId, String meetDateStr) {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: addReservation()");
        }
        return addReservation(transId, meetDateStr, userId);
    }

    /**
     * 用户添加看车预约
     * 指定用户
     *
     * @param transId
     * @param meetDateStr
     * @param userId      指定用户
     * @return
     */
    @Override
    public Integer addReservation(String transId, String meetDateStr, String userId) {
        //  预约时间 类型转换
        String[] meetDateArray = meetDateStr.split("-");
        Integer year = Integer.valueOf(meetDateArray[0]);
        Integer month = Integer.valueOf(meetDateArray[1]);
        Integer day = Integer.valueOf(meetDateArray[2]);
        LocalDate meetDate = LocalDate.of(year, month, day);

        return reservationMapper.insert(transId, userId, meetDate);
    }

    /**
     * 删除看车预约
     * 当前用户
     *
     * @param transId
     * @return
     */
    @Override
    public Integer deleteReservation(String transId) {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: addReservation()");
        }
        return deleteReservation(transId, userId);
    }

    /**
     * 删除看车预约
     * 指定用户
     *
     * @param transId
     * @param bookerId 指定用户
     * @return
     */
    @Override
    public Integer deleteReservation(String transId, String bookerId) {
        Integer result = reservationMapper.deleteByKey(transId, bookerId);
        return result;
    }

    /**
     * 获取出售交易
     * 当前用户
     *
     * @return
     */
    @Override
    public List<SellTransVo> getSellTransList() {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: addReservation()");
        }
        return getSellTransList(userId);
    }

    /**
     * 获取出售交易
     * 指定用户
     *
     * @param ownerId 指定用户
     * @return
     */
    @Override
    public List<SellTransVo> getSellTransList(String ownerId) {
        List<TransactionInfo> transInfoList = transactionInfoMapper.findByOwnerId(ownerId);
        List<SellTransVo> sellTransVoList = new ArrayList<>();

        for (TransactionInfo transInfo : transInfoList) {
            SellTransVo sellTransVo = switch (transInfo.getTransStatus()) {
                case 0 -> new SellTransVo0Factory().create(transInfo, carInfoMapper, detectionTaskMapper, userInfoMapper);
                case 1 -> new SellTransVo1Factory().create(transInfo, carInfoMapper, detectionTaskMapper, userInfoMapper);
                case 2, 3 -> new SellTransVo2Factory().create(transInfo, carInfoMapper, detectionTaskMapper, userInfoMapper);
                default -> null;
            };
            sellTransVoList.add(sellTransVo);
        }
        return sellTransVoList;
    }

    /**
     * 获取购买交易
     * 当前用户
     *
     * @return
     */
    @Override
    public List<BuyTransVo> getBuyTransList() {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: addReservation()");
        }
        return getBuyTransList(userId);
    }

    /**
     * 获取购买交易
     * 指定用户
     *
     * @param buyerId 指定用户
     * @return
     */
    @Override
    public List<BuyTransVo> getBuyTransList(String buyerId) {
        List<TransactionInfo> transInfoList = transactionInfoMapper.findByBuyerId(buyerId);
        List<BuyTransVo> buyTransVoList = new ArrayList<>();

        for (TransactionInfo transInfo : transInfoList) {
            String transId = transInfo.getTransId();
            String price = transInfo.getPrice().toString();
            String carPicture = transInfo.getTransStatus() == 1 ? transInfo.getCarPicture().split(";")[0] : ConstUtil.DISABLED_CAR_PICTURE;
            String title = String.join(" ", carInfoMapper.getConcatTitleById(transInfo.getCarId()), transInfo.getCity());
            String status = transInfo.getTransStatus().toString();
            String transConsultantPhone = userInfoMapper.findPhoneById(transInfo.getConsultantId());
            String ownerPhone = userInfoMapper.findPhoneById(transInfo.getOwnerId());
            LocalDateTime buyTime = transactionContractMapper.findCreateTimeByTransId(transId);

            buyTransVoList.add(new BuyTransVo(transId, price, carPicture, title, status, transConsultantPhone,
                    ownerPhone, buyTime));
        }
        return buyTransVoList;
    }

    /**
     * 获取看车预约
     * 当前用户
     *
     * @return
     */
    @Override
    public List<ReservationVo> getReservationList() {
        //  获取当前用户user_id
        String userId =
                SecurityContextHolder.getContext().getAuthentication() instanceof MobileAuthenticationToken token ?
                        token.getId() : "";
        if ("".equals(userId)) {
            log.error("user_id error: addReservation()");
        }
        return getReservationList(userId);
    }

    /**
     * 获取看车预约
     * 指定用户
     *
     * @param bookerId 指定用户
     * @return
     */
    @Override
    public List<ReservationVo> getReservationList(String bookerId) {
        List<TransactionInfo> transInfoList = transactionInfoMapper.findByBookerId(bookerId);
        List<ReservationVo> reservationVoList = new ArrayList<>();

        for (TransactionInfo transInfo : transInfoList) {
            CarInfo carInfo = carInfoMapper.findById(transInfo.getCarId());
            Reservation reservation = reservationMapper.findByKey(transInfo.getTransId(), bookerId);

            String transId = transInfo.getTransId();
            String price = transInfo.getPrice().toString();
            String carPicture = transInfo.getTransStatus() == 1 ? transInfo.getCarPicture().split(";")[0] : ConstUtil.DISABLED_CAR_PICTURE;
            String title = String.join(" ", carInfo.getMake(), carInfo.getModel(), transInfo.getCity());
            String status = transInfo.getTransStatus().toString();
            String address = reservation.address();
            LocalDate meetDate = reservation.meetDate();
            String ownerPhone = userInfoMapper.findPhoneById(transInfo.getOwnerId());
            String transConsultantPhone = userInfoMapper.findPhoneById(transInfo.getConsultantId());
            LocalDateTime createTime = transInfo.getCreateTime();

            reservationVoList.add(new ReservationVo(transId, price, carPicture, title, status, address, meetDate,
                    ownerPhone,
                    transConsultantPhone,
                    createTime));
        }
        return reservationVoList;
    }

    /**
     * 获取交易数量
     *
     * @return
     */
    @Override
    public BigInteger countTransactionInfo() {
        return transactionInfoMapper.countAll();
    }

    /**
     * 分配车检员
     * @param transId
     */
    private String assignDetectionConsultant(String transId) {
        return "10";
    }

    /**
     * 判断是否已经收藏
     *
     * @param transId
     * @param userId
     * @return
     */
    private Boolean isInFavourite(String transId, String userId) {
        Integer result = userFavouriteMapper.countByKey(userId, transId);
        return result.equals(1);
    }

    /**
     * 判断是否已经预约看车
     *
     * @param transId
     * @param userId
     * @return
     */
    private Boolean isReserved(String transId, String userId) {
        Integer result = reservationMapper.countByKey(transId, userId);
        return result.equals(1);
    }

}
