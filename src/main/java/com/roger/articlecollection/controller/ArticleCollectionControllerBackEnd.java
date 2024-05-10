package com.roger.articlecollection.controller;

import com.roger.articlecollection.entity.ArticleCollection;
import com.roger.articlecollection.service.ArticleCollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/backend/articlecollection")
public class ArticleCollectionControllerBackEnd {

    @Autowired
    private ArticleCollectionService articleCollectionService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 前往全部專欄文章收藏的頁面。
     * 此方法處理對 '/backend/articlecollection/listAllArticleCollection' 的 GET 請求，並返回顯示所有專欄文章收藏的視圖。
     *
     * @param model 用於儲存和傳遞資料到視圖的模型物件。
     * @return 'backend/articlecollection/listAllArticleCollection' 視圖名稱，以顯示所有專欄文章收藏的列表。
     */
    @GetMapping("/listAllArticleCollection")
    public String listAllArticleCollection(Model model) {
        return "backend/articlecollection/listAllArticleCollection";
    }

    /**
     * 提供所有專欄文章收藏資料列表供視圖渲染使用。
     * 此方法使用`@ModelAttribute`註解，確保在處理請求時可用於視圖中的`articleCollectionListData`屬性。
     * 該屬性是一個包含所有專欄文章收藏的列表，由`articleCollectionService.findAll()`方法獲取。
     *
     * @return 包含所有專欄文章點讚的列表。
     */
    @ModelAttribute("articleCollectionListData")
    protected List<ArticleCollection> articleCollectionListData() {
        List<ArticleCollection> list = articleCollectionService.findAll();
        return list;

    }


}
