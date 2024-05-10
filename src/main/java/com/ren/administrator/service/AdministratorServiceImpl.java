package com.ren.administrator.service;

import com.Entity.Administrator;
import com.Entity.Title;
import com.ren.administrator.dao.AdministratorRepository;
import com.ren.administrator.dto.LoginState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

@Service
public class AdministratorServiceImpl implements AdministratorService_interface {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Override
    public Administrator addAdministrator(Administrator administrator) {
        return administratorRepository.save(administrator);
    }

    @Override
    public Administrator register(Administrator administrator) {
        // 填入預設的資料
        administrator.setAdmStat(Byte.valueOf("1"));
        administrator.getTitle().setTitleNo(4);
        administrator.setAdmHireDate(new Date(new java.util.Date().getTime()));
        administrator.setAdmSalt("1");
        administrator.setAdmLogin(Byte.valueOf("0"));
        administrator.setAdmLogout(Byte.valueOf("1"));
        return administratorRepository.save(administrator);
    }

    @Override
    public Administrator getOneAdministrator(Integer admNo) {
        return administratorRepository.findById(admNo).orElse(null);
    }

    @Override
    public Administrator getOneAdministrator(String admEmail) {
        return administratorRepository.findByAdmEmail(admEmail).orElse(null);
    }

    @Override
    public List<Administrator> getAll() {
        return administratorRepository.findAll();
    }

    @Override
    public Administrator updateAdministrator(Administrator administrator) {
        return administratorRepository.save(administrator);
    }

    @Override
    public LoginState login(Administrator administrator, HttpSession session) {
        // Login 1:登入 0:登出 , Logout 1:登出 0:登入
        // 四種情況 1.已登入未登出(帳號使用中) 2.已登入已登出(異常) 3.未登入已登出(帳號未登入) 4.未登入未登出(異常)
        if (administrator.getAdmLogin() == 0 || administrator.getAdmLogout() == 1) {
            administrator.setAdmLogin(Byte.valueOf("1"));
            administrator.setAdmLogout(Byte.valueOf("0"));
            // 返回更新後的administrator
            administrator = administratorRepository.save(administrator);
        }
        LoginState loginState = new LoginState();
        loginState.setAdmNo(administrator.getAdmNo());
        loginState.setJsessionid(session.getId());
        loginState.setAdmLogin(administrator.getAdmLogin());
        loginState.setAdmLogout(administrator.getAdmLogout());
        loginState.setAdmActiveTime(administrator.getAdmActiveTime());
        loginState.setTitleNo(administrator.getTitle().getTitleNo());

        return loginState;
    }

    @Override
    public void deleteAdministrator(Integer admNo) {
        administratorRepository.deleteById(admNo);
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
//
//    @Override
//    public List<String> register(Administrator administrator) {
//        // 放驗證錯誤訊息，在controller迭代放入errorMessage
//        List<String> existData = new LinkedList<>();
//        // 輸入名字
//        String inputName = administrator.getAdmName();
//        // 輸入信箱
//        String inputEmail = administrator.getAdmEmail();
//        // 查詢使用者名稱與信箱，檢查是否有重複
//        existData = administratorRepository.findExistData(inputName, inputEmail);
//        if (existData.size() >= 1) {
//            return existData;
//        }
//
//        return existData;
//    }

}

