package com.ren.product.dao;

import com.ren.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 在Spring Data Jpa裡面，共有以下幾個大類:
 * (1)prefix 前綴
 * 1.find:透過指定值查詢資料，可為單個Entity(由Optional包裹)也可為List
 * 2.get:與find類似，通常會設計成透過主鍵搜尋單個物件(非規定)
 * 3.search:
 * 4.query:使用自定義的JPQL語法查詢，可執行更複雜的資料庫操作
 * 5.count:
 * 6.delete:
 * 7.stream:
 * 8.read:
 * 9.exists:
 * 10.update:
 * (2)suffix 後綴
 * 1.By:依照指定條件搜尋
 * 2.Distinct:返回不重複的結果
 * 3.OrderBy:排序
 * 4.And:串聯多個條件，且條件皆須符合
 * 5.Or:串聯多個條件，只需一個符合
 * 6.IgnoreCase:忽略大小寫進行匹配
 * 7.Before:查找指定日期之前
 * 8.After:查找指定日期之後
 * 9.In:查找指定屬性值
 * 10.NotIn:In的相反
 * 11.Null:查找空值的資料
 * 12.NotNull:查找非空值的資料
 * 13.Like:查找指定字符串的資料
 * 14.NotLike:Like相反
 * 15.StartingWith:查找指定字符串開頭的資料
 * 16.EndingWith:查找指定字符串結尾的資料
 * 17.Containing:查找包含指定字符串的資料
 * 18.Is:別名，用於指定屬性值
 * 19.Equals:查找等於指定屬性值的資料
 * 20.Not:查找不等於指定屬性值的資料
 * 21.Between:查找範圍內的資料
 * 22.LessThan:查找小於指定值的資料
 * 23.LessThanOrEqual:查找小於等於指定值的資料
 * 24.GreaterThan:查找大於指定值的資料
 * 25.GreaterThanOrEqual:查找大於等於指定值的資料
 *
 * 可根據上述詞綴組合自定義方法查找指定資料
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // 根據關鍵字搜尋產品
    @Transactional
    List<Product> findByProductNameContaining(String keyword);
    @Transactional
    List<Product> findByProductNameContainingAndProductCategoryProductCatNo(String keyword, Integer productCatNo);

    /**
     * 獲得所有同一商品種類的商品清單
     *
     * @param productCatNo
     * @return
     */
    @Transactional
    List<Product> findProductsByProductCategory_ProductCatNo(Integer productCatNo);

    /**
     * 獲得所有上架商品(or 下架商品)
     *
     * @param productStat
     * @return
     */
    @Transactional
    List<Product> findByProductStat(Byte productStat);

    /**
     * 獲得評分前10名的商品
     *
     * @return
     */
    @Transactional
    List<Product> findTop10ByOrderByProductComScoreDesc();

    // 最多人評價
    @Transactional
    List<Product> findTop10ByOrderByProductComPeople();

    // 最新上架
    @Transactional
    List<Product> findTop1OByOrderByProductOnShelfDesc();

    // 最新下架
    @Transactional
    List<Product> findTop10ByOrderByProductOffShelf();

    @Transactional
    @Modifying
    @Query("SELECT p FROM Product p WHERE " +
//            "(:productNo IS NULL OR p.productNo = :productNo) AND " +
//            "(:productCatNo IS NULL OR p.productCatNo = :productCatNo) AND " +
            "(:productName IS NULL OR p.productName = :productName) AND " +
            "(:productInfo IS NULL OR p.productInfo = :productInfo) AND " +
            "(:productSize IS NULL OR p.productSize = :productSize) AND " +
            "(:productColor IS NULL OR p.productColor = :productColor) AND " +
            "(:productPrice IS NULL OR p.productPrice = :productPrice) AND " +
            "(:productStat IS NULL OR p.productStat = :productStat) AND " +
            "(:productSalQty IS NULL OR p.productSalQty = :productSalQty) AND " +
            "(:productComPeople IS NULL OR p.productComPeople = :productComPeople) AND " +
            "(:productComScore IS NULL OR p.productComScore = :productComScore)")
    List<Product> findByAttributes(
//            @Param("productNo") Integer productNo,
//            @Param("productCatNo") Integer productCatNo,
            @Param("productName") String productName,
            @Param("productInfo") String productInfo,
            @Param("productSize") Integer productSize,
            @Param("productColor") String productColor,
            @Param("productPrice") BigDecimal productPrice,
            @Param("productStat") Byte productStat,
            @Param("productSalQty") Integer productSalQty,
            @Param("productComPeople") Integer productComPeople,
            @Param("productComScore") Integer productComScore
    );

}
