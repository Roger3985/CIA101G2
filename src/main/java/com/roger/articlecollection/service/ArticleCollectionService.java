package com.roger.articlecollection.service;

import com.roger.articlecollection.entity.ArticleCollection;

import java.util.List;

public interface ArticleCollectionService {

    /**
     * 查找所有專欄文章的收藏。
     *
     * @return 包含所有專欄文章收藏的 List<ArticleCollection> 列表。
     */
    public List<ArticleCollection> findAll();

    /**
     * 根據會員編號檢索與之相關的文章收藏列表。
     *
     * @param memNo 要檢索文章收藏的會員編號
     * @return 指定會員編號的文章收藏列表
     * @throws IllegalArgumentException 如果 memNo 為 null
     */
    public List<ArticleCollection> getArticleCollectionByMember(Integer memNo);


}
