package com.chihyun.news.controller;

import com.chihyun.news.entity.News;
import com.chihyun.news.model.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/backend/news")
public class NewsBackendController {

    @Autowired
    NewsService newsService;

    @GetMapping("/selectNews")
    public String showNews(Model model) {
        return "/backend/news/listAllNews";
    }

    @ModelAttribute("newsListData")
    protected List<News> ListAllData() {
        List<News> list = newsService.getAll();
        return list;
    }

    @GetMapping("/addNews")
    public String addNews(Model model) {
        News news = new News();
        model.addAttribute("news", news);
        return "backend/news/addNews";
    }

    @PostMapping("insert")
    public String insert(@Valid News news, BindingResult result, ModelMap model,
                         @RequestParam String postTime) throws IOException {

        news.setPostTime(Timestamp.valueOf(LocalDateTime.now()));

        if (result.hasFieldErrors()) {
            List<String> errorMessage = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMessage.add(error.getDefaultMessage());
                model.addAttribute("errors", errorMessage);
                return "backend/news/addNews";
            }
        }
        newsService.addNews(news);
        List<News> list = newsService.getAll();
        model.addAttribute("newsListData", list);
        return "redirect:/backend/news/selectNews";
    }

    @GetMapping("/updateNews")
    public String updateNews(ModelMap modelmap) {
        News news = new News();
        modelmap.addAttribute("news", news);
        modelmap.addAttribute("newsListData", newsService.getAll());
        return "backend/news/updateNews";
    }

    @PostMapping("/updateNews")
    public String getOneForUpdate(@RequestParam Integer newsNo, Model model) {
        News news = newsService.getOneNews(newsNo);
        model.addAttribute("news", news);
        return "backend/news/updateNews";
    }

    @PostMapping("/update")
    public String update(@Valid News news, BindingResult result, Model model,
                         @RequestParam String postTime) throws IOException {

        news.setPostTime(Timestamp.valueOf(LocalDateTime.now()));

        if (result.hasFieldErrors()) {
            List<String> errorMessage = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMessage.add(error.getDefaultMessage());
                model.addAttribute("errors", errorMessage);
                return "backend/news/updateNews";
            }
        }

        newsService.updateNews(news);
        List<News> list = newsService.getAll();
        model.addAttribute("newsListData", list);
        return "redirect:/backend/news/selectNews";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("newsNo") Integer newsNo, Model model) {
        newsService.deleteNews(newsNo);
        List<News> list = newsService.getAll();
        model.addAttribute("newsListData", list);
        return "/backend/news/listAllNews";
    }
}
