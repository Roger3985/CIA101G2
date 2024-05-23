package com.roger.columnarticle.service.impl;

import com.roger.articlecollection.entity.ArticleCollection;
import com.roger.articlecollection.repository.ArticleCollectionRepository;
import com.roger.clicklike.entity.ClickLike;
import com.roger.clicklike.repository.ClickLikeRepository;
import com.roger.columnarticle.entity.ColumnArticle;
import com.roger.columnarticle.repository.ColumnArticleRepository;
import com.roger.columnarticle.service.ColumnArticleService;
import com.roger.member.entity.Member;
import com.roger.member.repository.MemberRepository;
import com.roger.member.service.MemberService;
import com.roger.notice.entity.Notice;
import com.roger.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
public class ColumnArticleServiceImpl implements ColumnArticleService {

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
    private MemberRepository memberRepository;

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

    @Autowired
    private MemberService memberService;

    @Autowired
    private NoticeService noticeService;

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


    /**
     * 將文章添加到會員的收藏中。
     *
     * @param memNo 會員編號
     * @param artNo 文章編號
     * @return 如果成功添加到收藏中則返回 true，否則返回 false
     */
    @Override
    public boolean columnArticleCollection(Integer memNo, Integer artNo) {

        // 檢查會員和文章是否存在
        if (!memberRepository.existsByMemNo(memNo) || !columnArticleRepository.existsByArtNo(artNo)) {
            return false;
        }

        // 創建複合主鍵
        ArticleCollection.CompositeArticleCollection compositeArticleCollection = new ArticleCollection.CompositeArticleCollection(memNo, artNo);

        // 檢查是否已經收藏過
        if (articleCollectionRepository.existsByCompositeArticleCollection(compositeArticleCollection)) {
            return false; // 已經收藏過
        }

        // 創建新的收藏記錄
        ArticleCollection newCollection = new ArticleCollection();
        newCollection.setCompositeArticleCollection(compositeArticleCollection);
        articleCollectionRepository.save(newCollection);

        return true; // 成功收藏
    }

    /**
     * 獲取指定文章的回應數量。
     */
    @Override
    public int getResponseCount(Integer artNo) {
        return columnArticleRepository.countColumnArticleByArtNo(artNo);
    }

    /**
     * 獲取目前上架中的文章列表。
     */
    public List<ColumnArticle> getPublishedArticles() {
        // 調用自訂方法以獲取上架中的文章
        return columnArticleRepository.findByArtStat((byte) 0);
    }

    @Override
    public boolean isColumnArticleCollectionByMember(Integer memNo, Integer artNo) {
        return articleCollectionRepository.existsByCompositeArticleCollection(memNo, artNo);
    }

    /**
     * 移除會員的專欄文章收藏。
     */
    @Override
    @Transactional
    public boolean unColumnArticleCollection(Integer memNo, Integer artNo) {
        if (memNo == null || artNo == null) {
            throw new IllegalArgumentException("會員編號和文章編號不能為 null");
        }

        if (isColumnArticleCollectionByMember(memNo, artNo)) {
            columnArticleRepository.deleteByMemNoAndArtNo(memNo, artNo);
            return true;
        }
        return false;
    }

}
