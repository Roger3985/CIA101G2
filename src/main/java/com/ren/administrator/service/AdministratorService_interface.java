package com.ren.administrator.service;

import com.ren.administrator.dto.LoginState;
import com.ren.administrator.entity.Administrator;

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

//    /**
//     * 將在線管理員的資料同步到Redis資料庫(用於當ServletContext啟動時使用)
//     *
//     * @param list 傳入在線管理員列表
//     */
//    void backup(List<Administrator> list);

    /**
     * 用來確認員工出缺勤狀態的方法，每當員工登入時，將今天第一次更新的活動時間存入Redis資料庫
     * 之後登入或登出就不會再更新了，並於首頁顯示出缺勤狀態，設置於每天24:00資料消失
     *
     * @param admNo 傳入員工編號
     */
    void punch(Integer admNo);

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
     * R:
     * 獲得所有已上線的管理員清單
     *
     * @return 返回管理員列表到View渲染
     */
    List<Administrator> getLoginAdms();

    /**
     * R:
     * 從Redis資料庫內查詢資料
     *
     * @param key 傳入管理員編號
     * @return 返回登入狀態DTO
     */
    LoginState getFromRedis(Integer key);

    /**
     * U:
     * 修改單筆管理員資料
     *
     * @param administrator 前台輸入資料(在view層使用thymeleaf tag封包)
     * @return 返回修改後的Entity，後續可用於渲染View
     */
    Administrator updateAdministrator(Administrator administrator);

    /**
     * 修改密碼
     *
     * @param administrator 傳入管理員Entity，用於後續更新密碼使用
     */
    void changePwd(Administrator administrator);

    /**
     * U:
     * 登入成功，修改管理員登入狀態，在Redis資料庫中增加登入狀態緩存
     *
     * @param administrator 使用查詢方法取得Entity，身分核對後將Entity傳入方法內修改登入狀態
     * @param session 傳入Session，用於獲取sessionID與後續存入登入狀態
     */
    void login(Administrator administrator, HttpSession session);

    /**
     * 登出成功，修改管理員登入狀態，並關閉Session，移除Redis資料庫中的登入狀態緩存
     *
     * @param loginState 傳入登入狀態，執行登出
     */
    void logout(LoginState loginState);

    /**
     * U:
     * 新增或修改Redis資料庫內的資料
     *
     * @param key 傳入管理員編號
     * @param loginState 傳入登入狀態
     */
    void storeLoginstateInRedis(Integer key, LoginState loginState);

    /**
     * D:
     * 刪除單筆資料
     *
     * @param admNo 根據主鍵刪除該筆資料
     */
    void deleteAdministrator(Integer admNo);

    /**
     * 刪除Redis資料庫內的資料
     *
     * @param key 傳入管理員編號
     */
    void deleteRedisData(Integer key);

    // 上傳圖片
//    public void uploadPhoto(Integer admNo, byte[] admPhoto);
//    // 顯示大頭貼
//    public String photoSticker(Integer admNo);
//    // 修改圖片
//    public void ChangePhoto(Integer admNo, byte[] admPhoto);

    /**
     * 用於找回密碼時寄出預設密碼的信件
     *
     * @param email 輸入註冊信箱
     * @return 返回布林值，成功重導回登入畫面、失敗則返回
     */
    boolean sendEmail(String email);

}
