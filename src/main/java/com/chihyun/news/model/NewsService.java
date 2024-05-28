package com.chihyun.news.model;

import com.chihyun.news.dao.NewsRepository;
import com.chihyun.news.entity.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    @Autowired
    NewsRepository repository;

    public void addNews(News news){
        repository.save(news);
    }

    public void updateNews(News news){
        repository.save(news);
    }

    public News getOneNews(Integer newsNo){
        Optional<News> optional =repository.findById(newsNo);
        return optional.orElse(null);
    }
    public List<News> getAll(){
        List<News> list = repository.findAllByOrderByPostTimeDesc();
        return list;
    }
    public void deleteNews(Integer newsNo){
        Optional<News> optional = repository.findById(newsNo);
        if(optional.isPresent()){
            News news = optional.get();
            repository.delete(news);
        }
    }
}
