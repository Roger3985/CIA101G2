package com.roger.columnarticle.service.impl;

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

    @Override
    @Transactional
    public boolean likeColumnArticle(Integer memNo, Integer artNo) {
        if (!memberRepository.existsByMemNo(memNo) || !columnArticleRepository.existsByArtNo(artNo)) {
            return false;
        }

        // 創建複合主鍵
        ClickLike.CompositeClickLike compositeClickLike = new ClickLike.CompositeClickLike(memNo, artNo);

        // 檢查是否已經點讚
        if (clickLikeRepository.existsByCompositeClickLike(compositeClickLike)) {
            return false; // 已經點讚過
        }

        // 創建點讚紀錄
        ClickLike clickLike = new ClickLike();
        clickLike.setCompositeClickLike(compositeClickLike);

        // 獲得用戶和專欄文章實體
        Member member = memberRepository.findMemberByMemNo(memNo).orElse(null);
        ColumnArticle columnArticle = columnArticleRepository.findColumnArticleByArtNo(artNo);

        if (member != null && columnArticle != null) {
            clickLike.setMember(member);
            clickLike.setColumnArticle(columnArticle);

            // 保存點讚紀錄
            clickLikeRepository.save(clickLike);

            // 更新文章的點讚數
            Set<ClickLike> clickLikes = columnArticle.getClickLikes();
            int clickCount = clickLikes.size();
            clickCount++; // 增加點讚數
            // 更新文章的 clickLikes 屬性
            columnArticle.setClickLikes(clickLikes);

            return true;
        }

        return false;
    }

}
