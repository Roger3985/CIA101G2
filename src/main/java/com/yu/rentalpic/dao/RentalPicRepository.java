package com.yu.rentalpic.dao;

import com.yu.rentalpic.entity.RentalPic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface RentalPicRepository extends JpaRepository<RentalPic, Long> {

    /**
     * 因繼承 JpaRepository，所以不需要實作任何方法，即可使用「新增、修改、刪除」等基本功能。
     * 注意：JpaRepository的泛型為 <T,ID>，所以在使用繼承時，必須定義好 T 與 ID 的型別，也就是 <MemberDTO, Long>。
     */
    @Transactional
    public RentalPic findByRentalPicNo(Integer rentalPicNo); //rentalPicNo查詢

    @Transactional
    public RentalPic findByRentalRentalNo(Integer rentalNo); //rentalNo查詢

    @Transactional
    @Query("UPDATE RentalPic SET rentalFile = :rentalFile WHERE rentalPicNo = :rentalPicNo")
    public void updateRentalFileById(@Param("rentalPicNo") Integer rentalPicNo, @Param("rentalFile") byte[] rentalFile);

    @Transactional
    @Query("SELECT p FROM RentalPic p WHERE " +
            "(:rentalPicNo IS NULL OR p.rentalPicNo = :rentalPicNo) AND " +
            "(:rentalNo IS NULL OR p.rental.rentalNo = :rentalNo) AND " +
            "(:rentalFile IS NULL OR p.rentalFile = :rentalFile)")
    List<RentalPic> searchRentalPics(@Param("rentalPicNo") Integer rentalPicNo,
                                     @Param("rentalNo") Integer rentalNo,
                                     @Param("rentalFile") byte[] rentalFile);
}