package com.roger.clicklike.service.impl;

import com.roger.clicklike.entity.ClickLike;
import com.roger.clicklike.repository.ClickLikeRepository;
import com.roger.clicklike.service.ClickLikeService;
import com.roger.columnarticle.entity.ColumnArticle;
import com.roger.columnarticle.repository.ColumnArticleRepository;
import com.roger.member.entity.Member;
import com.roger.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClickLikeServiceImpl implements ClickLikeService {

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
     * 查找所有專欄文章的點讚。
     */
    @Override
    public List<ClickLike> findAll() {
        return clickLikeRepository.findAll();
    }

    @Override
    public boolean isArticleLikedByMember(int memNo, int artNo) {
        return clickLikeRepository.existsByCompositeClickLike(memNo, artNo);
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

    @Override
    @Transactional
    public boolean unlikeArticle(int memNo, int artNo) {
        if (isArticleLikedByMember(memNo, artNo)) {
            clickLikeRepository.deleteByCompositeClickLike_MemNoAndCompositeClickLike_ArtNo(memNo, artNo);
            return true;
        }
        return false;
    }

    @Override
    public List<ClickLike> findByMemberMemNo(Integer memNo) {
        return clickLikeRepository.findByCompositeClickLike_MemNo(memNo);
    }

    /**
     * 根據會員編號獲取該會員已經點讚的文章列表。
     *
     * @param memNo 會員編號
     * @return 包含該會員已點讚的文章列表的集合
     */
    @Override
    public List<ClickLike> getLikedArticlesByMember(Integer memNo) {
        return clickLikeRepository.findByCompositeClickLike_MemNo(memNo);
    }

}
