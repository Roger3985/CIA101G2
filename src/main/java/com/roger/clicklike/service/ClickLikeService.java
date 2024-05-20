package com.roger.clicklike.service;

import com.roger.clicklike.entity.ClickLike;
import com.roger.clicklike.repository.ClickLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ClickLikeService {

    /**
     * 查找所有專欄文章的點讚。
     *
     * @return 包含所有專欄文章點讚的 List<ClickLike> 列表。
     */
    public List<ClickLike> findAll();




}
