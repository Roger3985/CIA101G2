package com.yu.rentalcategory.dao;

import com.yu.rentalcategory.entity.RentalCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RentalCategoryRepository extends JpaRepository<RentalCategory, Long> {

    /**
     * 因繼承 JpaRepository，所以不需要實作任何方法，即可使用「新增、修改、刪除」等基本功能。
     * 注意：JpaRepository的泛型為 <T,ID>，所以在使用繼承時，必須定義好 T 與 ID 的型別，也就是 <MemberDTO, Long>。
     */
    @Transactional
    public RentalCategory findByRentalCatNo(Integer rentalCatNo);

    @Transactional
    public RentalCategory findByRentalCatName(String rentalCatName);



    //自定義查詢(使用JPQL語法)
    @Query("SELECT re FROM RentalCategory re WHERE re.rentalCatNo = :rentalCatNo")
    List<RentalCategory> findQueryByRentalCatNo(@Param("rentalCatNo") Integer rentalCatNo);

    @Query("SELECT re FROM RentalCategory re WHERE re.rentalCatName LIKE %:rentalCatName%")
    List<RentalCategory> findQueryByRentalCatName(@Param("rentalCatName") String rentalCatName);

    @Query("SELECT re FROM RentalCategory re WHERE re.rentalStockQty = :rentalStockQty")
    List<RentalCategory> findQueryByRentalStockQty(@Param("rentalStockQty") Integer rentalStockQty);

    @Query("SELECT re FROM RentalCategory re WHERE re.rentalRentedQty = :rentalRentedQty")
    List<RentalCategory> findQueryByRentalRentedQty(@Param("rentalRentedQty") Integer rentalRentedQty);


    @Query("SELECT re FROM RentalCategory re WHERE re.rentalDesPrice = :rentalDesPrice")
    List<RentalCategory> findQueryByRentalDesPrice(@Param("rentalDesPrice") BigDecimal rentalDesPrice);



}