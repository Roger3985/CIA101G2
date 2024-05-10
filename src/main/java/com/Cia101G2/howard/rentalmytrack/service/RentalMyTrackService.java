package com.Cia101G2.howard.rentalmytrack.service;

import com.Cia101G2.Entity.RentalMyTrack;

import java.util.List;

public interface RentalMyTrackService {

   /*   2024/05/01 紀錄
    *   共有5個方法，依照 增、刪、改、查 順序排列 (刪除沒做，複合查詢施工中)
    *
    *   1. insert : 新增一筆租借品追蹤
    *       參數 -> rentalmytrack 物件
    *       回傳值 -> 無
    *
    *   2. update : 修改租借品追蹤資料
    *       參數 -> rentalmytrackrequest_put 物件 (dto)
    *       回傳值 -> 無
    *
    *   3. findById : 用 Id 查詢租借品追蹤資料
    *       參數 -> Integer 類型的 rentalNo(租借品編號)、memNo(會員編號)
    *       回傳值 -> rentalmytrack 物件
    *
    *   4. getAll : 查詢所有租借品追蹤資料
    *       參數 -> 無
    *       回傳值 -> 裝有全部 rentalmytrack 物件的 List
    *
    *   5. 複合查詢(名稱未定)
    *       參數 ->
    *       回傳值 ->
    *
   */

    public void insert(RentalMyTrack rentalMyTrack);

    public void update(RentalMyTrack rmt);

    public RentalMyTrack findById(Integer rentalNo, Integer memNo);

    public List<RentalMyTrack> getAll();

}
