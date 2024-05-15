package com.ren.administrator.service.Impl;

import com.ren.administrator.entity.Administrator;
import com.ren.administrator.service.AdministratorService_interface;
import com.ren.title.entity.Title;
import com.ren.administrator.dao.AdministratorRepository;
import com.ren.administrator.dto.LoginState;
import com.roger.member.service.impl.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


import static com.ren.util.Constants.*;
import static com.ren.util.RandomStringGenerator.generateRandomString;
import static java.awt.SystemColor.text;

@Service
public class AdministratorServiceImpl implements AdministratorService_interface {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    @Qualifier("admStrInt")
    private RedisTemplate<String, Integer> stiRedisTemplate;

    @Autowired
    @Qualifier("admIntLogin")
    private RedisTemplate<Integer, LoginState> itlRedisTemplate;

    @Autowired
    @Qualifier("admStrStr")
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 管理員新增管理員資料
     *
     * @param administrator 前台輸入資料(在view層使用thymeleaf tag封包)
     * @return 新增後Entity
     */
    @Override
    public Administrator addAdministrator(Administrator administrator) {
        return administratorRepository.save(administrator);
    }

    /**
     * 註冊管理員資料
     *
     * @param administrator 員工於註冊頁面填寫的資料，員工填入姓名、密碼、信箱，
     *                      其餘資料為預設值由Service層內自動輸入
     * @return 註冊後Entity(預設最低權限)
     */
    @Override
    public Administrator register(Administrator administrator) {
        // 填入預設的資料
        administrator.setAdmStat(Byte.valueOf("1"));
        administrator.getTitle().setTitleNo(4);
        administrator.setAdmHireDate(new Date(new java.util.Date().getTime()));
        administrator.setAdmSalt("1");
        administrator.setAdmLogin(Byte.valueOf("0"));
        administrator.setAdmLogout(Byte.valueOf("1"));
        return addAdministrator(administrator);
    }

//    /**
//     * 將在線管理員清單資料同步到Redis資料庫
//     *
//     * @param list 傳入在線管理員列表
//     */
//    @Override
//    public void backup(List<Administrator> list) {
//        for (Administrator administrator : list) {
//
//        }
//    }

    /**
     * 打卡，預計等之後實作記錄出缺勤表格之後完成
     *
     * @param admNo 傳入員工編號
     */
    @Override
    public void punch(Integer admNo) {

    }

    /**
     * 透過管理員編號查詢單筆管理員資料
     *
     * @param admNo 前台傳入主鍵值，透過主鍵找到該筆資料
     * @return 返回單筆查詢資料
     */
    @Override
    public Administrator getOneAdministrator(Integer admNo) {
        return administratorRepository.findById(admNo).orElse(null);
    }

    /**
     * 透過管理員信箱查詢單筆管理員資料
     *
     * @param admEmail 傳入管理員信箱，透過信箱找到該筆資料
     * @return 返回單筆查詢資料
     */
    @Override
    public Administrator getOneAdministrator(String admEmail) {
        return administratorRepository.findByAdmEmail(admEmail).orElse(null);
    }

    /**
     * 獲得管理員資料列表
     *
     * @return 返回所有管理員資料
     */
    @Override
    public List<Administrator> getAll() {
        return administratorRepository.findAll();
    }

    /**
     * 獲得在線管理員清單
     *
     * @return 返回在線管理員清單
     */
    @Override
    public List<Administrator> getLoginAdms() {
        return administratorRepository
                .findAllByAdmLoginAndAdmLogout(LOGIN_STATE_LOGIN ,LOGOUT_STATE_LOGIN);
    }

    /**
     * 從Redis資料庫內查詢資料
     *
     * @param key 傳入管理員編號
     * @return 返回登入狀態DTO
     */
    @Override
    public LoginState getFromRedis(Integer key) {
        return itlRedisTemplate.opsForValue().get(key);
    }

    /**
     * 更新管理員資料
     *
     * @param administrator 前台輸入資料(在view層使用thymeleaf tag封包)
     * @return 返回更新後Entity
     */
    @Override
    public Administrator updateAdministrator(Administrator administrator) {
        return administratorRepository.save(administrator);
    }

    /**
     *
     *
     * @param administrator 傳入管理員Entity，用於後續更新密碼使用
     */
    @Override
    public void changePwd(Administrator administrator) {

    }

