package com.ren;

import com.iting.productorderdetail.entity.ProductOrderDetail;
import com.iting.productorderdetail.service.impl.ProductOrderDetailServiceImpl;
import com.ren.administrator.entity.Administrator;
import com.ren.administrator.dto.LoginState;
import com.ren.administrator.service.impl.AdministratorServiceImpl;
import com.ren.product.dto.ProductDTO;
import com.ren.product.entity.Product;
import com.ren.product.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ren.util.Constants.*;
import static com.ren.util.RandomStringGenerator.generateRandomString;
import static com.ren.util.Validator.*;

@Controller
@RequestMapping("/backend")
public class BackendIndexController {

    @Autowired
    @Qualifier("cookieStrInt")
    private RedisTemplate<String, Integer> stiRedisTemplate;

    @Autowired
    @Qualifier("failIP")
    private RedisTemplate<String, Integer> ipRedisTemplate;

    @Autowired
    @Qualifier("cookieStrStr")
    private StringRedisTemplate cookieRedisTemplate;

    @Autowired
    private AdministratorServiceImpl administratorSvc;

    @Autowired
    private ProductServiceImpl productSvc;

    @Autowired
    private ProductOrderDetailServiceImpl productOrderDetailSvc;

    /**
     * 透過商品與商品訂單的ORM關係來獲得商品的售出數量，統計後送到前端由chart.js呈現
     *
     * @return 返回(商品類別編號 : 商品名稱, 售出數量)的Map到前端顯示
     */
    @ModelAttribute("productSalMap")
    public Map<String, Integer> productList() {
        List<ProductOrderDetail> productOrderDetailList = productOrderDetailSvc.getAll();
        Map<String, Integer> productSalMap = new HashMap<>();
        for (var productOrderDetail : productOrderDetailList) {
            Integer productCatNo = productOrderDetail.getProduct().getProductCategory().getProductCatNo();
            String productName = productOrderDetail.getProduct().getProductName();
            String key = productCatNo + " : " + productName;
            Integer quantity = productOrderDetail.getProductOrdQty();
            productSalMap.compute(key, (k, v) -> (v == null) ? quantity : v + quantity);
        }
        return productSalMap;
    }

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
     * 註冊管理員，預設為最低權限
     *
     * @param administrator 管理員Entity
     * @param result 若輸入不符合格式，將錯誤訊息取出渲染到前端
     * @param model 將錯誤訊息與輸入的Entity(避免讓使用者重複輸入)渲染到前端
     * @return 錯誤時會forward回註冊頁面，成功時會重導到登錄頁面
     */
    @PostMapping("/signUp")
    public String signUp(@Valid Administrator administrator,
                         @RequestParam("repeatPwd") String repeatPwd,
                         BindingResult result,
                         ModelMap model,
                         RedirectAttributes redirectAttributes) {
        // 檢查是否有重複信箱
        List<Administrator> list = administratorSvc.getAll();
        for (Administrator admin : list) {
            model.addAttribute("administrator", administrator);
            if (admin.getAdmEmail().equals(administrator.getAdmEmail())) {
                model.addAttribute("emailError", "信箱重複");
                return "backend/register";
            }
        }

        // 判斷密碼與二次密碼是否相同
        if (!administrator.getAdmPwd().equals(repeatPwd)) {
            model.addAttribute("pwdError", "密碼與二次密碼不符!");
            // 順便將其他異常訊息帶回去
            if (result.hasErrors()) {
                model.addAttribute("administrator", administrator);
                model.addAttribute("errors", result.getAllErrors());
            }
            return "backend/register";
        }
        // 確認是否有無錯誤訊息
        if (result.hasErrors()) {
            model.addAttribute("administrator", administrator);
            model.addAttribute("errors", result.getAllErrors());
            return "backend/register";
        }

        administratorSvc.register(administrator);
        redirectAttributes.addAttribute("success", "帳號申請成功!");

        return "redirect:/backend/login";
    }

