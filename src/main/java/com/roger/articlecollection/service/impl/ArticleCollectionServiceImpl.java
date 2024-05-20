package com.roger.articlecollection.service.impl;

import com.roger.articlecollection.entity.ArticleCollection;
import com.roger.articlecollection.repository.ArticleCollectionRepository;
import com.roger.articlecollection.service.ArticleCollectionService;
import com.roger.clicklike.entity.ClickLike;
import com.roger.clicklike.repository.ClickLikeRepository;
import com.roger.columnarticle.entity.ColumnArticle;
import com.roger.columnarticle.repository.ColumnArticleRepository;
import com.roger.member.entity.Member;
import com.roger.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ArticleCollectionServiceImpl implements ArticleCollectionService {

    /**
     * 自動裝配的 ArticleCollectionRepository，用於執行專欄文章收藏相關的資料庫操作。
     */
    @Autowired
    private ArticleCollectionRepository articleCollectionRepository;

    /**
     * 自動裝配的 ClickLikeRepository，用於執行專欄文章點讚相關的資料庫操作。
     */
    @Autowired
    private ClickLikeRepository clickLikeRepository;

    @Autowired
    private ColumnArticleRepository columnArticleRepository;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 自動裝配的 StringRedisTemplate，用於執行與 Redis 資料庫操作，特別是針對字串類型的資料。
     */
    @Autowired
    @Qualifier("colStrStr")
    private StringRedisTemplate redisTemplate;
    /**
     * 查找所有專欄文章的收藏。
     */
    @Override
    public List<ArticleCollection> findAll() {
        return articleCollectionRepository.findAll();
    }


}
