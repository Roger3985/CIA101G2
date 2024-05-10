package com.ren.title.service;

import com.Entity.Title;

import java.util.List;

public interface TitleService_interface {
    // 新增(將前端request值放入VO由DAO執行SQL語法，並返回VO由Controller轉給View)
    Title addTitle(Title title);
    // 查詢單筆資料
    Title getOneTitle(Integer titleNo);
    // 查詢所有資料
    List<Title> getAll();
    // 修改(將前端request值放入VO由DAO執行SQL語法，返回VO由Controller轉給View)
    Title updateTitle(Title title);
    // 刪除
    void deleteTitle(Integer titleNo);

    //    // 查詢同職位編號的所有管理員
//    List<TitleAdmVO> getAdms(Integer titleNo);
//    // 查詢同職位的所有管理員
//    List<TitleAdmVO> getAdms(String titleName);
//    // 查詢所有管理員
//    List<TitleAdmVO> getAdmsAll();
}
