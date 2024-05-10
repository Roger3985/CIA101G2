package com.ren.authorityfunction.service;

import com.ren.authorityfunction.entity.AuthorityFunction;

import java.util.List;

public interface AuthorityFunctionService_interface {
    // 新增(將前端request值放入VO由DAO執行SQL語法，並返回VO由Controller轉給View)
    // Auto-increment
    public AuthorityFunction addAuthorityFunction(AuthorityFunction authorityFunction);
    // 查詢單筆資料
    public AuthorityFunction getOneAuthorityFunction(Integer authFuncNo);
    // 查詢所有資料
    public List<AuthorityFunction> getAll();
    // 修改(返回值由Controller轉給View)
    public AuthorityFunction updateAuthorityFunction(AuthorityFunction authorityFunction);
    // 刪除
    public void deleteAuthorityFunction(Integer authFuncNo);

}
