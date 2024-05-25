package com.yu.rentalmyfavorite;

import com.config.RedisConfig;
import com.yu.rentalmyfavorite.service.RentalMyFavoriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class myFAVTest {

    public static void main (String[] args){

//        ApplicationContext context = new AnnotationConfigApplicationContext(RedisConfig.class);
//        RentalMyFavoriteServiceImpl rentalMyFAVService = context.getBean(RentalMyFavoriteServiceImpl.class);
////        @Autowired  // 自動裝配
////        private RentalMyFavoriteServiceImpl rentalMyFAVService;
//
//
//
//        //測試rentalFAV 進 redis
//
//        // 預設資料
//        Integer memNo = 5566;
//        Integer rentalNo = 5001;
//        Timestamp rentalFavTime = new Timestamp(System.currentTimeMillis());
//
//        System.out.println("預設memNo: " + memNo);
//        System.out.println("預設rentalNo: " + rentalNo);
//        System.out.println("預設rentalFavTime: " + rentalFavTime);
//
//        Map<String, String> redisTest = new HashMap<>();
//        redisTest.put("rentalNo", String.valueOf(rentalNo));
//        redisTest.put("memNo", String.valueOf(memNo));
//        redisTest.put("rentalFavTime", rentalFavTime.toString());
//
//        System.out.println("要匯入的memNo: " + memNo);
//        System.out.println("要匯入的rentalNo: " + rentalNo);
//        System.out.println("要匯入的rentalFavTime: " + rentalFavTime);
//
//        rentalMyFAVService.addWish(String.valueOf(memNo), redisTest);
//        System.out.println("抓到redisTest (我是去redis)：" + redisTest);
    }
}
