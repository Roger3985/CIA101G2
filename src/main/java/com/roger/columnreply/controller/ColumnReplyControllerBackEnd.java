package com.roger.columnreply.controller;

import com.roger.columnarticle.entity.ColumnArticle;
import com.roger.columnreply.entity.ColumnReply;
import com.roger.columnreply.service.ColumnReplyService;
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
@RequestMapping("/backend/columnreply")
public class ColumnReplyControllerBackEnd {

    @Autowired
    ColumnReplyService columnReplyService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 前往全部專欄文章回覆的頁面。
     * 此方法處理對 '/backend/columnreply/listAllColumnReply' 的 GET 請求，並返回顯示所有專欄文章回覆的視圖。
     *
     * @param model 用於儲存和傳遞資料到視圖的模型物件。
     * @return '/backend/columnreply/listAllColumnReply' 視圖名稱，以顯示所有專欄文章回覆的列表。
     */
    @GetMapping("/listAllColumnReply")
    public String listAllColumnReply(Model model) {
        return "/backend/columnreply/listAllColumnReply";
    }

    /**
     * 提供所有專欄文章回覆資料列表供視圖渲染使用。
     * 此方法使用`@ModelAttribute`註解，確保在處理請求時可用於視圖中的`columnReplyListData`屬性。
     * 該屬性是一個包含所有專欄文章回覆的列表，由`columnReplyService.findAll()`方法獲取。
     *
     * @return 包含所有專欄文章回覆的列表。
     */
    @ModelAttribute("columnReplyListData")
    protected List<ColumnReply> columnReplyListData() {
        List<ColumnReply> list = columnReplyService.findAll();
        return list;
    }
}

