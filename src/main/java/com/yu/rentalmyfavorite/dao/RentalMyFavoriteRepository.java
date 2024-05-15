package com.yu.rentalmyfavorite.dao;

import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

    @Repository
    public interface RentalMyFavoriteRepository extends JpaRepository<RentalMyFavorite, Long> {

        /**
         * 因繼承 JpaRepository，所以不需要實作任何方法，即可使用「新增、修改、刪除」等基本功能。
         * 注意：JpaRepository的泛型為 <T,ID>，所以在使用繼承時，必須定義好 T 與 ID 的型別，也就是 <MemberDTO, Long>。
         */
        @Transactional
        public RentalMyFavorite findByRentalNo(Integer rentalNo);  //rentalNo查詢

        @Transactional
        public RentalMyFavorite findByMemNo(Integer memNo); //memNo查詢

        @Transactional
        public RentalMyFavorite findByIdRentalNoAndIdMemNo(Integer rentalNo,Integer memNo); //複合主鍵查詢

        @Transactional
        public RentalMyFavorite findByRentalFavTime(DateTimeFormat rentalFavTime); //rentalFavTime查詢


        //        //自定義查詢(使用JPQL語法)
    //        @Query("SELECT re FROM RentalMyFavorite re WHERE re.rentalCatNo = :rentalCatNo")
    //        List<RentalMyFavorite> findQueryByRentalCatNo(@Param("rentalCatNo") Integer rentalCatNo);
    //
    //        @Query("SELECT re FROM RentalMyFavorite re WHERE re.rentalCatName LIKE %:rentalCatName%")
    //        List<RentalMyFavorite> findQueryByRentalCatName(@Param("rentalCatName") String rentalCatName);
    //
    //        @Query("SELECT re FROM RentalMyFavorite re WHERE re.rentalStockQty = :rentalStockQty")
    //        List<RentalMyFavorite> findQueryByRentalStockQty(@Param("rentalStockQty") Integer rentalStockQty);
    //
    //        @Query("SELECT re FROM RentalMyFavorite re WHERE re.rentalRentedQty = :rentalRentedQty")
    //        List<RentalMyFavorite> findQueryByRentalRentedQty(@Param("rentalRentedQty") Integer rentalRentedQty);
    //
    //
    //        @Query("SELECT re FROM RentalMyFavorite re WHERE re.rentalDesPrice = :rentalDesPrice")
    //        List<RentalMyFavorite> findQueryByRentalDesPrice(@Param("rentalDesPrice") BigDecimal rentalDesPrice);

    }
