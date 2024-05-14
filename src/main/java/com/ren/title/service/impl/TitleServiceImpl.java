package com.ren.title.service.impl;

import com.ren.title.entity.Title;
import com.ren.title.dao.TitleRepository;
import com.ren.title.service.TitleService_interface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TitleServiceImpl implements TitleService_interface {

    @Autowired
    private TitleRepository titleRepository;

    @Override
    public Title addTitle(Title title) {
        return titleRepository.save(title);
    }

    @Override
    public Title getOneTitle(Integer titleNo) {
        return titleRepository.findById(titleNo).orElse(null);
    }

    @Override
    public List<Title> getAll() {
        return titleRepository.findAll();
    }

    @Override
    public Title updateTitle(Title title) {
        return titleRepository.save(title);
    }

    @Override
    public void deleteTitle(Integer titleNo) {
        titleRepository.deleteById(titleNo);
    }

    //    @Override
//    public List<TitleAdmVO> getAdms(Integer titleNo) {
//        return null;
//    }
//
//    @Override
//    public List<TitleAdmVO> getAdms(String titleName) {
//        return null;
//    }
//
//    @Override
//    public List<TitleAdmVO> getAdmsAll() {
//        return null;
//    }

}
