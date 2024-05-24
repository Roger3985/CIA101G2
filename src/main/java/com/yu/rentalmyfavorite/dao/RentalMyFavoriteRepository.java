package com.yu.rentalmyfavorite.dao;

import com.yu.rentalmyfavorite.dto.AddToWishList;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RentalMyFavoriteRepository extends JpaRepository<RentalMyFavorite, RentalMyFavorite.CompositeRentalMyFavorite> {

    /**
     * 因繼承 JpaRepository，所以不需要實作任何方法，即可使用「新增、修改、刪除」等基本功能。
     * 注意：JpaRepository的泛型為 <T,ID>，所以在使用繼承時，必須定義好 T 與 ID 的型別，也就是 <___DTO, Long>。
     */
    @Transactional
    RentalMyFavorite findByRental_RentalNo(Integer rentalNo);  //rentalNo查詢

    @Transactional
    List<AddToWishList> findByMember_MemNo(Integer memNo); //memNo查詢

    @Transactional
    RentalMyFavorite findByRental_RentalNoAndMember_MemNo(Integer rentalNo,Integer memNo); //複合主鍵查詢

    @Transactional
    List<RentalMyFavorite> findByRentalRentalNoAndMemberMemNo(Integer rentalNo,Integer memNo); //複合主鍵查詢

    @Transactional
    List<RentalMyFavorite> findByRentalFavTime(Timestamp rentalFavTime); //rentalFavTime查詢

    @Transactional
    @Query(value = "SELECT * FROM RentalMyFavorite WHERE rentalNo = ?1", nativeQuery = true)
    List<RentalMyFavorite> findByCompositeKey(Integer rentalNo);

    //取得會員編號
    @Transactional
    @Query("SELECT FAV FROM RentalMyFavorite FAV WHERE FAV.member.memNo = ?1")
    List<RentalMyFavorite> getFAVByMemNo(Integer memNo);

    @Transactional
    @Query("SELECT FAV FROM RentalMyFavorite FAV WHERE " +
            "(:rentalNo IS NULL OR FAV.rental.rentalNo = :rentalNo) AND " +
            "(:memNo IS NULL OR FAV.member.memNo = :memNo) AND " +
            "(:rentalFavTime IS NULL OR FAV.rentalFavTime = :rentalFavTime)")
    List<RentalMyFavorite> searchRentalMyFAVs(@Param("rentalNo") Integer rentalNo,
                                              @Param("memNo") Integer memNo,
                                              @Param("rentalFavTime") Timestamp rentalFavTime);
}
