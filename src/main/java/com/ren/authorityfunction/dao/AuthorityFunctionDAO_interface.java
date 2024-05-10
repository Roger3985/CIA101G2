package com.ren.authorityfunction.dao;

import com.Entity.AuthorityFunction;
import com.Entity.ServiceRobot;

import java.util.List;

public interface AuthorityFunctionDAO_interface {
    // 新增
    public void insert(AuthorityFunction authorityFunction);
    // 查詢單項
    public AuthorityFunction findByPrimaryKey(Integer authFuncNo);
    // 查詢所有
    public List<AuthorityFunction> getAll();
    // 修改
    public void update(AuthorityFunction authorityFunction);
    // 刪除
    public void delete(Integer authFuncNo);

}
