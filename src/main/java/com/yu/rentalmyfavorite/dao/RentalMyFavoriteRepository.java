package com.yu.rentalmyfavorite.dao;

import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface RentalMyFavoriteRepository extends
        JpaRepository<RentalMyFavorite, RentalMyFavorite.CompositeRentalMyFavorite> {

    /**
     * 因繼承 JpaRepository，所以不需要實作任何方法，即可使用「新增、修改、刪除」等基本功能。
     */
    @Transactional
    RentalMyFavorite findByRental_RentalNo(Integer rentalNo);  //rentalNo查詢

    @Transactional
    RentalMyFavorite findByRental_RentalNoAndMember_MemNo(Integer rentalNo,Integer memNo); //複合主鍵查詢

    @Transactional
    @Query(value = "SELECT * FROM RentalMyFavorite WHERE rentalNo = ?1", nativeQuery = true)
    List<RentalMyFavorite> findByCompositeKey(Integer rentalNo);

}
