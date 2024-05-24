package com.roger.clicklike.repository;

import com.roger.clicklike.entity.ClickLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClickLike 實體的資料庫存取介面。
 * 這個介面擴展了 JpaRepository，以繼承對 ClickLike 實體的基本 CRUD 操作。
 */
@Repository
public interface ClickLikeRepository extends JpaRepository<ClickLike, Integer> {

    /**
     * 查詢方法：通過複合主鍵查找 ClickLike 實體。
     *
     * @param compositeClickLike 複合主鍵對象。
     * @return 如果存在指定的 ClickLike 實體則返回 true，否則返回 false。
     */
    boolean findClickLikeByCompositeClickLikeLike(ClickLike.CompositeClickLike compositeClickLike);

    /**
     * 自定義查詢方法：通過複合主鍵判斷是否存在 ClickLike 實體。
     *
     * @param compositeClickLike 複合主鍵對象。
     * @return 如果存在指定的 ClickLike 實體則返回 true，否則返回 false。
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ClickLike c WHERE c.compositeClickLike = :compositeClickLike")
    boolean existsByCompositeClickLike(@Param("compositeClickLike") ClickLike.CompositeClickLike compositeClickLike);

    /**
     * 自定義查詢方法：通過會員編號和文章編號判斷是否存在 ClickLike 實體。
     *
     * @param memNo 會員編號。
     * @param artNo 文章編號。
     * @return 如果存在指定的 ClickLike 實體則返回 true，否則返回 false。
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ClickLike c WHERE c.compositeClickLike.memNo = :memNo AND c.compositeClickLike.artNo = :artNo")
    boolean existsByCompositeClickLike(@Param("memNo") int memNo, @Param("artNo") int artNo);

    /**
     * 根據會員編號和文章編號刪除 ClickLike 實體。
     *
     * @param memNo 會員編號。
     * @param artNo 文章編號。
     */
    @Transactional
    void deleteByCompositeClickLike_MemNoAndCompositeClickLike_ArtNo(int memNo, int artNo);

    /**
     * 根據會員編號查詢所有點讚紀錄。
     *
     * @param memNo 會員編號
     * @return 包含該會員所有點讚紀錄的列表
     */
    List<ClickLike> findByCompositeClickLike_MemNo(Integer memNo);

    List<ClickLike> findByCompositeClickLike_MemNoAndCompositeClickLike_ArtNo(Integer memNo, Integer artNo);

    /**
     * 根據文章編號計算按讚數量。
     *
     * @param artNo 文章編號。
     * @return 按讚數量。
     */
    @Query("SELECT COUNT(cl) FROM ClickLike cl WHERE cl.compositeClickLike.artNo = :artNo")
    int countByArtNo(Integer artNo);

}
