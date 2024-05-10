package com.howard.rentalorderdetails.dao;

import com.changhoward.cia10108springboot.Entity.RentalOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
public interface RentalOrderDetailsRepository extends JpaRepository<RentalOrderDetails, RentalOrderDetails.CompositeDetail> {



    @Transactional
    @Query("SELECT d FROM RentalOrderDetails d WHERE " +
            "(:rentalOrdNo IS NULL OR d.compositeDetail.rentalOrdNo = :rentalOrdNo) AND " +
            "(:rentalNo IS NULL OR d.compositeDetail.rentalNo = :rentalNo) AND " +
            "(:rentalPrice IS NULL OR d.rentalPrice = :rentalPrice) AND " +
            "(:rentalDesPrice IS NULL OR d.rentalDesPrice = :rentalDesPrice)")
    List<RentalOrderDetails> findByAttributes(@Param("rentalOrdNo") Integer rentalOrdNo,
                                              @Param("rentalNo") Integer rentalNo,
                                              @Param("rentalPrice") BigDecimal rentalPrice,
                                              @Param("rentalDesPrice") BigDecimal rentalDesPrice);


}
