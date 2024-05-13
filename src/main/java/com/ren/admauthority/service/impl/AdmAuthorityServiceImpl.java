package com.ren.admauthority.service.impl;

import com.ren.admauthority.dao.AdmAuthorityRepository;
import com.ren.admauthority.entity.AdmAuthority;
import com.ren.admauthority.service.AdmAuthorityService_interface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdmAuthorityServiceImpl implements AdmAuthorityService_interface {

    @Autowired
    private AdmAuthorityRepository admAuthorityRepository;

    /**
     * 新增管理員權限Entity
     *
     * @param admAuthority 傳入管理員權限Entity
     * @return 返回新增後的Entity
     */
    @Override
    public AdmAuthority addAdmAuthority(AdmAuthority admAuthority) {
        return admAuthorityRepository.save(admAuthority);
    }

    /**
     * 因此表格為複合主鍵，無查詢單項功能
     * 此方法用於查詢職位所有的所有權限清單
     *
     * @param titleNo 管理員職位編號
     * @return 返回該職位所有的權限清單
     */
    @Override
    public List<AdmAuthority> getByTitleNo(Integer titleNo) {
        return null;
    }

    /**
     * 因此表格為複合主鍵，無查詢單項功能
     * 此方法用於查詢職位所有的所有權限清單
     *
     * @param AuthFuncNo 權限編號
     * @return 返回該職位所有的權限清單
     */
    @Override
    public List<AdmAuthority> getByAuthFuncNo(Integer AuthFuncNo) {
        return null;
    }

    /**
     * 獲得所有職位權限清單
     *
     * @return 返回所有職位權限清單
     */
    @Override
    public List<AdmAuthority> getAll() {
        return admAuthorityRepository.findAll();
    }

    /**
     * 更新職位權限資料
     *
     * @param admAuthority 傳入職位權限Entity
     * @return 返回更新後的Entity
     */
    @Override
    public AdmAuthority updateAdmAuthority(AdmAuthority admAuthority) {
        return admAuthorityRepository.save(admAuthority);
    }

    /**
     * 根據職位編號刪除相關的所有職位權限
     *
     * @param titleNo 管理員編號
     */
    @Override
    public void deleteByTitleNo(Integer titleNo) {
        admAuthorityRepository.deleteAdmAuthoritiesByCompositeAdmAuthority_TitleNo(titleNo);
    }

    /**
     * 根據權限編號刪除所有相關的職位權限
     *
     * @param authFuncNo 權限編號
     */
    @Override
    public void deleteByFuncNo(Integer authFuncNo) {
        admAuthorityRepository.deleteAdmAuthoritiesByCompositeAdmAuthority_AuthFuncNo(authFuncNo);
    }
}
