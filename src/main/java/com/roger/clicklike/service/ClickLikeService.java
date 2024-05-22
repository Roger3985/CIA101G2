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

}
