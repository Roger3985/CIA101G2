package com.ren.admauthority.service;

import com.Entity.*;
import com.Entity.News;

import java.util.List;

interface AdmAuthorityService_interface {
    // 新增(將前端request值放入VO由DAO執行SQL語法，並返回VO由Controller轉給View)
    AdmAuthority addAdmAuthority(AdmAuthority admAuthority);
    // 查詢單筆資料
    AdmAuthority getOneAdmAuthority(Integer titleNo);
    // 查詢所有資料
    List<AdmAuthority> getAll();
    // 修改(返回值由Controller轉給View)
    AdmAuthority updateAdmAuthority(AdmAuthority admAuthority);
    // 刪除
    void deleteAdmAuthority(Integer titleNo);

}