    /**
     * 前往登入頁面
     *
     * @return forward to backend login
     */
    @GetMapping("/login")
    public String toLogin() {
        return "backend/login";
    }

    /**
     * 登入頁面
     * 1.使用者可以輸入用戶名稱或Email，與密碼登入後台頁面
     * 2.密碼錯誤只能嘗試5次，當超過5次時會鎖定30分鐘
     * 3.提供自動登入服務，當使用者勾選自動登入時，將會在使用者的瀏覽器存入辨識使用者身分的cookie，
     * ，並存入相同的資料到Redis資料庫，日後可透過這個cookie辨識使用者身分，在Redis資料庫內搜尋是否
     * 有相關的使用者登入記錄，當確認有相關記錄時，會在AutoLogin Filter執行登入方法，詳情可查看
     * AutoLoginFilter，有提供相關聯的業務邏輯。
     *
     * @param userId 使用者輸入ID，後續透過正則表達式辨識輸入為信箱格式還是用戶名
     * @param admPwd 使用者密碼，使用Redis記錄錯誤次數，每當登入錯誤時會將嘗試次數記錄在Redis資料庫，
     *               並設定資料生命週期為30分，超過5次時則不予以嘗試機會。
     * @param autoLogin 當使用者勾選自動登入時，此值為1，不溝選擇預設為0
     * @param session 使用者會話，設計為當使用者登入期間會在Session內存入名為loginState登入狀態的DTO，
     *                因登入成功在邏輯上應該要有登入狀態，因此在這個方法內傳入用來存入loginState這個DTO。
     * @param req 用來獲取ServletContext()路徑，當設置Cookie的路徑時可以使用
     * @param res 用來將Cookie加進response傳回給前端
     * @param model 將錯誤訊息與administrator物件傳回前端渲染
     * @return 失敗會回到登入頁面(原頁面)，成功則重導到首頁
     */
    @PostMapping("/loginPage")
    public String login(@RequestParam String userId,
                        @RequestParam String admPwd,
                        @RequestParam Byte autoLogin,
                        HttpSession session,
                        HttpServletRequest req,
                        HttpServletResponse res,
                        ModelMap model) {
        if (userId == null || userId.trim().equals("")) {
            model.addAttribute("idError", "請輸入用戶名或註冊信箱!");
            return "backend/login";
        }

        // 宣告管理員與管理員編號，於後續參數驗證後賦值
        Administrator administrator = null;
        Integer admNo = null;

        // 確認是否為信箱格式，true則透過信箱搜尋使用者資訊，false則透過管理員編號搜尋使用者資訊
        if (validateEmail(userId)) {
            administrator = administratorSvc.getOneAdministrator(userId);
            // 確認是否有無這個資料
            if (administrator != null) {
                // 獲得管理員編號，之後存入Redis資料庫使用
                admNo = administrator.getAdmNo();
            }
        } else if (validateInteger(userId)){
            // 將管理員編號轉成 Integer，供Service方法使用
            admNo = Integer.valueOf(userId);
            administrator = administratorSvc.getOneAdministrator(admNo);
        }

        // 在驗證前將使用者Id放入model，如果使用者輸入錯誤，返回給使用者而不用重新輸入
        model.addAttribute("userId" ,userId);

        // 判斷是否有無這個帳號
        if (administrator == null) {
            model.addAttribute("idError", "不存在此用戶");
            return "backend/login";
        }

        Integer failTimes = getFailedTimes();
        // 密碼嘗試次數最多5次，如果錯誤次數少於等於5次，檢查密碼是否正確；若超過5次，直接導回去頁面
        if (failTimes < 5) {
            // 判斷密碼是否正確
            if (!admPwd.equals(administrator.getAdmPwd())) {
                // 執行登入失敗方法，將登入失敗次數記入Session
                failTimes = loginFailed(failTimes);
                if (failTimes == 5) {
                    model.addAttribute("pwdError", "您已達嘗試次數上限，請過30分鐘後再嘗試");
                } else {
                    model.addAttribute("pwdError", "密碼錯誤，剩餘嘗試次數: " + (5 - failTimes));
                }
                return "backend/login";
            }
        } else {
            model.addAttribute("pwdError", "您已超過嘗試次數，請等30分鐘後再嘗試");
            return "backend/login";
        }


        // 確認帳號是否已被登入
        if (administrator.getAdmLogin() == LOGIN_STATE_LOGIN && administrator.getAdmLogout() == LOGOUT_STATE_LOGIN) {
            System.out.println("帳號已被登入");
        }

        // 使用Service的登入方法，更改administrator的登入狀態，並傳回登入狀態DTO，
        // 於後續存入Session做權限、登入狀態驗證
        administratorSvc.login(administrator, session);

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
            session.setAttribute("random", random);
        }

