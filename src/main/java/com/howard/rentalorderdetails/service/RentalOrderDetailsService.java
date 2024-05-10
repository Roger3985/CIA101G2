package com.howard.rentalorderdetails.service;

import com.changhoward.cia10108springboot.Entity.RentalOrderDetails;

import java.util.List;

public interface RentalOrderDetailsService {

    /*   2024/05/01 紀錄
     *   共有5個方法，依照 增、刪、改、查 順序排列 (刪除沒做，複合查詢施工中)
     *
     *   1. insert : 新增一筆租借訂單明細
     *       參數 -> rentalordertails 物件
     *       回傳值 -> 無
     *
     *   2. update : 修改租借訂單明細資料
     *       參數 -> rentalordertailsrequest 物件 (dto)
     *       回傳值 -> 無
     *
     *   3. findById : 用 Id 查詢租借訂單明細資料
     *       參數 -> Integer 類型的 rOrdNo(租借訂單編號)、rNo(租借品編號)
     *       回傳值 -> rentalordertails 物件
     *
     *   4. getAll : 查詢所有租借訂單明細資料
     *       參數 -> 無
     *       回傳值 -> 裝有全部 rentalordertails 物件的 List
     *
     *   5. 複合查詢(名稱未定)
     *       參數 ->
     *       回傳值 ->
     *
     */

    public void insert(RentalOrderDetails rentalOrderDetails);

    public void update(RentalOrderDetails rod);

    public RentalOrderDetails findById(Integer rentalOrdNo, Integer rentalNo);

    public List<RentalOrderDetails> getAll();



}
