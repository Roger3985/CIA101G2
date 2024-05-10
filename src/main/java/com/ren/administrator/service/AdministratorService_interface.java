package com.ren.administrator.service;

import com.Entity.Administrator;
import com.Entity.ServicePicture;
import com.ren.administrator.dto.LoginState;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.List;

public interface AdministratorService_interface {

    /**
     * C:
     * 新增單筆管理員
     *
     * @param administrator 前台輸入資料(在view層使用thymeleaf tag封包)
     * @return 返回新增後的Entity，後續可用於渲染View
     */
    Administrator addAdministrator(Administrator administrator);

    /**
     * C:
     * 員工申請管理員帳號
     *
     * @param administrator 員工於註冊頁面填寫的資料，員工填入姓名、密碼、信箱，
     *                      其餘資料為預設值由Service層內自動輸入
     * @return 返回新增後的Entity，後續可用於渲染View
     */
    Administrator register(Administrator administrator);

    /**
     * R:
     * 查詢單筆管理員資料
     *
     * @param admNo 前台傳入主鍵值，透過主鍵找到該筆資料
     * @return 返回Entity到View渲染
     */
    Administrator getOneAdministrator(Integer admNo);

    /**
     * R:
     * 查詢單筆管理員資料
     *
     * @param admEmail 傳入管理員信箱，透過信箱找到該筆資料
     * @return 返回Entity到View渲染
     */
    Administrator getOneAdministrator(String admEmail);
    /**
     * R:
     * 查詢所有管理員資料
     *
     * @return 返回管理員列表到View渲染
     */
    List<Administrator> getAll();

    /**
     * U:
     * 修改單筆管理員資料
     *
     * @param administrator 前台輸入資料(在view層使用thymeleaf tag封包)
     * @return 返回修改後的Entity，後續可用於渲染View
     */
    Administrator updateAdministrator(Administrator administrator);

    /**
     * U:
     * 登入成功，修改管理員登入狀態
     *
     * @param administrator 使用查詢方法取得Entity，身分核對後將Entity傳入方法內修改登入狀態
     * @return 返回帳號登入狀態DTO
     */
    LoginState login(Administrator administrator, HttpSession session);

    /**
     * D:
     * 刪除單筆資料
     *
     * @param admNo 根據主鍵刪除該筆資料
     */
    void deleteAdministrator(Integer admNo);

    // 上傳圖片
//    public void uploadPhoto(Integer admNo, byte[] admPhoto);
//    // 顯示大頭貼
//    public String photoSticker(Integer admNo);
//    // 修改圖片
//    public void ChangePhoto(Integer admNo, byte[] admPhoto);
//    // 註冊(含驗證)
//    public List<String> register(Administrator administrator);

}
