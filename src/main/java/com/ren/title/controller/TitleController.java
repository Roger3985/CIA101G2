package com.ren.title.controller;

import com.Entity.Title;
import com.ren.title.service.TitleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/backend/title")
public class TitleController {

    @Autowired
    private TitleServiceImpl titleSvc;

    @GetMapping("/listOneTitle")
    public String listOne(@PathVariable Integer titleNo) {
        return "/backend/title/listOneTitle";
    }

    @GetMapping("/listAllTitles")
    public List<Title> listAll() {
        return titleSvc.getAll();
    }

    @PostMapping("/addTitle")
    public Title addTitle(@RequestBody Title title) {
        return titleSvc.addTitle(title);
    }

    @PutMapping("/updateTitle")
    public Title updateTitle(@PathVariable Integer titleNo, @RequestBody Title title) {
        // Ensure the productNo in the path matches the productNo in the request body
        if (!titleNo.equals(title.getTitleNo())) {
            throw new IllegalArgumentException("Path variable productNo must match the productNo in the request body");
        }
        return titleSvc.updateTitle(title);
    }

//    @DeleteMapping("/titles/{titleNo}")
//    public void deleteTitle(@PathVariable Integer titleNo) {
//        titleSvc.deleteTitle(titleNo);
//    }

    @ModelAttribute("/")
    protected List<Title> getAllTitles() {
        return titleSvc.getAll();
    }
}
