package com.ren.product.dao;

import com.ren.product.entity.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 在Spring Data Jpa裡面，共有以下幾個大類:
 * (1)prefix 前綴
 * 1.find:透過指定值查詢資料，可為單個Entity(由Optional包裹)也可為List
 * 2.get:與find類似，通常會設計成透過主鍵搜尋單個物件(非規定)
 * 3.search:通用搜尋方法，可以設計複雜的搜尋條件獲得欲搜索資料
 * 4.query:使用自定義的JPQL語法查詢，可執行更複雜的資料庫操作
 * 5.count:統計符合指定條件的實體數量
 * 6.delete:刪除符合指定條件的資料
 * 7.stream:以Stream的形式獲得指定條件的實體對象
 * 8.read:讀取資料庫內的數據，通常透過查詢實現，類似find方法
 * 9.exists:檢查是否存在符合指定條件的Entity
 * 10.update:更新符合指定條件的Entity
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

    /**
     * 全文搜索
     *
     * @param keyword 關鍵字
     * @param pageable 設定搜尋結果為第幾分頁、有幾筆資料，將搜尋結果以分頁呈現
     * @return 返回分頁搜尋結果
     */
    @Query("SELECT p FROM Product p WHERE CONCAT(p.productCategory.productCatNo, ' ', p.productCategory.productCatName, ' ', p.productName, ' ', p.productInfo, ' ', p.productColor) LIKE %?1%")
    @Transactional
    Page<Product> findAll(String keyword, Pageable pageable);

