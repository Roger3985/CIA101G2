package com.chihyun.news.controller;

import com.chihyun.news.entity.News;
import com.chihyun.news.model.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("frontend/news")
public class NewsFrontendController {
    @Autowired
    NewsService newsService;

    @GetMapping("/selectNews")
    public String showNews(Model model) {

        return "/frontend/news/listAllNews";
    }

    @ModelAttribute("newsListData")
    protected List<News> ListAllData() {
        List<News> list = newsService.getAll();
        return list;
    }
}
