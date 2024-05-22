package com.ren.administrator.dao;

import com.ren.administrator.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

    /**
     * 以信箱查詢管理員資料
     * 此搜尋方法用於登入時的驗證
     *
     * @param admEmail 於登入畫面輸入的信箱
     * @return 回傳管理員資料，用於Filter做身分核對
     */
    @Transactional
    Optional<Administrator> findByAdmEmail(String admEmail);

    /**
     * 用於查詢資料庫內的登入狀態，預計使用在WebApplication開啟時同步Redis資料，
     * 將以登入的用戶資料同步於Redis資料庫
     *
     * @param loginState 1為登入、0為登出
     * @param logoutState 1為登出、0為登入
     * @return 返回上線人數列表
     */
    @Transactional
    List<Administrator> findAllByAdmLoginAndAdmLogout(Byte loginState, Byte logoutState);

    @Transactional
    Optional<Administrator> getAdministratorByAdmName(String admName);
}
