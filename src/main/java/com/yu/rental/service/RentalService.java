package com.yu.rental.service;

import com.yu.rental.entity.Rental;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RentalService {

    public Rental findByRentalName(String rentalName);//單筆查詢

    public List<Rental> findAllSortDESC(); //以rentalPrice查詢，金額由大到小

    public List<Rental> findAllSort(); //以rentalPrice查詢，金額由小到大

    public List<Rental> getRentalSize(Integer rentalSize); //以rentalSize查詢

    public List<Rental> getRentalColor(String rentalColor); //以rentalColor查詢

    public List<Rental> getRentalName(String rentalName); //以rentalName 做模糊查詢

    public Rental findByNo(Integer rentalNo);//單筆查詢

    public List<Rental> findAll();  //全部查詢(Rental)

    public List<Rental> searchRentals(Map<String, String[]> paramsMap); //複合查詢

    public Rental getOneRental(Integer rentalNo);

    public List<Rental> getOneRental(Map<String, Object> getOneRentalMap);

//    public List<RentalCategory> findAllRentalCat();  //全部查詢(RentalCategory)


    public Rental addRental(Rental rental); //新增

    public Rental updateRental(Rental rental); //修改


}
