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

    /**
     * 獲取點讚數最多的文章。
     *
     * 此方法從資料庫中查詢每篇文章的編號及其點讚數量，並按點讚數量降序排列。
     * 返回的列表中的每個元素都是一個Object數組，其中：
     * - 第一個元素是文章編號 (artNo)
     * - 第二個元素是點讚數量 (likeCount)
     *
     * @return 包含點讚數最多文章及其點讚數量的列表
     */
    public List<Object[]> getMostLikedArticles();

}
