package com.roger.clicklike.controller;

import com.roger.clicklike.entity.ClickLike;
import com.roger.clicklike.service.ClickLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/backend/clicklike")
public class ClickLikeControllerBackEnd {

    @Autowired
    private ClickLikeService clickLikeService;

    @Autowired
    @Qualifier("colStrStr")
    private StringRedisTemplate redisTemplate;

    /**
     * 前往全部專欄文章點讚的頁面。
     * 此方法處理對 '/backend/clicklike/listAllClickLike' 的 GET 請求，並返回顯示所有專欄文章點讚的視圖。
     *
     * @param model 用於儲存和傳遞資料到視圖的模型物件。
     * @return '/backend/clicklike/listAllClickLike' 視圖名稱，以顯示所有專欄文章點讚的列表。
     */
    @GetMapping("/listAllClickLike")
    public String listAllClickLike(Model model) {
        return "backend/clicklike/listAllClickLike";
    }

    /**
     * 提供所有專欄文章點讚資料列表供視圖渲染使用。
     * 此方法使用`@ModelAttribute`註解，確保在處理請求時可用於視圖中的`clickLikeListData`屬性。
     * 該屬性是一個包含所有專欄文章點讚的列表，由`clickLikeService.findAll()`方法獲取。
     *
     * @return 包含所有專欄文章點讚的列表。
     */
    @ModelAttribute("clickLikeListData")
    protected List<ClickLike> clickLikeListData() {
        List<ClickLike> list = clickLikeService.findAll();
        return list;
    }


}