    /**
     * 登入管理員帳號
     * 傳入管理員Entity與SessionID，先確認登入狀態並修改資料庫內的登入狀態
     * 並將管理員資料轉移到登入狀態DTO，之後將此DTO存入Redis資料庫，
     * 返回DTO到Controller、Filter、Listener等處理請求、回應的層級來存入Session
     *
     * @param administrator 使用查詢方法取得Entity，身分核對後將Entity傳入方法內修改登入狀態
     * @param sessionID 用於獲取SessionID，之後拿來核對會話身分
     * @return 返回登入狀態DTO
     */
    @Override
    public LoginState login(Administrator administrator, String sessionID) {
        // Login 1:登入 0:登出 , Logout 1:登出 0:登入
        // 四種情況 1.已登入未登出(帳號使用中) 2.已登入已登出(異常) 3.未登入已登出(帳號未登入) 4.未登入未登出(異常)
        if (administrator.getAdmLogin() == 0 || administrator.getAdmLogout() == 1) {
            administrator.setAdmLogin(Byte.valueOf("1"));
            administrator.setAdmLogout(Byte.valueOf("0"));
            // 返回更新後的administrator
            administrator = updateAdministrator(administrator);
        }
        Integer admNo = administrator.getAdmNo();
        LoginState loginState = new LoginState(admNo, sessionID, administrator.getAdmLogin(), administrator.getAdmLogout(), administrator.getAdmActiveTime(), administrator.getTitle().getTitleNo());
        // 將登入狀態放入Redis資料庫，供LoginStateFilter於每次發出請求時做登入狀態驗證
        storeLoginstateInRedis(admNo, loginState);

        return loginState;
    }

    /**
     * 執行登出，更新資料庫內的登入狀態與刪除Redis內的登入資訊
     *
     * @param loginState 傳入登入狀態，執行登出
     */
    @Override
    public void logout(LoginState loginState) {
        Administrator administrator = getOneAdministrator(loginState.getAdmNo());
        // Login 1:登入 0:登出 , Logout 1:登出 0:登入
        administrator.setAdmLogin(Byte.valueOf("0"));
        administrator.setAdmLogout(Byte.valueOf("1"));
        // 修改資料庫內的登入狀態
        updateAdministrator(administrator);
        // 刪除Redis內的登入資料
        deleteRedisData(administrator.getAdmNo());
    }

    /**
     * 新增或修改Redis資料庫內的資料
     *
     * @param key 傳入管理員編號
     * @param loginState 傳入登入狀態
     */
    @Override
    public void storeLoginstateInRedis(Integer key, LoginState loginState) {
        itlRedisTemplate.opsForValue().set(key, loginState);
    }

    /**
     * 刪除管理員資料
     *
     * @param admNo 根據主鍵刪除該筆資料
     */
    @Override
    public void deleteAdministrator(Integer admNo) {
        administratorRepository.deleteById(admNo);
    }

    /**
     * 刪除Redis資料庫內的資料
     *
     * @param key 傳入管理員編號
     */
    @Override
    public void deleteRedisData(Integer key) {
        if (itlRedisTemplate.hasKey(key)) {
            itlRedisTemplate.delete(key);
        }
    }

    //    @Override
//    public void uploadPhoto(Integer admNo,byte[] admPhoto) {
//        administratorRepository.upload(admNo, admPhoto);
//    }
//
//    @Override
//    public String photoSticker(Integer admNo) {
//        // 將byte[]陣列(二進制資料)轉成Base64(字串)傳到前端的src屬性即可轉成圖片顯示
//        byte[] admPhoto = administratorRepository.photoSticker(admNo);
//        String photoBase64 = null;
//        if (admPhoto != null) {
//            Base64.Encoder encoder = Base64.getEncoder();
//            photoBase64 = encoder.encodeToString(admPhoto);
//        } else {
//            photoBase64 = ""; // 或其他預設值
//        }
//        return photoBase64;
//    }
//
//    @Override
//    public void ChangePhoto(Integer admNo, byte[] admPhoto) {
//        administratorRepository.ChangePhoto(admNo, admPhoto);
//    }

    /**
     * 用於找回密碼時寄出預設密碼的信件
     *
     * @param email 輸入註冊信箱
     * @return 返回布林值，成功重導回登入畫面、失敗則返回
     */
    @Override
    public boolean sendEmail(String email) {

        try {
            // 設定使用 SSL 連線至 Gmail smtp Server
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            // 設定 Gmail 帳號跟密碼
            final String myGmail = "ixlogic.wu@gmail.com";
            final String myGmail_password = "ddjomltcnypgcstn";

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 返回 Gmail 帳號和密碼進行身分驗證
                    return new PasswordAuthentication(myGmail, myGmail_password);
                }
            });

            // 創建新郵件
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myGmail));

            // 設定郵件的收件人
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

            // 設定郵件的主旨
            message.setSubject(emailSubject);

            String newPwd = generateRandomString(12);
            // 設定郵件的內容，包括新密碼
            message.setText(forgotPwdContent + newPwd);
            Administrator administrator = getOneAdministrator(email);
            administrator.setAdmPwd(newPwd);
            updateAdministrator(administrator);

            // 發送郵件
            Transport.send(message);
            System.out.println("傳送成功");

            // 郵件發送成功，返回true
            return true;
        } catch (MessagingException e) {
            System.out.println("傳送失敗!");
            e.printStackTrace();
        }
        return false;
    }

}

