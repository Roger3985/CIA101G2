//package com.ren.elasticsearch.controller;
//
//import com.ren.elasticsearch.service.ElasticsearchService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.io.IOException;
//import java.util.List;
//
//@Controller
//public class SearchController {
//
//    @Autowired
//    private ElasticsearchService elasticsearchService;
//
//    @GetMapping("/backend/searchTest")
//    public String tosearch() {
//        return "backend/searchTest";
//    }
//
//    @GetMapping("/search")
//    public String search(@RequestParam("query") String query, Model model) throws IOException {
//        String[] indices = {"administrators", "admauthorities", "authorityfunctions", "titles"};
//        List<Object> results = elasticsearchService.searchEntities(query, indices);
//        model.addAttribute("results", results);
//        return "searchResults";
//    }
//}
