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
}
