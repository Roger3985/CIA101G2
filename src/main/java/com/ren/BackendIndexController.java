package com.ren;

import com.ren.administrator.entity.Administrator;
import com.ren.administrator.dto.LoginState;
import com.ren.administrator.service.AdministratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import static com.ren.util.Constants.YES;
import static com.ren.util.RandomStringGenerator.generateRandomString;
import static com.ren.util.Validator.validateEmail;

@Controller
@RequestMapping("/backend")
public class BackendIndexController {

    @Autowired
    @Qualifier("integerLoginState")
    private RedisTemplate<Integer, LoginState> itlRedisTemplate;

    @Autowired
    @Qualifier("stringInteger")
    private RedisTemplate<String, Integer> stiRedisTemplate;

    @Autowired
    private AdministratorServiceImpl administratorSvc;

    /**
     * 前往首頁
     * 登入後跳轉、在登入狀態時點選側邊欄的 icon 與 Home 後可回到首頁
     *
     * @return forward to backend index
     */
    @GetMapping("/index")
    public String toBackendIndex() {
        System.out.println("首頁我來囉!");
        return "backend/index";
    }

    /**
     * 前往註冊頁面
     *
     * @return forward to backend register
     */
    @GetMapping("/register")
    public String toRegister(Model model) {
        model.addAttribute("administrator", new Administrator());
        return "backend/register";
    }

    /**
     * 前往登入頁面
     *
     * @return forward to backend login
     */
    @GetMapping("/login")
    public String toLogin(Model model) {
        model.addAttribute("administrator", new Administrator());
        return "backend/login";
    }


    // 記錄登入活動，如嘗試登入、成功登入、登入失敗等
    // 確認帳號正確，但密碼不正確的情況 n + 1， 30分鐘內達5次鎖定登入

    @PostMapping("/login/login")
    public String login(@RequestParam String userId,
                        @RequestParam String admPwd,
                        @RequestParam Byte autoLogin,
                        HttpSession session,
                        HttpServletRequest req,
                        HttpServletResponse res,
                        ModelMap model) {

        // 宣告管理員與管理員編號，於後續參數驗證後賦值
        Administrator administrator = null;
        Integer admNo = null;

        // 確認是否為信箱格式，true則透過信箱搜尋使用者資訊，false則透過管理員編號搜尋使用者資訊
        if (validateEmail(userId)) {
            administrator = administratorSvc.getOneAdministrator(userId);
            // 獲得管理員編號，之後存入Redis資料庫使用
            admNo = administrator.getAdmNo();
        } else {
            // 將管理員編號轉成 Integer，供Service方法使用
            admNo = Integer.valueOf(userId);
            administrator = administratorSvc.getOneAdministrator(admNo);
        }

        // 在驗證前將使用者Id放入model，如果使用者輸入錯誤，返回給使用者而不用重新輸入
        model.addAttribute("userId" ,userId);

        // 判斷是否有無這個帳號
        if (administrator == null) {
            model.addAttribute("message", "不存在此用戶");
            return "backend/login";
        }

        // 判斷密碼是否正確
        if (!admPwd.equals(administrator.getAdmPwd())) {
            model.addAttribute("message", "密碼錯誤!");
            return "backend/login";
        }

        // 確認帳號是否已被登入
        if (administrator.getAdmLogin() == 1) {
            System.out.println("帳號已被登入");
        }

        // 使用Service的登入方法，更改administrator的登入狀態，並傳回登入狀態DTO，
        // 於後續存入Session做權限、登入狀態驗證
        LoginState loginState = administratorSvc.login(administrator, session);

        // 確認密碼正確後，回傳登入成功訊息，並將administrator存入session
        model.addAttribute("message", "登入成功!");

        // 確認使用者是否要自動登入
        if (autoLogin == YES) {
            // 生成名為autoLogin的cookie，其值設置為一個亂數生成的字符串，分別存入給使用者與redis資料庫，做身分核對
            String random = generateRandomString(40);
            Cookie cookie = new Cookie("autoLogin", random);
            // 設置存活7天
            cookie.setMaxAge(604800);
            // 設置cookie的路徑為/backend，當訪問所有後台網頁時都可以獲取這個cookie
            cookie.setPath(req.getContextPath() + "/backend");
            res.addCookie(cookie);
            stiRedisTemplate.opsForValue().set(random, admNo);
            System.out.println("cookie存入");
        }

        // 將登入狀態放入Redis資料庫，供LoginStateFilter於每次發出請求時做登入狀態驗證
        storeLoginstateInRedis(admNo, loginState);
        System.out.println("登入成功!!!");

        return "redirect:/backend/index";
    }

    @GetMapping("/forgotPassword")
    public String toForgotPassword() {
        return "backend/forgotPassword";
    }

    /**
     * 點擊導覽與個人資料頁面內的登出按鈕登出
     *
     * @param session 傳入當前會話Session
     * @return 重導到登入頁面
     */
    @GetMapping("logout")
    public String logout(HttpSession session) {
        // 註銷session，會觸發SessionListener的登入狀態確認
        session.invalidate();

        return "redirect:/backend/login";
    }

    // 最新消息推播
    @GetMapping("/")
    public String news() {
        return "";
    }

    // 站內搜尋
    @GetMapping
    public String search() {
        return "";
    }

    /**
     * 從Redis資料庫內查詢資料
     *
     * @param key 傳入管理員編號
     * @return 返回登入狀態DTO
     */
    private LoginState getFromRedis(Integer key) {
        return itlRedisTemplate.opsForValue().get(key);
    }

    /**
     * 新增或修改Redis資料庫內的資料
     *
     * @param key 傳入管理員編號
     * @param loginState 傳入登入狀態
     */
    private void storeLoginstateInRedis(Integer key, LoginState loginState) {
        itlRedisTemplate.opsForValue().set(key, loginState);
    }

    /**
     * 刪除Redis資料庫內的資料
     *
     * @param key 傳入管理員編號
     */
    private void deleteRedisData(Integer key) {
        if (itlRedisTemplate.hasKey(key)) {
            itlRedisTemplate.delete(key);
        }
    }

}