        return "redirect:/backend/index";
    }

    /**
     * 前往忘記密碼頁面
     *
     * @return forward到忘記密碼頁面
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
    public String forgotPwd(@RequestParam String email,
                            ModelMap model,
                            RedirectAttributes redirectAttributes) {

        if (email.trim().equals("")) {
            model.addAttribute("errorMessage", "請勿空白!");
            return "backend/forgotPassword";
        }

        if (!validateEmail(email)) {
            model.addAttribute("errorMessage", "格式不符!請輸入信箱格式");
            return "backend/forgotPassword";
        } else if (administratorSvc.getOneAdministrator(email) == null){
            model.addAttribute("errorMessage", "查無此信箱");
            return "backend/forgotPassword";
        }

        // 如果寄送郵件失敗
        if (!administratorSvc.sendEmail(email)) {
            System.out.println("Controller:失敗");
            model.addAttribute("errorMessage", "發生錯誤，請重新嘗試");
            return "backend/forgotPassword";
        }
        System.out.println("信件成功寄出!");
        // 將參數以請求參數的方式透過url傳到重定向後的網頁
        redirectAttributes.addAttribute("success", "信件成功寄出!");
        // 也可以用以下寫法
        // String successMsg = URLEncoder.encode("信件成功寄出!", StandardCharsets.UTF_8.toString());
        // return "redirect:/backend/login" + "?success=" + successMsg;
        // 另有方法addFlashAttribute，是將資料存到Session
        return "redirect:/backend/login";
    }

    /**
     * 點擊導覽與個人資料頁面內的登出按鈕登出
     *
     * @param session 傳入當前會話Session
     * @return 重導到登入頁面
     */
    @GetMapping("/logout")
    public String logout(HttpSession session,
                         HttpServletRequest req,
                         HttpServletResponse res) {
        // 獲取登入狀態物件
        LoginState loginState = (LoginState) session.getAttribute("loginState");
        // 執行Service的登出方法，修改資料庫內的登入狀態與刪除Redis內的登入狀態資料
        administratorSvc.logout(loginState);

        // 移除cookie
        Optional<Cookie> userCookie = Optional.ofNullable(req.getCookies())
                .flatMap(this::userCookie);
        if (userCookie.isPresent()) {
            // 從Optional中獲取Cookie物件
            Cookie deleteCookie = userCookie.get();
            // 將Cookie期限設為0,並新增到BackendIndexController新增時的路徑，即可註銷Cookie
            deleteCookie.setMaxAge(0);
            deleteCookie.setPath(req.getContextPath() + "/backend");
            res.addCookie(deleteCookie);
            // 刪除Redis內的cookie資料
            cookieRedisTemplate.delete(deleteCookie.getValue());

        }
        // 註銷session
        session.invalidate();

        return "redirect:/backend/login";
    }


    /**
     * 在最新消息推播中在前端發出異步請求來獲得使用者職位，用來覺得要訂閱哪一個頻道
     *
     * @param session 傳入Session，用於取得loginState裡的職位身分
     * @return 返回職位
     */
    @GetMapping("/getUserTitle")
    @ResponseBody
    public String getUserTitle(HttpSession session) {
        LoginState loginState = (LoginState) session.getAttribute("loginState");
        return loginState.getTitleNo().toString();
    }

    /**
     * 於推播時判斷是不是自己發出的訊息，用來過濾
     *
     * @param session 傳入Session，用於取得loginState裡的管理員編號
     * @return 返回管理員編號，於前端比對傳入消息是不是自己的
     */
    @GetMapping("/getCurrentUser")
    @ResponseBody
    public Integer getCurrentUser(HttpSession session) {
        // 獲取當前用戶的身份驗證信息
        LoginState loginState = (LoginState) session.getAttribute("loginState");
        Integer admNo = loginState.getAdmNo();

        // 返回當前用戶的識別標識
        return admNo;
    }

    @GetMapping("/searchTest")
    public String toSearch() {

        return "backend/searchTest";
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String keyword,
                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                         @RequestParam(value = "size", defaultValue = "10") Integer size,
                         ModelMap model) {
        Page<Product> products = productSvc.searchProducts(keyword, page, size);

        System.out.println("getTotalPages()" + products.getTotalPages());
        System.out.println("getTotalElements()" + products.getTotalElements());

        System.out.println(keyword);
        System.out.println("當前page" + page + "當前size" + size);
        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("keyword", keyword); // 添加關鍵字到模型中以便在分頁導航中使用
        model.addAttribute("size", size); // 添加每頁大小到模型中以便在分頁導航中使用
        return "backend/searchTest";
    }

    @GetMapping("/administrator")
    public String toBackendAdministrator() {
        return "backend/administrator";
    }

    @GetMapping("/member")
    public String toBackendMember() {
        return "backend/member";
    }

    @GetMapping("/service")
    public String toBackendService() {
        return "backend/service";
    }

    @GetMapping("/column")
    public String toBackendColumn() {
        return "backend/column";
    }

    @GetMapping("/product")
    public String toBackendProduct() {
        return "backend/product";
    }

    @GetMapping("/rental")
    public String toBackendRental() {
        return "backend/rental";
    }

    @GetMapping("/alerts")
    public String toBackendAlerts() {
        return "backend/alerts";
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
                ipRedisTemplate.opsForValue().set(ipAddress, ++failTimes);
                ipRedisTemplate.expire(ipAddress, Duration.ofMinutes(30));
            } else {
                failTimes = ipRedisTemplate.opsForValue().get(ipAddress);
                ipRedisTemplate.opsForValue().set(ipAddress, ++failTimes);
                ipRedisTemplate.expire(ipAddress, Duration.ofMinutes(30));
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
            if (ipRedisTemplate.hasKey(ipAddress)) {
                failTimes = ipRedisTemplate.opsForValue().get(ipAddress);
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
    private BindingResult removeFieldError(Administrator administrator,
                                           BindingResult result,
                                           String removedFieldname) {

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

    /**
     * 搜尋使用者是否已有登入的記錄
     *
     * @param cookies 傳入使用者的cookies，並於後續使用Stream的filter方法，
     *                於方法內呼叫自定義check()方法將含使用者登入資訊的cookies過濾，
     *                並使用findFirst()方法找出第一個符合條件的Cookie
     * @return 返回Optional物件，如果沒有找到符合條件的cookie，則返回含null的Optional物件
     */
    private Optional<Cookie> userCookie(Cookie[] cookies) {
        return Stream.of(cookies)
                .filter(cookie -> check(cookie))
                .findFirst();
    }

    /**
     * 判斷是否有登入資訊
     * 1.從cookie裡面先尋找是否有名字為"autoLogin"的cookie
     * 2.其值為在登入時自動生成的亂數，分別存入Cookie("autoLogin", random)、Redis資料庫(random, admNo)
     * 3.在LoginState過濾器中，主要用途為找到"autoLogin"的Cookie，並把他註銷，讓使用者重新登入
     *
     * @param cookie 傳入Cookie物件，並呼叫Cookie的getter方法確認使否有符合登入資訊的Cookie
     * @return 如果兩者都符合則返回true，沒有則返回false
     */
    private boolean check(Cookie cookie) {
        return "autoLogin".equals(cookie.getName());
    }

}