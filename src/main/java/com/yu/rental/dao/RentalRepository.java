package com.yu.rental.dao;

import com.yu.rental.entity.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {

    /**
     * 因繼承 JpaRepository，所以不需要實作任何方法，即可使用「新增、修改、刪除」等基本功能。
     */

    @Transactional
    public Rental findByRentalNo(Integer rentalNo); //rentalNo查詢

    @Transactional
    public List<Rental> findByRentalCategoryRentalCatNo(Integer rentalCatNo); //rentalCatNo查詢

    @Transactional
    @Query("SELECT re FROM Rental re WHERE re.rentalSize = :rentalSize") //以rentalSize 查詢
    public List<Rental> findByRentalSize(@Param("rentalSize") Integer rentalSize);

    @Transactional
    @Query("SELECT r FROM Rental r WHERE r.rentalColor = :rentalColor")
    public List<Rental> findByRentalColor(@Param("rentalColor") String rentalColor); // 查詢顏色(String rentalColor)

    @Transactional
    @Query("SELECT re FROM Rental re WHERE :rentalCatNo IS NULL OR " +
            "re.rentalCategory.rentalCatNo = :rentalCatNo ORDER BY re.rentalPrice ASC")
    List<Rental> findByRentalCatNo_OrderByRentalPriceASC(Integer rentalCatNo);  //尋找對應編號的價格排序 (升冪)

    @Transactional
    @Query("SELECT re FROM Rental re WHERE :rentalCatNo IS NULL OR " +
            "re.rentalCategory.rentalCatNo = :rentalCatNo ORDER BY re.rentalPrice DESC")
    List<Rental> findByRentalCatNo_OrderByRentalPriceDESC(Integer rentalCatNo);  //尋找對應編號的價格排序 (降冪)

    @Transactional
    @Query("SELECT re FROM Rental re WHERE re.rentalStat = 0 ORDER BY re.rentalNo DESC") //以rentalStat排序 (編號越晚的先顯示)
    List<Rental> findByRentalStatDESC(Byte rentalStat);


    @Transactional
    @Query("SELECT re FROM Rental re WHERE " +
            "(:rentalNo IS NULL OR re.rentalNo = :rentalNo) AND " +
            "(:rentalCatNo IS NULL OR re.rentalCategory.rentalCatNo = :rentalCatNo) AND " +
            "(:rentalName IS NULL OR re.rentalName = :rentalName) AND " +
            "(:rentalPrice IS NULL OR re.rentalPrice = :rentalPrice) AND " +
            "(:rentalSize IS NULL OR re.rentalSize = :rentalSize) AND " +
            "(:rentalColor IS NULL OR re.rentalColor = :rentalColor) AND " +
            "(:rentalInfo IS NULL OR re.rentalInfo = :rentalInfo) AND " +
            "(:rentalStat IS NULL OR re.rentalStat = :rentalStat)")
    List<Rental> searchRentals(@Param("rentalNo") Integer rentalNo,
                               @Param("rentalCatNo") Integer rentalCatNo,
                               @Param("rentalName") String rentalName,
                               @Param("rentalPrice") BigDecimal rentalPrice,
                               @Param("rentalSize") Integer rentalSize,
                               @Param("rentalColor") String rentalColor,
                               @Param("rentalInfo") String rentalInfo,
                               @Param("rentalStat") Byte rentalStat);


    //自定義查詢(for萬用，使用JPQL語法)
    @Query("SELECT re FROM Rental re WHERE re.rentalName LIKE %:rentalName%") //以rentalName 做模糊查詢
    List<Rental> findQueryByRentalName(@Param("rentalName") String rentalName);

    @Transactional
    @Query("SELECT re FROM Rental re WHERE re.rentalColor = :rentalColor AND re.rentalSize = :rentalSize")
    List<Rental> findByColorAndSize(@Param("rentalColor") String color,
                                    @Param("rentalSize") Integer size);


    /**
     * 全文搜索
     *
     * @param keyword 關鍵字
     * @param pageable 設定搜尋結果為第幾分頁、有幾筆資料，將搜尋結果以分頁呈現
     * @return 返回分頁搜尋結果
     */
    @Query("SELECT re FROM Rental re WHERE CONCAT(re.rentalNo, ' ', re.rentalCategory.rentalCatNo, ' ', re.rentalCategory.rentalCatName, ' ', re.rentalName, ' ', re.rentalInfo, ' ', re.rentalColor) LIKE %?1%")
    Page<Rental> findAll(String keyword, Pageable pageable);



}