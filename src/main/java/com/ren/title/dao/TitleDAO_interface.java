//package com.ren.title.dao;
//
//import com.ren.title.entity.Title;
//
//import java.util.List;
//
//public interface TitleDAO_interface {
//
//    // 預計寫入功能
//    // 1. 可以新增職位
//    // 2. 查詢功能
//    //    (1) 可以在下拉式選單中選取編號(或名稱)，將顯示職位編號、名稱與有哪些人，並在表單底下顯示有哪些人
//    //    (2) 顯示全部職位名稱與多少人
//    //    (3) 點擊人數時顯示共有哪些人
//    // 3. 可以修改職位名稱
//    // 4. 可以刪除職位(後台管理員參照)
//
//    // 新增
//    public void insert(Title title);
//    // 以職位編號查詢(職位編號, 職位名稱)
//    public Title findByPrimaryKey(Integer titleNo);
//    // 查詢全部(職位編號, 職位名稱)
//    public List<Title> getAll();
//    // 以職位編號查詢(職位編號, 職位名稱, 管理員名稱)
//    public List<TitleAdmVO> getAdms(Integer titleNo);
//    // 以職稱查詢(職位編號, 職位名稱, 管理員名稱)
//    public List<TitleAdmVO> getAdms(String titleName);
//    // 查詢全部(職位編號, 職位名稱, 管理員名稱)並以職位編號排序
//    public List<TitleAdmVO> getAdmsAll();
//    // 修改
//    public void update(Title title);
//    // 刪除
//    public void delete(Integer titleNo);
//
//}