//    @Query(value = "SELECT * FROM product WHERE MATCH(productNo, productName, productInfo) " + "AGAINST (?1)", nativeQuery = true)
//    public Page<Product> search(String keyword, Pageable pageable);

    // 根據關鍵字搜尋產品
    @Transactional
    List<Product> findByProductNameContaining(String keyword);

    /**
     * 萬用搜尋
     *
     * @param keyword 關鍵字
     * @return 返回相關聯商品清單
     */
    @Query("SELECT p FROM Product p " +
            "LEFT JOIN p.productCategory pc " +
            "WHERE CAST(p.productNo AS string) LIKE %:keyword% " +
            "OR CAST(pc.productCatNo AS string) LIKE %:keyword% " +
            "OR p.productName LIKE %:keyword% " +
            "OR pc.productCatName LIKE %:keyword% " +
            "OR p.productInfo LIKE %:keyword% " +
            "OR p.productColor LIKE %:keyword%")
    @Transactional
    List<Product> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 獲得所有同一商品種類的商品清單
     *
     * @param productCatNo 商品類別編號
     * @return 返回商品清單
     */
    @Transactional
    List<Product> findProductsByProductCategory_ProductCatNo(Integer productCatNo);

    /**
     * 透過商品類別名稱搜尋所有商品
     *
     * @param productCatName 商品類別名稱
     * @return 返回商品清單
     */
    @Transactional
    List<Product> findProductsByProductCategory_ProductCatName(String productCatName);

    /**
     * 獲得所有上架商品(or 下架商品)
     *
     * @param productStat 商品狀態
     * @return 返回所有上架或下架商品清單
     */
    @Transactional
    List<Product> findByProductStat(Byte productStat);

    /**
     * 最新上架時間前10名
     *
     * @return 返回前10名的清單
     */
    @Transactional
    List<Product> findTop1OByOrderByProductOnShelfDesc();

    /**
     * 下架時間前10名
     *
     * @return 返回前10名的清單
     */
    @Transactional
    List<Product> findTop10ByOrderByProductOffShelfDesc();

    /**
     * 萬用複合查詢
     * 預計在前台使用標籤過濾搜尋結果使用
     *
     * @param productNo 商品標號
     * @param productCatNo 商品類別編號
     * @param productName 商品名稱
     * @param productInfo 商品資訊
     * @param productSize 商品尺寸
     * @param productColor 商品顏色
     * @param productPrice 商品單價
     * @param productStat 商品狀態
     * @param productOnShelf 商品上架時間
     * @param productOffShelf 商品下架時間
     * @return 返回符合查詢資格的清單
     */
    @Transactional
    @Query("SELECT p FROM Product p WHERE " +
            "(:productNo IS NULL OR p.productNo = :productNo) AND " +
            "(:productCatNo IS NULL OR p.productCategory.productCatNo = :productCatNo) AND " +
            "(:productName IS NULL OR p.productName = :productName) AND " +
            "(:productInfo IS NULL OR p.productInfo = :productInfo) AND " +
            "(:productSize IS NULL OR p.productSize = :productSize) AND " +
            "(:productColor IS NULL OR p.productColor = :productColor) AND " +
            "(:productPrice IS NULL OR p.productPrice = :productPrice) AND " +
            "(:productStat IS NULL OR p.productStat = :productStat) AND " +
            "(:productOnShelf IS NULL OR p.productOnShelf = :productOnShelf) AND " +
            "(:productOffShelf IS NULL OR p.productOffShelf = :productOffShelf)")
    List<Product> findByAttributes(
            @Param("productNo") Integer productNo,
            @Param("productCatNo") Integer productCatNo,
            @Param("productName") String productName,
            @Param("productInfo") String productInfo,
            @Param("productSize") Integer productSize,
            @Param("productColor") String productColor,
            @Param("productPrice") BigDecimal productPrice,
            @Param("productStat") Byte productStat,
            @Param("productOnShelf") Timestamp productOnShelf,
            @Param("productOffShelf") Timestamp productOffShelf
    );

    /**
     * 前台搜尋同一件商品(類別 + 名稱一樣)
     * 一件商品代表一件衣服
     *
     * @param productCatNo 商品類別
     * @param productName 商品名稱
     * @return 返回同一個商品(List)
     */
    @Transactional
    List<Product> findProductsByProductCategory_ProductCatNoAndProductName(Integer productCatNo, String productName);

    /**
     * 返回可選擇顏色的商品
     *
     * @param productCatNo
     * @param productName
     * @param productSize
     * @return
     */
    @Transactional
    List<Product> findProductsByProductCategory_ProductCatNoAndProductNameAndProductSize(Integer productCatNo, String productName, Integer productSize);

    /**
     * 找到同一個商品其他顏色的衣服
     * 同一商品定義(類別一樣、名稱一樣)
     *
     * @param productCatNo 商品類別
     * @param productName 商品名稱
     * @param productColor 商品顏色
     * @return 返回同一顏色商品(List)
     */
    @Transactional
    List<Product> findProductsByProductCategory_ProductCatNoAndProductNameAndProductColor(Integer productCatNo, String productName, String productColor);

    /**
     * 返回客人可購買的商品List(當客人在前台點擊商品時，及選好商品種類與商品名稱，
     * 而後根據喜好選size與顏色，此時應返回可以提供給顧客的商品)
     *
     * @param productCatNo 商品類別編號
     * @param productName 商品名稱
     * @param productColor 商品顏色
     * @param productSize 商品尺寸
     * @return 返回商品list
     */
    @Transactional
    List<Product> findProductsByProductCategory_ProductCatNoAndProductNameAndProductColorAndProductSize(Integer productCatNo, String productName, String productColor, Integer productSize);

    @Transactional
    List<Product> findProductsByProductColor(String productColor);

    @Transactional
    List<Product> findProductsByProductSize(Integer productSize);

    @Transactional
    List<Product> findProductsByProductPriceBetween(BigDecimal min, BigDecimal max);

    /**
     * 自定義更新商品上架狀態方法
     *
     * @param productNo 商品編號
     * @param productStat 更新商品狀態為上架
     * @param productOnShelf 更新商品上架時間
     */
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.productStat = :productStat, p.productOnShelf = :productOnShelf WHERE p.productNo = :productNo")
    void updateProductOnShelf(@Param("productNo") Integer productNo, @Param("productStat") Byte productStat, @Param("productOnShelf") Timestamp productOnShelf);

    /**
     * 自定義更新商品下架狀態方法
     *
     * @param productNo 商品編號
     * @param productStat 更新商品狀態為下架
     * @param productOffShelf 更新商品下架時間
     */
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.productStat = :productStat, p.productOffShelf = :productOffShelf WHERE p.productNo = :productNo")
    void updateProductOffShelf(@Param("productNo") Integer productNo, @Param("productStat") Byte productStat, @Param("productOffShelf") Timestamp productOffShelf);

    /**
     * 自定義批量更新商品上架方法
     *
     * @param productNos 商品編號
     * @param productStat 更新商品狀態為上架
     * @param productOnShelf 更新商品上架時間
     */
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.productStat = :productStat, p.productOnShelf = :productOnShelf WHERE p.productNo IN :productNos")
    void updateProductOnShelfBatch(@Param("productNos") List<Integer> productNos, @Param("productStat") Byte productStat, @Param("productOnShelf") Timestamp productOnShelf);

    /**
     * 自定義批量更新商品下架方法
     *
     * @param productNos 商品編號
     * @param productStat 更新商品狀態為下架
     * @param productOffShelf 更新商品下架時間
     */
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.productStat = :productStat, p.productOffShelf = :productOffShelf WHERE p.productNo IN :productNos")
    void updateProductOffShelfBatch(@Param("productNos") List<Integer> productNos, @Param("productStat") Byte productStat, @Param("productOffShelf") Timestamp productOffShelf);

    /**
     * 根據商品類別編號上架商品
     *
     * @param productCatNo 商品類別編號
     * @param productStat 更新商品狀態為上架
     * @param productOnShelf 更新商品上架時間
     */
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.productStat = :productStat, p.productOnShelf = :productOnShelf WHERE p.productCategory.productCatNo = :productCatNo")
    void updateProductOnShelfByCategoryNo(@Param("productCatNo") Integer productCatNo, @Param("productStat") Byte productStat, @Param("productOnShelf") Timestamp productOnShelf);

    /**
     * 根據商品類別編號下架商品
     *
     * @param productCatNo 商品類別編號
     * @param productStat 更新商品狀態為下架
     * @param productOffShelf 更新商品下架時間
     */
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.productStat = :productStat, p.productOffShelf = :productOffShelf WHERE p.productCategory.productCatNo = :productCatNo")
    void updateProductOffShelfByCategoryNo(@Param("productCatNo") Integer productCatNo, @Param("productStat") Byte productStat, @Param("productOffShelf") Timestamp productOffShelf);

    /**
     * 根據商品類別名稱上架
     *
     * @param productCatName 商品類別名稱
     * @param productStat 更新商品狀態為上架
     * @param productOnShelf 更新商品上架時間
     */
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.productStat = :productStat, p.productOnShelf = :productOnShelf WHERE p.productCategory.productCatName = :productCatName")
    void updateProductOnShelfByCategoryName(@Param("productCatName") String productCatName, @Param("productStat") Byte productStat, @Param("productOnShelf") Timestamp productOnShelf);

    /**
     * 根據商品類別名稱下架
     *
     * @param productCatName 商品類別名稱
     * @param productStat 更新商品狀態為下架
     * @param productOffShelf 更新商品下架時間
     */
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.productStat = :productStat, p.productOffShelf = :productOffShelf WHERE p.productCategory.productCatName = :productCatName")
    void updateProductOffShelfByCategoryName(@Param("productCatName") String productCatName, @Param("productStat") Byte productStat, @Param("productOffShelf") Timestamp productOffShelf);

    /**
     * 根據商品類別編號刪除商品
     *
     * @param productCatNo 商品類別編號
     */
    @Transactional
    @Modifying
    void deleteProductsByProductCategory_ProductCatNo(Integer productCatNo);

    /**
     * 根據商品類別名稱刪除商品
     *
     * @param productCatName 商品類別名稱
     */
    @Transactional
    @Modifying
    void deleteProductsByProductCategory_ProductCatName(String productCatName);


    @Query("SELECT p FROM Product p WHERE p.productStat = :productStat " +
            "AND (:#{#filters['color']} IS NULL OR p.productColor = :#{#filters['color']}) " +
            "AND (:#{#filters['size']} IS NULL OR p.productSize = :#{#filters['size']}) " +
            "AND (:#{#filters['price']} IS NULL OR (p.productPrice BETWEEN :#{#filters['priceLow']} AND :#{#filters['priceHigh']}))")
    @Transactional
    Page<Product> findFilteredProducts(@Param("filters") Map<String, String> filters,
                                       @Param("productStat") Byte productStat,
                                       Pageable pageable);

}
