package com.yu.rentSet.dao;

import com.yu.rentSet.entity.RentSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RentSetRepository extends JpaRepository<RentSet, Long> {


    /**
     * 因繼承 JpaRepository，所以不需要實作任何方法，即可使用「新增、修改、刪除」等基本功能。
     */
    @Transactional
    public RentSet findByRentalOrder_RentalOrdNo(Integer rentalOrdNo); //rentalOrdNo查詢

    @Transactional
    public RentSet findByRentalSetName(String rentalSetName); //rentalSetName查詢

    @Transactional
    public RentSet findByRentalSetDays(Byte rentalSetDays); //單筆查詢(rentalSetDays)


    @Transactional
    @Query("SELECT rset FROM RentSet rset WHERE " +
            "(:rentalOrdNo IS NULL OR rset.rentalOrdNo = :rentalOrdNo) AND " +
            "(:rentalSetName IS NULL OR rset.rentalSetName = :rentalSetName) AND " +
            "(:rentalSetDays IS NULL OR rset.rentalSetDays = :rentalSetDays)")
    List<RentSet> searchRentSets(@Param("rentalOrdNo") Integer rentalOrdNo,
                                 @Param("rentalSetName") String rentalSetName,
                                 @Param("rentalSetDays") Byte rentalSetDays);
}

