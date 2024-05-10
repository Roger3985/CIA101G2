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
}
