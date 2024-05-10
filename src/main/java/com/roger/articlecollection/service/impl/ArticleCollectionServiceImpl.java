package com.roger.articlecollection.service.impl;

import com.roger.articlecollection.entity.ArticleCollection;
import com.roger.articlecollection.repository.ArticleCollectionRepository;
import com.roger.articlecollection.service.ArticleCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleCollectionServiceImpl implements ArticleCollectionService {

    /**
     * 自動裝配的 ArticleCollectionRepository，用於執行專欄文章收藏相關的資料庫操作。
     */
    @Autowired
    private ArticleCollectionRepository articleCollectionRepository;

    /**
     * 自動裝配的 StringRedisTemplate，用於執行與 Redis 資料庫操作，特別是針對字串類型的資料。
     */
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 查找所有專欄文章的收藏。
     */
    @Override
    public List<ArticleCollection> findAll() {
        return articleCollectionRepository.findAll();
    }
}
