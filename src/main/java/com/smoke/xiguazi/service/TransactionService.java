package com.smoke.xiguazi.service;

import com.smoke.xiguazi.model.vo.*;

import java.math.BigInteger;
import java.util.List;

public interface TransactionService {

    CarPageVo getCarPage(String transId);

    CarPageVo getCarPage(String transId, String userId);

    void sellCar(SellFormVo sellForm);

    void sellCar(SellFormVo sellForm, String ownerId);

    Integer addFavourite(String transId);

    Integer addFavourite(String transId, String userId);

    Integer removeFavourite(String transId);

    Integer removeFavourite(String transId, String userId);

    Integer addReservation(String transId, String meetDateStr);

    Integer addReservation(String transId, String meetDateStr, String userId);

    Integer deleteReservation(String transId);

    Integer deleteReservation(String transId, String bookerId);

    List<SellTransVo> getSellTransList();

    List<SellTransVo> getSellTransList(String ownerId);

    List<BuyTransVo> getBuyTransList();

    List<BuyTransVo> getBuyTransList(String buyerId);

    List<ReservationVo> getReservationList();

    List<ReservationVo> getReservationList(String bookerId);

    BigInteger countTransactionInfo();
}
