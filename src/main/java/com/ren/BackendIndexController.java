package com.ren;

import com.ren.administrator.entity.Administrator;
import com.ren.administrator.dto.LoginState;
import com.ren.administrator.service.Impl.AdministratorServiceImpl;
import com.roger.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static com.ren.util.Constants.*;
import static com.ren.util.RandomStringGenerator.generateRandomString;
import static com.ren.util.Validator.validateEmail;

@Controller
@RequestMapping("/backend")
public class BackendIndexController {

    @Autowired
    @Qualifier("admStrInt")
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
     *
     *
     * @param administrator
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/signup")
    public String signUp(@Valid Administrator administrator, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("administrator", administrator);
            model.addAttribute("errors", result.getAllErrors());
            return "backend/administrator/addAdministrator";
        } else {
            System.out.println("新增成功");
        }
        administratorSvc.register(administrator);

        return "redirect:/backend/login";
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

    @PostMapping("/loginPage")
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

        Integer failTimes = getFailedTimes();
        // 密碼嘗試次數最多5次，如果錯誤次數少於等於5次，檢查密碼是否正確；若超過5次，直接導回去頁面
        if (failTimes <= 5) {
            // 判斷密碼是否正確
            if (!admPwd.equals(administrator.getAdmPwd())) {
                model.addAttribute("message", "密碼錯誤!");
                // 執行登入失敗方法，將登入失敗次數記入Session
                failTimes = loginFailed(failTimes);
                session.setAttribute("failTimes", failTimes);
                return "backend/login";
            }
        } else {
            model.addAttribute("message", "您已超過嘗試次數，請等30分鐘後再嘗試");
            return "backend/login";
        }


        // 確認帳號是否已被登入
        if (administrator.getAdmLogin() == LOGIN_STATE_LOGIN && administrator.getAdmLogout() == LOGOUT_STATE_LOGIN) {
            System.out.println("帳號已被登入");
        }

        // 使用Service的登入方法，更改administrator的登入狀態，並傳回登入狀態DTO，
        // 於後續存入Session做權限、登入狀態驗證
        LoginState loginState = administratorSvc.login(administrator, session.getId());
        session.setAttribute("loginState", loginState);

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

        System.out.println("登入成功!!!");

        return "redirect:/backend/index";
    }

    /**
     *
     *
     * @return
     */
    @GetMapping("/forgotPassword")
    public String toForgotPwd() {
        return "backend/forgotPassword";
    }

    /**
     * 密碼遺失時可透過輸入註冊信箱來重新獲取帳戶密碼
     *
     * @param email 帳戶註冊信箱
     * @param model 用於將錯誤信息傳到view
     * @return 錯誤則會forward到原網址，成功則會重導至登入頁面
     */
    @PostMapping("/sendEmail")
    public String forgotPwd(@RequestParam String email, ModelMap model) {
        if (!validateEmail(email)) {
            model.addAttribute("errorMessage", "格式不符!請輸入信箱格式");
            return "backend/forgotPassword";
        } else if (administratorSvc.getOneAdministrator(email) == null){
            model.addAttribute("errorMessage", "查無此信箱");
            return "backend/forgotPassword";
        }

        // 如果寄送郵件失敗
        if (!administratorSvc.sendEmail(email)) {
            model.addAttribute("errorMessage", "發生錯誤，請重新嘗試");
            return "backend/forgotPassword";
        }

        return "redirect:/backend/login";
    }

    /**
     * 點擊導覽與個人資料頁面內的登出按鈕登出
     *
     * @param session 傳入當前會話Session
     * @return 重導到登入頁面
     */
    @GetMapping("logout")
    public String logout(HttpSession session) {
        // 獲取登入狀態物件
        LoginState loginState = (LoginState) session.getAttribute("loginState");
        // 執行Service的登出方法，修改資料庫內的登入狀態與刪除Redis內的登入狀態資料
        administratorSvc.logout(loginState);
        // 註銷session
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
     * 記錄密碼輸入錯誤的次數，
     * 透過使用者IP記錄，記錄時間為30分鐘
     *
     * @param failTimes 密碼輸入錯誤次數
     * @return 返回 failTimes + 1
     */
    private Integer loginFailed(Integer failTimes) {
        try {
            // 取得使用者IP
            InetAddress localHost = InetAddress.getLocalHost();
            String ipAddress = localHost.getHostAddress();
            // 如果密碼輸入失敗次數為0
            if (failTimes == 0) {
                stiRedisTemplate.opsForValue().set(ipAddress, ++failTimes);
                stiRedisTemplate.expire(ipAddress, Duration.ofMinutes(30));
            } else {
                failTimes = stiRedisTemplate.opsForValue().get(ipAddress);
                stiRedisTemplate.opsForValue().set(ipAddress, ++failTimes);
                stiRedisTemplate.expire(ipAddress, Duration.ofMinutes(30));
            }

            System.out.println("本地主機的 IP 地址：" + ipAddress + ", 密碼輸入錯誤次數" + failTimes);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return failTimes;
    }

    /**
     * 取得密碼輸入錯誤次數
     *
     * @return 從Redis資料庫取得錯誤次數，若沒有則設為0
     */
    private Integer getFailedTimes() {
        Integer failTimes = null;
        try {
            // 取得使用者IP
            InetAddress localHost = InetAddress.getLocalHost();
            String ipAddress = localHost.getHostAddress();
            if (stiRedisTemplate.hasKey(ipAddress)) {
                failTimes = stiRedisTemplate.opsForValue().get(ipAddress);
            } else {
                failTimes = 0;
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return failTimes;
    }

    /**
     * 去除指定字段的錯誤信息並返回更新後的 BindingResult。
     * 此方法會過濾掉 BindingResult 中與指定字段相關的錯誤信息，然後將剩餘的錯誤信息添加到新的 BindingResult 中。
     *
     * @param administrator 要操作的 Entity 對象。
     * @param result 目前的 BindingResult，其中包含 Entity 的錯誤信息。
     * @param removedFieldname 要去除錯誤信息的字段名稱。
     * @return 更新後的 BindingResult，其中去除了指定字段的錯誤信息。
     */
    private BindingResult removeFieldError(Administrator administrator, BindingResult result, String removedFieldname) {

        // 過濾掉指定字段的錯誤信息
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        // 創建新的 BindingResult，關聯 Entity 物件
        result = new BeanPropertyBindingResult(administrator, "administrator");
        // 將過濾後的錯誤信息添加到新的 BindingResult 中
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        // 返回修改後的 BindingResult
        return result;
    }

}