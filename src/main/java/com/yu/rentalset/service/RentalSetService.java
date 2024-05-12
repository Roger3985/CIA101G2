package com.yu.rentalset.service;

import com.yu.rentalset.entity.RentSet;

import java.util.List;
import java.util.Map;

    public interface RentalSetService {

        RentSet addRentalSet(String rSetName, Byte rSetDays);

        RentSet updateRentalSet(String rSetName, Byte rSetDays);

        RentSet findByRentalOrdNo(Integer rentalOrdNo);

        RentSet getOneRentalSet(Integer rOrdNo);//單筆查詢

        List<RentSet> getAll();  //萬用複合查詢


        List<RentSet> getByCompositeQuery(Map<String, String[]> map); //複合查詢

    }
