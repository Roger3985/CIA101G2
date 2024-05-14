package com.yu.rental.dao;

import com.yu.rental.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    /**
     * 因繼承 JpaRepository，所以不需要實作任何方法，即可使用「新增、修改、刪除」等基本功能。
     * 注意：JpaRepository的泛型為 <T,ID>，所以在使用繼承時，必須定義好 T 與 ID 的型別，也就是 <MemberDTO, Long>。
     */
    @Transactional
    public Rental findByRentalNo(Integer rentalNo); //rentalNo查詢

    @Transactional
    public List<Rental> findByRentalCategoryRentalCatNo(Integer rentalCatNo); //rentalCatNo查詢

    @Transactional
    public Rental findByRentalName(String rentalName); //單筆查詢(String rentalName)


    //自定義查詢(for萬用，使用JPQL語法)
    @Query("SELECT re FROM Rental re WHERE re.rentalName LIKE %:rentalName%") //以rentalName 做模糊查詢
    List<Rental> findQueryByRentalName(@Param("rentalName") String rentalName);

    @Query("SELECT re FROM Rental re WHERE re.rentalSize = :rentalSize") //以rentalSize 查詢
    List<Rental> findQueryByRentalSize(@Param("rentalSize") Integer rentalSize);

    @Query("SELECT re FROM Rental re WHERE re.rentalColor LIKE %:rentalColor%") //以rentalColor 做模糊查詢
    Rental findQueryByRentalColor(@Param("rentalColor") String rentalColor);

    @Query("SELECT re FROM Rental re WHERE re.rentalInfo LIKE %:rentalInfo%") //以rentalInfo 做模糊查詢
    List<Rental> findQueryByRentalInfo(@Param("rentalInfo") String rentalInfo);

    @Query("SELECT re FROM Rental re WHERE re.rentalStat = :rentalStat") //以rentalStat 查詢
    List<Rental> findQueryByRentalStat(@Param("rentalStat") Byte rentalStat);


}