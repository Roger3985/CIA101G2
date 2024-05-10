package com.Cia101G2.howard.rentalmytrack.dao;

import com.Cia101G2.Entity.RentalMyTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Component
public interface RentalMyTrackRepository extends JpaRepository<RentalMyTrack, RentalMyTrack.CompositeTrack> {

    @Transactional
    @Query("SELECT t FROM RentalMyTrack t WHERE " +
            "(:rentalNo IS NULL OR t.compositeTrack.rentalNo = :rentalNo) AND " +
            "(:memNo IS NULL OR t.compositeTrack.memNo = :memNo) AND " +
            "(:rentalTrackTime IS NULL OR t.rentalTrackTime = :rentalTrackTime) AND" +
            "(:expRentalDate IS NULL OR t.expRentalDate = :expRentalDate)")
    List<RentalMyTrack>findByAttributes(@Param("rentalNo") Integer rentalNo,
                                        @Param("memNo") Integer memNo,
                                        @Param("rentalTrackTime") Timestamp rentalTrackTime,
                                        @Param("expRentalDate") Date expRentalDate);

}
