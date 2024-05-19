package com.ren.admauthority.dao;

import com.ren.admauthority.entity.AdmAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AdmAuthorityRepository extends JpaRepository<AdmAuthority, Integer> {

    /**
     * 複合主鍵表格，透過兩個主鍵編號找到Entity
     *
     * @param titleNo 管理員職位編號
     * @param AuthFuncNo 權限編號
     * @return 返回管理員權限Entity
     */
    @Transactional
    AdmAuthority findAdmAuthorityByTitle_TitleNoAndAuthorityFunction_AuthFuncNo(Integer titleNo, Integer AuthFuncNo);

    /**
     * 透過職位編號查詢所有該職位的權限功能
     *
     * @param titleNo 傳入職位編號
     * @return 返回職位權限列表
     */
    @Transactional
    List<AdmAuthority> findAdmAuthoritiesByCompositeAdmAuthority_TitleNo(Integer titleNo);

    /**
     * 透過權限編號查詢所有有該權限的職位
     *
     * @param AuthFuncNo 傳入權限編號
     * @return 返回職位權限列表
     */
    @Transactional
    List<AdmAuthority> findAdmAuthoritiesByCompositeAdmAuthority_AuthFuncNo(Integer AuthFuncNo);

    /**
     * 依複合主鍵刪除管理員權限
     *
     * @param titleNo 管理員編號
     * @param authFuncNo 權限編號
     */
    void deleteAdmAuthorityByTitle_TitleNoAndAuthorityFunction_AuthFuncNo(Integer titleNo, Integer authFuncNo);

    /**
     * 根據職位編號刪除職位權限
     *
     * @param titleNo 管理員編號
     */
    @Transactional
    void deleteAdmAuthoritiesByCompositeAdmAuthority_TitleNo(Integer titleNo);

    /**
     * 根據權限編號刪除職位權限
     *
     * @param AuthFuncNo 權限編號
     */
    @Transactional
    void deleteAdmAuthoritiesByCompositeAdmAuthority_AuthFuncNo(Integer AuthFuncNo);



}
