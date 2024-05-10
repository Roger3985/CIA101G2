package com.ren.admauthority.dao;

import com.Entity.*;

import java.util.List;

public interface AdmAuthorityDAO_interface {
    // 新增
    public void insert(AdmAuthority admAuthority);
    // 查詢單項
    public AdmAuthority findByPrimaryKey(Integer titleNo);
    // 查詢所有
    public List<AdmAuthority> getAll();
    // 修改
    public void update(AdmAuthority admAuthority);
    // 刪除
    public void delete(Integer titleNo);

}
