package com.roger.columnarticle.repository;

import com.roger.columnarticle.entity.ColumnArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
