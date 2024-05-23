package com.roger.articlecollection.repository;

import com.roger.articlecollection.entity.ArticleCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleCollectionRepository extends JpaRepository<ArticleCollection, Integer> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM ArticleCollection a WHERE a.compositeArticleCollection = :compositeArticleCollection")
    boolean existsByCompositeArticleCollection(@Param("compositeArticleCollection") ArticleCollection.CompositeArticleCollection compositeArticleCollection);

    /**
     * 根據指定的會員編號查找並檢索文章收藏列表。
     *
     * @param memNo 要搜索的會員編號
     * @return 指定會員編號的文章收藏列表
     */
    @Query("SELECT ac FROM ArticleCollection ac WHERE ac.member.memNo = :memNo")
    List<ArticleCollection> findArticleCollectionsByMember(@Param("memNo") Integer memNo);

    /**
     * 自定義查詢方法：通過會員編號和文章編號判斷是否存在 ArticleCollection 實體。
     *
     * @param memNo 會員編號。
     * @param artNo 文章編號。
     * @return 如果存在指定的 ArticleCollection 實體則返回 true，否則返回 false。
     */
    @Query("SELECT CASE WHEN COUNT(ac) > 0 THEN true ELSE false END FROM ArticleCollection ac WHERE ac.compositeArticleCollection.memNo = :memNo AND ac.compositeArticleCollection.artNo = :artNo")
    boolean existsByCompositeArticleCollection(Integer memNo, Integer artNo);
}
