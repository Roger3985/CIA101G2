package com.chihyun.news.controller;

import com.chihyun.news.entity.News;
import com.chihyun.news.model.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String insert(BindingResult result, Model model, News news,
                         @RequestParam @NotBlank(message = "標題請勿空白") String NewsTitle,
                         @RequestParam @NotBlank(message = "內容請勿空白") String NewsContent,
                         @RequestParam @NotNull(message = "發布時間請勿空白") @Future(message = "發布日期不得早於現在時間") String postTime
    ) throws IOException {

        Timestamp postTimeParm = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            java.util.Date parseDate = dateFormat.parse(postTime);
            postTimeParm = new Timestamp(parseDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        news.setPostTime(postTimeParm);

        if(result.hasFieldErrors()){
            List<String> errorMessage = new ArrayList<>();
            for(FieldError error : result.getFieldErrors()){
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


    @PostMapping("/updateNews")
    public String getOneForUpdate(@RequestParam Integer newsNo, Model model) {
        News news = newsService.getOneNews(newsNo);
        model.addAttribute("news", news);
        return "backend/news/updateNews";
    }

    @PostMapping("/update")
    public String update(@Valid News news, BindingResult result, Model model,
                         @RequestParam String postTime) throws IOException {
        Timestamp postTimeParm = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            java.util.Date parseDate = dateFormat.parse(postTime);
            postTimeParm = new Timestamp(parseDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        news.setPostTime(postTimeParm);

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
