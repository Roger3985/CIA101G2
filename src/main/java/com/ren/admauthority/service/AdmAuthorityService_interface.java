package com.ren.admauthority.service;

import com.ren.admauthority.entity.AdmAuthority;

import java.util.List;

public interface AdmAuthorityService_interface {

    /**
     * C:
     * 新增管理員權限功能
     *
     * @param admAuthority 傳入管理員權限Entity
     * @return 返回新增後的Entity
     */
    AdmAuthority addAdmAuthority(AdmAuthority admAuthority);

    /**
     * R:
     * 根據職位編號,權限編號查詢功能權限(複合主鍵表格)
     *
     * @param titleNo 管理員職位編號
     * @param AuthFuncNo 權限編號
     * @return 返回查詢之Entity
     */
    AdmAuthority getOneAdmAuthority(Integer titleNo, Integer AuthFuncNo);

    /**
     * R:
     * 根據職位編號查詢該職位所有的所有權限(複合主鍵表格)
     *
     * @param titleNo 管理員職位編號
     * @return 該職位所有的權限清單
     */
    List<AdmAuthority> getByTitleNo(Integer titleNo);

    /**
     * R:
     * 根據權限編號查詢所有擁有該權限的職位(複合主鍵表格)
     *
     * @param AuthFuncNo 權限編號
     * @return 返回所有擁有該權限的職位清單
     */
    List<AdmAuthority> getByAuthFuncNo(Integer AuthFuncNo);

    /**
     * R:
     * 獲得所有職位權限資料
     *
     * @return 返回所有資料的列表
     */
    List<AdmAuthority> getAll();

    /**
     * U:
     * 更新職位權限資料
     *
     * @param admAuthority 傳入職位權限Entity
     * @return 返回更新後Entity
     */
    AdmAuthority updateAdmAuthority(AdmAuthority admAuthority);

    /**
     * D:
     * 依複合主鍵刪除管理員權限
     *
     * @param titleNo 管理員編號
     * @param authFuncNo 權限編號
     */
    void deleteAdmAuthority(Integer titleNo, Integer authFuncNo);

    /**
     * D:
     * 根據管理員編號刪除職位權限
     *
     * @param titleNo 管理員編號
     */
    void deleteByTitleNo(Integer titleNo);

    /**
     * D:
     * 根據權限編號刪除職位權限
     *
     * @param AuthFuncNo 權限編號
     */
    void deleteByFuncNo(Integer AuthFuncNo);

}
