package com.roger.columnarticle.repository;

import com.roger.columnarticle.entity.ColumnArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用於訪問專欄文章及其相關回應的 Repository 介面。
 */
@Repository
public interface ColumnArticleRepository extends JpaRepository<ColumnArticle, Integer> {

    /**
     * 根據專欄文章編號查找專欄文章。
     * 此方法使用 JPQL 查找語句從資料庫中查找具有特定編號的專欄文章。
     *
     * @param artNo 專欄文章編號。
     * @return 與特定編號相對應的專欄文章物件，如果找不到則返回 null。
     */
    @Query("SELECT ca FROM ColumnArticle ca WHERE ca.artNo = :artNo")
    ColumnArticle findColumnArticleByArtNo(@Param("artNo") Integer artNo);

    /**
     * 判斷是否存在與指定的專欄文章編號相關聯的點讚記錄。
     *
     * @param artNo 專欄文章編號。
     * @return 如果存在與指定專欄文章編號相關聯的點讚記錄，則返回 true；否則返回 false。
     */
    public boolean existsByArtNo(Integer artNo);

    /**
     * 計算與特定專欄文章相關聯的回應數量。
     *
     * @param artNo 欲檢索回應數量的專欄文章ID
     * @return 與指定專欄文章相關聯的回應數量
     */
    @Query("SELECT COUNT(cr) FROM ColumnReply cr WHERE cr.columnArticle.artNo = :artNo")
    int countColumnArticleByArtNo(@Param("artNo") Integer artNo);

    /**
     * 根據 artStat 查詢上架中的文章。
     *
     * @param artStat 文章的狀態碼。0 表示上架，1 表示下架。
     * @return 符合指定狀態的文章列表。
     */
    List<ColumnArticle> findByArtStat(Byte artStat);

    /**
     * 根據文章狀態獲取上架中的專欄文章，並進行分頁處理。
     *
     * @param artStat  文章狀態，0 代表上架中。
     * @param pageable 用於分頁的 Pageable 物件。它包含分頁的資訊，如頁碼和每頁顯示的記錄數量。
     * @return 包含上架中專欄文章的分頁 Page 物件。這個物件包含了當前頁的專欄文章列表以及分頁資訊。
     */
    Page<ColumnArticle> findByArtStat(byte artStat, Pageable pageable);

    /**
     * 根據會員編號和文章編號刪除專欄文章收藏記錄。
     *
     * @param memNo 會員編號，不可為 null
     * @param artNo 文章編號，不可為 null
     * @throws IllegalArgumentException 如果會員編號或文章編號為 null
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM ArticleCollection ac WHERE ac.compositeArticleCollection.memNo = :memNo AND ac.compositeArticleCollection.artNo = :artNo")
    void deleteByMemNoAndArtNo(Integer memNo, Integer artNo);

    /**
     * 搜尋標題或內容包含給定關鍵字且狀態為指定值的 {@link ColumnArticle} 實體。
     * 搜尋不區分大小寫，並使用 SQL LIKE 運算子進行部分匹配。
     * 支持分頁。
     *
     * @param artStat 文章狀態。
     * @param keyword 要在文章標題和內容中搜尋的關鍵字。
     * @param pageable 分頁參數。
     * @return 符合搜尋條件的 {@link ColumnArticle} 實體的分頁結果。
     */
    @Query("SELECT c FROM ColumnArticle c WHERE c.artStat = :artStat AND (c.artTitle LIKE %:keyword% OR c.artContent LIKE %:keyword% OR c.administrator.admName LIKE %:keyword%)")
    Page<ColumnArticle> searchByKeywordAndStatus(@Param("artStat") byte artStat, @Param("keyword") String keyword, Pageable pageable);


}
