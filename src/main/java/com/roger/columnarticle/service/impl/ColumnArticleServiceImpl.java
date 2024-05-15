package com.roger.columnarticle.service.impl;

import com.roger.columnarticle.entity.ColumnArticle;
import com.roger.columnarticle.repository.ColumnArticleRepository;
import com.roger.columnarticle.service.ColumnArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnArticleServiceImpl implements ColumnArticleService {

    /**
     * 自動裝配的 ColumnArticleRepository，用於執行專欄文章相關的資料庫操作。
     */
    @Autowired
    private ColumnArticleRepository columnArticleRepository;

    /**
     * 自動裝配的 StringRedisTemplate，用於執行與 Redis 資料庫操作，特別是針對字串類型的資料。
     */
    @Autowired
    @Qualifier("colStrStr")
    private StringRedisTemplate redisTemplate;

    /**
     * 查找所有專欄文章。
     */
    @Override
    public List<ColumnArticle> findAll() {
        return columnArticleRepository.findAll();
    }

    /**
     * 獲取指定文章編號的文章專欄。
     */
    @Override
    public ColumnArticle getOneColumnArticle(Integer artNo) {
        return columnArticleRepository.findColumnArticleByArtNo(artNo);
    }


    /**
     * 更新專欄文章的資料。
     */
    @Override
    public ColumnArticle edit(ColumnArticle newData) {
        return columnArticleRepository.save(newData);
    }

    /**
     * 更新現有的專欄文章。
     */
    @Override
    public ColumnArticle updateColumnArticle(ColumnArticle columnArticle) {
        return columnArticleRepository.save(columnArticle);
    }

    /**
     * 根據文章編號查找專欄文章。
     */
    @Override
    public ColumnArticle findColumnArticleByArtNo(Integer artNo) {
        return columnArticleRepository.findColumnArticleByArtNo(artNo);
    }

}
