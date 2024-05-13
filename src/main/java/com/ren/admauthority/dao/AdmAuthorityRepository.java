package com.ren.admauthority.dao;

import com.ren.admauthority.entity.AdmAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AdmAuthorityRepository extends JpaRepository<AdmAuthority, Integer> {

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
     * 根據職位編號刪除職位權限
     *
     * @param titleNo
     */
    void deleteAdmAuthoritiesByCompositeAdmAuthority_TitleNo(Integer titleNo);

    /**
     * 根據權限編號刪除職位權限
     *
     * @param AuthFuncNo
     */
    void deleteAdmAuthoritiesByCompositeAdmAuthority_AuthFuncNo(Integer AuthFuncNo);



}
