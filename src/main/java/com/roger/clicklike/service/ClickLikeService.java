package com.roger.clicklike.service;

import com.roger.clicklike.entity.ClickLike;
import com.roger.columnarticle.entity.ColumnArticle;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface ClickLikeService {

    /**
     * 查找所有專欄文章的點讚。
     *
     * @return 包含所有專欄文章點讚的 List<ClickLike> 列表。
     */
    public List<ClickLike> findAll();

    public boolean isArticleLikedByMember(int memNo, int artNo);

    public boolean likeColumnArticle(Integer memNo, Integer artNo);

    public boolean unlikeArticle(int memNo, int artNo);

    public List<ClickLike> findByMemberMemNo(Integer memNo);

    public List<ClickLike> getLikedArticlesByMember(Integer memNo);

    /**
     * 根據文章編號獲取按讚數量。
     *
     * @param artNo 文章編號。
     * @return 按讚數量。
     */
    public int getLikeCountByArtNo(Integer artNo);

}
