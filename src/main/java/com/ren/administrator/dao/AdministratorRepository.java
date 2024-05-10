package com.ren.administrator.dao;

import com.ren.administrator.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
