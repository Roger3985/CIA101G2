package com.yu.rental.service;

import com.yu.rental.entity.Rental;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RentalService {


    public List<Rental> findAll();  //全部查詢(Rental)

    public Rental findByNo(Integer rentalNo);//單筆查詢(rentalNo)

    public List<Rental> getOneRental(Map<String, Object> getOneRentalMap);	//單筆查詢(List集合)

    public Rental findByRentalName(String rentalName);//單筆查詢(rentalName)

    List<Rental> findByRentalColor(String rentalColor);  //處理查詢(依租借品的顏色)

    public List<Rental> getRentalName(String rentalName); //以rentalName 做模糊查詢

    public List<Rental> findAllSortDESC(); //以rentalPrice查詢，金額由大到小

    public List<Rental> findAllSort(); //以rentalPrice查詢，金額由小到大

//    public List<Rental> getRentalSize(Integer rentalSize); //以rentalSize查詢
//
//    public List<Rental> getRentalColor(String rentalColor); //以rentalColor查詢
//
//    public List<Rental> findByStat(Byte rentalStat);//單筆查詢(rentalStat) //以rentalStat查詢租借品狀態

    public List<Rental> findByRentalCatNoSortDESC(Integer rentalCatNo); //以rentalPrice查詢，金額由大到小 (依rentalCatNo處理)

    public List<Rental> findByRentalCatNoSort(Integer rentalCatNo); //以rentalPrice查詢，金額由小到大 (依rentalCatNo處理)

    List<Rental> findByRentalStatDESC(Byte rentalStat); //以rentalStat排序 (編號越晚的先顯示)

//----------------------------------------------------------------------------------------------------------------------
    //主要為後端使用：增查改

    public List<Rental> findByRentalCategoryRentalCatNo(Integer rentalCatNo); //rentalCatNo查詢(List集合)


    public Rental addRental(Rental rental); //新增

    public Rental updateRental(Rental rental); //修改

    public List<Rental> searchRentals(Map<String, Object> map); //複合查詢

    /**
     * 全文搜索
     *
     * @param keyword 關鍵字
     * @param page 指定搜尋結果為第幾頁
     * @param size 指定一頁要有多少筆資料
     * @return 返回分頁搜尋結果
     */
    Page<Rental> searchRentals(String keyword, Integer page, Integer size);


}
