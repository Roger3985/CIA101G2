//package com.yu.rentalset.dao;
//
//import com.yu.rentalset.entity.RentSet;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Repository
//    public interface RentSetRepository extends JpaRepository<RentSet, Integer> {
//
//
//    /**
//     * 因繼承 JpaRepository，所以不需要實作任何方法，即可使用「新增、修改、刪除」等基本功能。
//     * 注意：JpaRepository的泛型為 <T,ID>，所以在使用繼承時，必須定義好 T 與 ID 的型別，也就是 <MemberDTO, Long>。
//     */
//    @Transactional
//    public RentSet findByRentalOrdNo(Integer rentalOrdNo); //rentalOrdNo查詢
//
//    @Transactional
//    public RentSet findByRentalSetName(String rentalSetName); //rentalSetName查詢
//
//    @Transactional
//    public RentSet findByRentalSetDays(Byte rentalSetDays); //單筆查詢(rentalSetDays)
//
//
//
//
//    }
//
