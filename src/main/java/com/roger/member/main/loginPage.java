package com.roger.member.main;

public class loginPage {

    // 其他 import 和類的定義部分


//    /**
//     * 以彈出視窗方式處理登入頁面的 POST 請求方法。
//     *
//     * @param autoLoginMember    自動登入會員標誌，預設值為 0，1 表示要自動登入
//     * @param session            HTTP 會話
//     * @param response           HTTP 響應
//     * @param request            HTTP 請求
//     * @return 如果請求參數無效或為空，則返回 400 錯誤；如果會員不存在，返回 404 錯誤；如果會員被禁止使用，返回 403 錯誤；
//     *         如果登入失敗，則返回 401 錯誤；如果未知錯誤，則返回 500 錯誤；如果嘗試次數達到限制，則返回 401 錯誤；
//     *         如果登入成功，則返回 302 轉發到原始請求的 URI。
//     */
//    @PostMapping("/loginPageByPopup")
//    @ResponseBody
//    public ResponseEntity<String> loginPageByPopup(@RequestParam("identifier") String identifier,
//                                                   @RequestParam("password") String memPwd,
//                                                   @RequestParam(value = "autoLoginMember", defaultValue = "0") Byte autoLoginMember,
//                                                   HttpSession session,
//                                                   HttpServletResponse response,
//                                                   HttpServletRequest request) {
//
//
//        // 檢查帳號或信箱和密碼是否為空
//        if (identifier == null || identifier.isEmpty() || memPwd == null || memPwd.isEmpty()) {
//            String errorMessage = identifier == null || identifier.isEmpty() ? "帳號或信箱不能空白，請重新輸入!" : "密碼不能空白，請重新輸入!";
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//        }
//
//        // 獲取重定向的 URI 或設置默認值
//        String uri = session.getAttribute("URI") == null ? "/" : session.getAttribute("URI").toString();
//        String projectUri = session.getServletContext().getContextPath();
//
//        // 從會話中獲取儲存的登錄失敗次數 Map 物件
//        Map<String, Integer> loginAttemptsMap = (Map<String, Integer>) session.getAttribute("loginAttemptsMap");
//        if (loginAttemptsMap == null) {
//            // 如果 Map 尚未初始化，創建一個新的 Map
//            loginAttemptsMap = new HashMap<>();
//            session.setAttribute("loginAttemptsMap", loginAttemptsMap);
//        }
//
//        // 根據請求路徑確定使用帳號還是信箱登入
//        // 宣告會員跟會員編號，於後續參數驗證後賦值
//        Member loginData = null;
//        Integer memNo = null;
//        Member existingMember = null;
//        String memberKey = null;
//
//        if (validateEmail(identifier)) {
//            // 使用信箱進行登入
//            loginData = memberService.login(identifier, memPwd);
//            existingMember = memberService.findByMail(identifier);
//        } else if (validateAccout(identifier)){
//            // 使用帳號進行登入
//            loginData = memberService.loginByMemAcc(identifier, memPwd);
//            existingMember = memberService.findByMemAcc(identifier);
//        }
//
//        // 檢查會員是否存在於資料庫中
//        if (existingMember == null) {
//            // 根據不同的正則表達式返回不同的錯誤消息
//            if (validateEmail(identifier)) {
//                // 信箱登入
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("沒有該會員信箱，請確認您的信箱。");
//            } else if (validateAccout(identifier)){
//                // 帳號登入
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("沒有該會員帳號，請確認您的帳號。");
//            } else {
//                // 其他錯誤
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("會員帳號跟信箱有誤請重新確認");
//            }
//        }
//
//        // 使用會員ID作為 `loginAttemptsMap` 的鍵，以跟蹤同一會員的登入失敗次數
//        memberKey = existingMember.getMemNo().toString();
//        Integer attemptCount = loginAttemptsMap.getOrDefault(memberKey, 0);
//
//        // 如果會員狀態是 2，表示會員已經被禁止使用
//        if (existingMember.getMemStat() == 2) {
//            // 刪除無權限會員的標記
//            redisTemplate.delete("noFun" + existingMember.getMemNo().toString());
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("此會員已無權限，請洽詢相關的工作人員");
//        }
//
//        // 如果登錄失敗
//        if (loginData == null) {
//            // 增加該會員的登錄失敗次數
//            attemptCount++;
//            loginAttemptsMap.put(memberKey, attemptCount);
//
//            // 如果嘗試次數達到或超過五次，則停權該會員並返回提示訊息
//            if (attemptCount >= 5) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("此會員已遭停權，請聯繫官方客服尋求幫助");
//            }
//
//            // 細分登入失敗的原因
//            if (existingMember != null) {
//                // 檢查密碼是否正確
//                if (!existingMember.getMemPwd().equals(memPwd)) {
//                    // 密碼錯誤
//                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("密碼輸入錯誤，請重新輸入!");
//                } else {
//                    // 因為會員存在但密碼正確，所以可能是其他原因造成的錯誤
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("未知的登入錯誤，請稍後再試。");
//                }
//            } else {
//                // 會員不存在，所以可能是帳號或信箱錯誤
//                if (validateEmail(identifier)) {
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("信箱錯誤，請重新輸入!");
//                } else if (validateAccout(identifier)){
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("帳號錯誤，請重新輸入!");
//                }
//            }
//
//            if (5 - attemptCount <= 3) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("您剩餘 " + (5 - attemptCount) + " 次嘗試次數");
//            }
//        }
//
//        // 登錄成功，清除該會員登錄失敗的次數
//        loginAttemptsMap.remove(memberKey);
//
//        // 獲取會員的通知
//        List<Notice> noticeList = noticeService.findNoticesByMemberMemNo(existingMember.getMemNo());
//
//        // 獲取上架中的文章列表
//        List<ColumnArticle> publishedArticles = columnArticleService.getPublishedArticles();
//
//        if (!publishedArticles.isEmpty()) {
//            ColumnArticle firstArticle = publishedArticles.get(0);
//            // 現在您可以使用 firstArticle 來訪問第一個文章的屬性和方法
//            // 例如：firstArticle.getTitle()，firstArticle.getContent()，等等
//            session.setAttribute("onePublishedArticles", firstArticle.getArtNo());
//        }
//
//        List<ColumnArticle> columnArticles = columnArticleService.findAll();
//        // modelMap.addAttribute("columnArticles", columnArticles);
//
//        // 獲取未讀取通知的數量
//        int unreadNoticeCount = noticeService.getUnreadNoticeCount(existingMember);
//
//        System.out.println(noticeList);
//
//        // 從會話中移除 "URI" 屬性，以避免重複重定向
//        session.removeAttribute("URI");
//
//        // 登入成功後，將會員訊息和通知訊息儲存到會話中
//        session.setAttribute("loginsuccess", loginData);
//        session.setAttribute("noticeList", noticeList);
//        session.setAttribute("unreadNoticeCount", unreadNoticeCount);
//
//        if (autoLoginMember == null) {
//            autoLoginMember = 0;
//        }
//
//        // 確認會員是否要自動登入
//        if (autoLoginMember != null && autoLoginMember == YES) {
//            // 如果 autoLoginMember 不為空且值為 1，表示用戶要自動登入
//            // 添加處理自動登入的邏輯，例如生成一個 token，存入 cookie 和 redis
//            // 這裡的範例代碼是將自動登入的信息存入 redis 和 cookie 中
//            // 生成名為 autoLoginMember 的 cookie，其值設置為一個亂數生成的字符串，分別存入給會員與 redis 資料庫，做身分核對
//            String random = generateRandomString(40);
//            Cookie cookie = new Cookie("autoLoginMember", random);
//
//            // 設置存活 7 天
//            cookie.setMaxAge(604800);
//
//            // 設置 cookie 的路徑為 / frontend，當訪問所有的前台網頁都可以獲取這個 cookie
//            cookie.setPath(request.getContextPath() + "/frontend");
//            response.addCookie(cookie);
//            memStrIntRedisTemplate.opsForValue().set(random, memNo);
//            System.out.println("cookie 存入");
//            System.out.println("自動登入信息已存入");
//        }
//
//        // 重定向到原始請求的 URI
//        String redirectUri = session.getAttribute("URI") == null ? "/" : session.getAttribute("URI").toString();
//        String projectUri2 = session.getServletContext().getContextPath();
//        String fullRedirectUri = projectUri + redirectUri;
//        return ResponseEntity.status(HttpStatus.FOUND).body(fullRedirectUri);
//    }

//    /**
//     * 控制器方法，用於處理彈出式登錄。
//     *
//     * @param identifier         使用者輸入的識別符（電子郵件或帳戶）。
//     * @param memPwd             使用者輸入的密碼。
//     * @param autoLoginMember    標誌，指示是否要求自動登錄。
//     * @param modelMap           ModelMap 物件，用於為視圖添加屬性。
//     * @param session            HttpSession 物件，用於管理會話數據。
//     * @param response           HttpServletResponse 物件，用於發送響應。
//     * @param request            HttpServletRequest 物件，用於處理請求。
//     * @return                   視圖名稱或如果重定向則為 null。
//     */
//    @PostMapping("/loginPageByPopup")
//    public String loginPageByPopup(@ModelAttribute("identifier") String identifier,
//                                   @ModelAttribute("password") String memPwd,
//                                   @RequestParam(value = "autoLoginMember", defaultValue = "0") Byte autoLoginMember,
//                                   ModelMap modelMap,
//                                   HttpSession session,
//                                   HttpServletResponse response,
//                                   HttpServletRequest request) {
//
//        // 獲取重定向的 URI 或設置默認值
//        String uri = session.getAttribute("URI") == null ? "/" : session.getAttribute("URI").toString();
//        String projectUri = session.getServletContext().getContextPath();
//
//        // 檢查帳號或信箱和密碼是否為空
//        if (identifier.isEmpty() || memPwd.isEmpty()) {
//            if (identifier.isEmpty()) {
//                modelMap.addAttribute("message", "帳號或信箱不能空白，請重新輸入!");
//            } else {
//                modelMap.addAttribute("message", "密碼不能空白，請重新輸入!");
//            }
//            return "frontend/member/loginMember";
//        }
//
//        // 從會話中獲取儲存的登錄失敗次數 Map 物件
//        Map<String, Integer> loginAttemptsMap = (Map<String, Integer>) session.getAttribute("loginAttemptsMap");
//        if (loginAttemptsMap == null) {
//            // 如果 Map 尚未初始化，創建一個新的 Map
//            loginAttemptsMap = new HashMap<>();
//            session.setAttribute("loginAttemptsMap", loginAttemptsMap);
//        }
//
//        // 根據請求路徑確定使用帳號還是信箱登入
//        // 宣告會員跟會員編號，於後續參數驗證後賦值
//        Member loginData = null;
//        Integer memNo = null;
//        Member existingMember = null;
//        String memberKey = null;
//
//        if (validateEmail(identifier)) {
//            // 使用信箱進行登入
//            loginData = memberService.login(identifier, memPwd);
//            existingMember = memberService.findByMail(identifier);
//        } else if (validateAccout(identifier)){
//            // 使用帳號進行登入
//            loginData = memberService.loginByMemAcc(identifier, memPwd);
//            existingMember = memberService.findByMemAcc(identifier);
//        }
//
//        // 檢查會員是否存在於資料庫中
//        if (existingMember == null) {
//            // 根據不同的正則表達式返回不同的錯誤消息
//            if (validateEmail(identifier)) {
//                // 信箱登入
//                modelMap.addAttribute("message", "沒有該會員信箱，請確認您的信箱。");
//            } else if (validateAccout(identifier)){
//                // 帳號登入
//                modelMap.addAttribute("message", "沒有該會員帳號，請確認您的帳號。");
//            } else {
//                // 其他錯誤
//                modelMap.addAttribute("message", "會員帳號跟信箱有誤請重新確認");
//            }
//            return "frontend/member/loginMember";
//        }
//
//        // 使用會員ID作為 `loginAttemptsMap` 的鍵，以跟蹤同一會員的登入失敗次數
//        memberKey = existingMember.getMemNo().toString();
//        Integer attemptCount = loginAttemptsMap.getOrDefault(memberKey, 0);
//
//        // 如果會員狀態是 2，表示會員已經被禁止使用
//        if (existingMember.getMemStat() == 2) {
//            // 刪除無權限會員的標記
//            redisTemplate.delete("noFun" + existingMember.getMemNo().toString());
//            modelMap.addAttribute("noFun", "此會員已無權限，請洽詢相關的工作人員");
//            return "frontend/member/loginMember";
//        }
//
//        // 如果登錄失敗
//        if (loginData == null) {
//            // 增加該會員的登錄失敗次數
//            attemptCount++;
//            loginAttemptsMap.put(memberKey, attemptCount);
//
//            // 如果嘗試次數達到或超過五次，則停權該會員並返回提示訊息
//            if (attemptCount >= 5) {
//                modelMap.addAttribute("message", "此會員已遭停權，請聯繫官方客服尋求幫助");
//                // 停權會員
//                if (validateEmail(identifier)) {
//                    banMemberByMail(identifier, session, modelMap);
//                } else if (validateAccout(identifier)){
//                    banMemberByMemAcc(identifier, session, modelMap);
//                }
//                return "frontend/member/loginMember";
//            }
//
//            // 細分登入失敗的原因
//            if (existingMember != null) {
//                // 檢查密碼是否正確
//                if (!existingMember.getMemPwd().equals(memPwd)) {
//                    // 密碼錯誤
//                    modelMap.addAttribute("message", "密碼輸入錯誤，請重新輸入!");
//                } else {
//                    // 因為會員存在但密碼正確，所以可能是其他原因造成的錯誤
//                    modelMap.addAttribute("message", "未知的登入錯誤，請稍後再試。");
//                }
//            } else {
//                // 會員不存在，所以可能是帳號或信箱錯誤
//                if (validateEmail(identifier)) {
//                    modelMap.addAttribute("message", "信箱錯誤，請重新輸入!");
//                } else if (validateAccout(identifier)){
//                    modelMap.addAttribute("message", "帳號錯誤，請重新輸入!");
//                }
//            }
//
//            if (5 - attemptCount <= 3) {
//                modelMap.addAttribute("message", "您剩餘 " + (5 - attemptCount) + " 次嘗試次數");
//            }
//
//            return "frontend/member/loginMember";
//        }
//
//        // 登錄成功，清除該會員登錄失敗的次數
//        System.out.println("登入成功");
//        loginAttemptsMap.remove(memberKey);
//
//        // FIXME 修改
//        // 獲取會員的通知
//        Notice notice = noticeService.getOneByMember(existingMember);
//
//        // 從會話中移除 "URI" 屬性，以避免重複重定向
//        session.removeAttribute("URI");
//
//        // 登入成功後，將會員訊息和通知訊息儲存到會話中
//        session.setAttribute("loginsuccess", loginData);
//        session.setAttribute("notice", notice);
//
//        System.out.println("autoLoginMember: " + autoLoginMember);
//
//        memNo = existingMember.getMemNo();
//        // 確認會員是否要自動登入
//        if (autoLoginMember != null && autoLoginMember == YES) {
//            // 如果 autoLoginMember 不為空且值為 1，表示用戶要自動登入
//            // 添加處理自動登入的邏輯，例如生成一個 token，存入 cookie 和 redis
//            // 這裡的範例代碼是將自動登入的信息存入 redis 和 cookie 中
//            // 生成名為 autoLoginMember 的 cookie，其值設置為一個亂數生成的字符串，分別存入給會員與 redis 資料庫，做身分核對
//            String random = generateRandomString(40);
//            Cookie cookie = new Cookie("autoLoginMember", random);
//
//            // 設置存活 7 天
//            cookie.setMaxAge(604800);
//
//            // 設置 cookie 的路徑為 / frontend，當訪問所有的前台網頁都可以獲取這個 cookie
//            cookie.setPath(request.getContextPath() + "/frontend");
//            response.addCookie(cookie);
//            memStrIntRedisTemplate.opsForValue().set(random, memNo);
//            System.out.println("cookie 存入");
//            System.out.println("自動登入信息已存入");
//        }
//
//        // 重定向到原始請求的 URI
//        try {
//            response.sendRedirect(projectUri + uri);
//            return null;
//        } catch (IOException e) {
//            // 處理重定向的 IOException
//            e.printStackTrace();
//        }
//
//        return "frontend/member/loginMember";
//    }

    //    /**
//     * 處理會員登入請求 (信箱跟密碼)。
//     * 此方法處理 HTTP POST 請求到 '/frontend/member/loginPage' URL 路徑，
//     * 接收會員的電子郵件和密碼，並嘗試進行登入操作。
//     * 如果登入成功，將會員的訊息儲存到會話中，並重定向到原始請求的 URI。
//     * 如果會員狀態是被禁止使用（狀態為2），則刪除該會員的無權限標記，並顯示相關訊息。
//     * 如果登入失敗，增加會員的登入嘗試次數。
//     * 如果會員的登入嘗試次數達到或超過五次，將停權該會員並返回相關提示消息。
//     * 如果剩餘嘗試次數少於等於三次，顯示剩餘的嘗試次數。
//     *
//     * @param memMail 會員的電子郵件。
//     * @param memPwd 會員的密碼。
//     * @param modelMap 包含模型屬性的 'ModelMap'。
//     * @param session 用於存儲和訪問會員訊息的會話對象。
//     * @param response 用於處理重定向的回應對象。
//     * @return 登入成功時，返回 null 並重定向到原始請求的 URI；登入失敗時，返回登入頁面名稱並顯示錯誤消息。
//     */
//    @PostMapping("/loginPage2")
//    public String loginPage2(@ModelAttribute("mail") String memMail,
//                             @ModelAttribute("account") String memAcc,
//                             @ModelAttribute("password") String memPwd,
//                             ModelMap modelMap,
//                             HttpSession session,
//                             HttpServletResponse response) {
//
//        // 獲取重定向的 URI 或設置默認值
//        String uri = session.getAttribute("URI") == null ? "/" : session.getAttribute("URI").toString();
//
//        // 獲取項目的根路徑
//        String projectUri = session.getServletContext().getContextPath();
//
//        // 檢查郵件和密碼是否為空
//        if (memMail.isEmpty() || memPwd.isEmpty()) {
//            modelMap.addAttribute("message", "信箱或密碼不能空白，請重新輸入!");
//            return "frontend/member/loginMember";
//        }
//
//        // 從會話中獲取儲存的登錄失敗次數 Map 物件
//        Map<String, Integer> loginAttemptsMap = (Map<String, Integer>) session.getAttribute("loginAttemptsMap");
//        if (loginAttemptsMap == null) {
//            // 如果 Map 尚未初始化，創建一個新的 Map
//            loginAttemptsMap = new HashMap<>();
//            session.setAttribute("loginAttemptsMap", loginAttemptsMap);
//        }
//
//        // 檢查會員的電子郵件是否已存在於 Map 中
//        Integer attemptCount = loginAttemptsMap.getOrDefault(memMail, 0);
//
//        // 嘗試登入
//
//        Member loginData = memberService.login(memMail, memPwd);
//
//        // 檢查會員是否存在於資料庫中
//        Member existingMember = memberService.findByMail(memMail);
//
//        if (existingMember == null) {
//            // 如果會員不存在，顯示沒有該會員信箱的提示訊息
//            modelMap.addAttribute("message", "沒有該會員信箱，請確認您的電子郵件地址。");
//            return "frontend/member/loginMember";
//        }
//
//        // 如果會員狀態是 2，表示會員已經被禁止使用
//        if (existingMember.getMemStat() == 2) {
//            // 從 Redis 中刪除無權限會員的標記
//            redisTemplate.delete("noFun" + existingMember.getMemNo().toString());
//
//            // 在 ModelMap 中添加一條提示消息，告訴用戶他們的帳號已經沒有權限
//            modelMap.addAttribute("noFun", "此帳號已無權限，請洽詢相關的工作人員");
//
//            // 返回到登入頁面，並顯示無權限提示
//            return "frontend/member/loginMember";
//        }
//
//        // 如果登錄失敗
//        if (loginData == null) {
//            // 增加該會員的登錄失敗次數
//            attemptCount++;
//
//            // 將更新的次數儲存到 Map 中
//            loginAttemptsMap.put(memMail, attemptCount);
//
//            // 如果嘗試次數達到或超過五次，則停權該會員並返回提示訊息
//            if (attemptCount >= 5) {
//                modelMap.addAttribute("message", "此會員帳號已遭停權，請聯繫官方客服尋求幫助");
//                // 使用 banMemberByMail 方法停權會員
//                banMemberByMail(memMail, session, modelMap);
//                // 返回到登入頁面，並顯示無權限提示
//                return "frontend/member/loginMember";
//            }
//
//            // 提示錯誤訊息
//            modelMap.addAttribute("message", "信箱或密碼輸入錯誤，請重新輸入!");
//            if (5 - attemptCount <= 3) {
//                modelMap.addAttribute("message", "您剩餘" + (5 - attemptCount) + "次嘗試次數");
//            }
//
//            return "frontend/member/loginMember";
//        }
//
//        // 如果登錄成功，清除該會員登錄失敗的次數
//        loginAttemptsMap.remove(memMail);
//
//        // FIXME 修改
//        // 獲取會員的通知
//        Notice notice = noticeService.getOneByMember(loginData);
//
//        // 從會話中移除 "URI" 屬性，以避免重複重定向
//        session.removeAttribute("URI");
//
//        // 登入成功後，將會員信息和通知信息儲存到會話中
//        // `loginsuccess` 屬性存儲當前登入的會員信息 (`loginData`)
//        // `notice` 屬性存儲與該會員相關的通知信息 (`notice`)
//        session.setAttribute("loginsuccess", loginData);
//        session.setAttribute("notice", notice);
//
//        // 重定向到原始請求的 URI
//        try {
//            response.sendRedirect(projectUri + uri);
//            return null;
//        } catch (IOException e) {
//            // 處理重定向的 IOException
//            e.printStackTrace();
//        }
//
//        return "frontend/member/loginMember";
//    }
//
//    /**
//     * 處理會員登入請求 (信箱跟密碼)。
//     * 此方法處理 HTTP POST 請求到 '/frontend/member/loginPage' URL 路徑，
//     * 接收會員的電子郵件和密碼，並嘗試進行登入操作。
//     * 如果登入成功，將會員的訊息儲存到會話中，並重定向到原始請求的 URI。
//     * 如果會員狀態是被禁止使用（狀態為2），則刪除該會員的無權限標記，並顯示相關訊息。
//     * 如果登入失敗，增加會員的登入嘗試次數。
//     * 如果會員的登入嘗試次數達到或超過五次，將停權該會員並返回相關提示消息。
//     * 如果剩餘嘗試次數少於等於三次，顯示剩餘的嘗試次數。
//     *
//     * @param memMail 會員的電子郵件。
//     * @param memPwd 會員的密碼。
//     * @param modelMap 包含模型屬性的 'ModelMap'。
//     * @param session 用於存儲和訪問會員訊息的會話對象。
//     * @param response 用於處理重定向的回應對象。
//     * @return 登入成功時，返回 null 並重定向到原始請求的 URI；登入失敗時，返回登入頁面名稱並顯示錯誤消息。
//     */
//    @PostMapping("/loginPage")
//    public String loginPage(@ModelAttribute("mail") String memMail,
//                            @ModelAttribute("password") String memPwd,
//                            ModelMap modelMap,
//                            HttpSession session,
//                            HttpServletResponse response) {
//
//        // 獲取重定向的 URI 或設置默認值
//        String uri = session.getAttribute("URI") == null ? "/" : session.getAttribute("URI").toString();
//
//        // 獲取項目的根路徑
//        String projectUri = session.getServletContext().getContextPath();
//
//        // 檢查郵件和密碼是否為空
//        if (memMail.isEmpty() || memPwd.isEmpty()) {
//            modelMap.addAttribute("message", "信箱或密碼不能空白，請重新輸入!");
//            return "frontend/member/loginMember";
//        }
//
//        // 從會話中獲取儲存的登錄失敗次數 Map 物件
//        Map<String, Integer> loginAttemptsMap = (Map<String, Integer>) session.getAttribute("loginAttemptsMap");
//        if (loginAttemptsMap == null) {
//            // 如果 Map 尚未初始化，創建一個新的 Map
//            loginAttemptsMap = new HashMap<>();
//            session.setAttribute("loginAttemptsMap", loginAttemptsMap);
//        }
//
//        // 檢查會員的電子郵件是否已存在於 Map 中
//        Integer attemptCount = loginAttemptsMap.getOrDefault(memMail, 0);
//
//        // 嘗試登入
//        Member loginData = memberService.login(memMail, memPwd);
//
//        // 檢查會員是否存在於資料庫中
//        Member existingMember = memberService.findByMail(memMail);
//
//        if (existingMember == null) {
//            // 如果會員不存在，顯示沒有該會員信箱的提示訊息
//            modelMap.addAttribute("message", "沒有該會員信箱，請確認您的電子郵件地址。");
//            return "frontend/member/loginMember";
//        }
//
//        // 如果會員狀態是 2，表示會員已經被禁止使用
//        if (existingMember.getMemStat() == 2) {
//            // 從 Redis 中刪除無權限會員的標記
//            redisTemplate.delete("noFun" + existingMember.getMemNo().toString());
//
//            // 在 ModelMap 中添加一條提示消息，告訴用戶他們的帳號已經沒有權限
//            modelMap.addAttribute("noFun", "此帳號已無權限，請洽詢相關的工作人員");
//
//            // 返回到登入頁面，並顯示無權限提示
//            return "frontend/member/loginMember";
//        }
//
//        // 如果登錄失敗
//        if (loginData == null) {
//            // 增加該會員的登錄失敗次數
//            attemptCount++;
//
//            // 將更新的次數儲存到 Map 中
//            loginAttemptsMap.put(memMail, attemptCount);
//
//            // 如果嘗試次數達到或超過五次，則停權該會員並返回提示訊息
//            if (attemptCount >= 5) {
//                modelMap.addAttribute("message", "此會員帳號已遭停權，請聯繫官方客服尋求幫助");
//                // 使用 banMemberByMail 方法停權會員
//                banMemberByMail(memMail, session, modelMap);
//                // 返回到登入頁面，並顯示無權限提示
//                return "frontend/member/loginMember";
//            }
//
//            // 提示錯誤訊息
//            modelMap.addAttribute("message", "信箱或密碼輸入錯誤，請重新輸入!");
//            if (5 - attemptCount <= 3) {
//                modelMap.addAttribute("message", "您剩餘" + (5 - attemptCount) + "次嘗試次數");
//            }
//
//            return "frontend/member/loginMember";
//        }
//
//        // 如果登錄成功，清除該會員登錄失敗的次數
//        loginAttemptsMap.remove(memMail);
//
//        // FIXME 修改
//        // 獲取會員的通知
//        Notice notice = noticeService.getOneByMember(loginData);
//
//        // 從會話中移除 "URI" 屬性，以避免重複重定向
//        session.removeAttribute("URI");
//
//        // 登入成功後，將會員信息和通知信息儲存到會話中
//        // `loginsuccess` 屬性存儲當前登入的會員信息 (`loginData`)
//        // `notice` 屬性存儲與該會員相關的通知信息 (`notice`)
//        session.setAttribute("loginsuccess", loginData);
//        session.setAttribute("notice", notice);
//
//        // 重定向到原始請求的 URI
//        try {
//            response.sendRedirect(projectUri + uri);
//            return null;
//        } catch (IOException e) {
//            // 處理重定向的 IOException
//            e.printStackTrace();
//        }
//
//        return "frontend/member/loginMember";
//    }
//
//    /**
//     * 處理會員登入請求 (帳號跟密碼)。
//     * 此方法處理 HTTP POST 請求到 '/frontend/member/loginPageByMemAccount' URL 路徑，
//     * 接收會員的帳號和密碼，並嘗試進行登入操作。
//     * 如果登入成功，將會員的訊息儲存到會話中，並重定向到原始請求的 URI。
//     * 如果會員狀態是被禁止使用（狀態為2），則刪除該會員的無權限標記，並顯示相關訊息。
//     * 如果登入失敗，增加會員的登入嘗試次數。
//     * 如果會員的登入嘗試次數達到或超過五次，將停權該會員並返回相關提示消息。
//     * 如果剩餘嘗試次數少於等於三次，顯示剩餘的嘗試次數。
//     *
//     * @param memAcc 會員的帳號。
//     * @param memPwd 會員的密碼。
//     * @param modelMap 包含模型屬性的 'ModelMap'。
//     * @param session 用於存儲和訪問會員訊息的會話對象。
//     * @param response 用於處理重定向的回應對象。
//     * @return 登入成功時，返回 null 並重定向到原始請求的 URI；登入失敗時，返回登入頁面名稱並顯示錯誤消息。
//     */
//    @PostMapping("/loginPageByMemAccount")
//    public String loginPageByMemAccount(@ModelAttribute("account") String memAcc,
//                                        @ModelAttribute("password") String memPwd,
//                                        ModelMap modelMap, HttpSession session,
//                                        HttpServletResponse response) {
//
//        // 獲取重定向的 URI 或設置默認值
//        String uri = session.getAttribute("URI") == null ? "/" : session.getAttribute("URI").toString();
//
//        // 獲取項目的根路徑
//        String projectUri = session.getServletContext().getContextPath();
//
//        // 檢查郵件和密碼是否為空
//        if (memAcc.isEmpty() || memPwd.isEmpty()) {
//            modelMap.addAttribute("message", "帳號或密碼不能空白，請重新輸入!");
//            return "frontend/member/loginMember";
//        }
//
//        // 從會話中獲取儲存的登錄失敗次數 Map 物件
//        Map<String, Integer> loginAttemptsMap = (Map<String, Integer>) session.getAttribute("loginAttemptsMap");
//        if (loginAttemptsMap == null) {
//            // 如果 Map 尚未初始化，創建一個新的 Map
//            loginAttemptsMap = new HashMap<>();
//            session.setAttribute("loginAttemptsMap", loginAttemptsMap);
//        }
//
//        // 檢查會員的電子郵件是否已存在於 Map 中
//        Integer attemptCount = loginAttemptsMap.getOrDefault(memAcc, 0);
//
//        // 嘗試登入
//        Member loginData = memberService.loginByMemAcc(memAcc, memPwd);
//
//        // 檢查會員是否存在於資料庫中
//        Member existingMember = memberService.findByMemAcc(memAcc);
//
//        if (existingMember == null) {
//            // 如果會員不存在，顯示沒有該會員帳號的提示訊息
//            modelMap.addAttribute("message", "沒有該會員帳號，請確認您的帳號。");
//            return "frontend/member/loginMemberByAccount";
//        }
//
//        // 如果會員狀態是 2，表示會員已經被禁止使用
//        if (existingMember.getMemStat() == 2) {
//            // 從 Redis 中刪除無權限會員的標記
//            redisTemplate.delete("noFun" + existingMember.getMemNo().toString());
//
//            // 在 ModelMap 中添加一條提示消息，告訴用戶他們的帳號已經沒有權限
//            modelMap.addAttribute("noFun", "此帳號已無權限，請洽詢相關的工作人員");
//
//            // 返回到登入頁面，並顯示無權限提示
//            return "frontend/member/loginMemberByAccount";
//        }
//
//        // 如果登錄失敗
//        if (loginData == null) {
//            // 增加該會員的登錄失敗次數
//            attemptCount++;
//
//            // 將更新的次數儲存到 Map 中
//            loginAttemptsMap.put(memAcc, attemptCount);
//
//            // 如果嘗試次數達到或超過五次，則停權該會員並返回提示訊息
//            if (attemptCount >= 5) {
//                modelMap.addAttribute("message", "此會員帳號已遭停權，請聯繫官方客服尋求幫助");
//                // 使用 banMemberByMail 方法停權會員
//                banMemberByMemAcc(memAcc, session, modelMap);
//                // 返回到登入頁面，並顯示無權限提示
//                return "frontend/member/loginMemberByAccount";
//            }
//
//            // 提示錯誤訊息
//            modelMap.addAttribute("message", "帳號或密碼輸入錯誤，請重新輸入!");
//            if (5 - attemptCount <= 3) {
//                modelMap.addAttribute("message", "您剩餘" + (5 - attemptCount) + "次嘗試次數");
//            }
//
//            return "frontend/member/loginMemberByAccount";
//        }
//
//        // 如果登錄成功，清除該會員登錄失敗的次數
//        loginAttemptsMap.remove(memAcc);
//
//        // FIXME 修改
//        // 獲取會員的通知
//        Notice notice = noticeService.getOneByMember(loginData);
//
//        // 從會話中移除 "URI" 屬性，以避免重複重定向
//        session.removeAttribute("URI");
//
//        // 登入成功後，將會員信息和通知信息儲存到會話中
//        // `loginsuccess` 屬性存儲當前登入的會員信息 (`loginData`)
//        // `notice` 屬性存儲與該會員相關的通知信息 (`notice`)
//        session.setAttribute("loginsuccess", loginData);
//        session.setAttribute("notice", notice);
//
//        // 重定向到原始請求的 URI
//        try {
//            response.sendRedirect(projectUri + uri);
//            return null;
//        } catch (IOException e) {
//            // 處理重定向的 IOException
//            e.printStackTrace();
//        }
//
//        return "frontend/member/loginMember";
//    }

    //    /**
//     * 處理會員登入請求 (帳號或信箱和密碼)。
//     * 此方法處理 HTTP POST 請求到 '/frontend/member/loginPage' 和
//     * '/frontend/member/loginPageByMemAccount' URL 路徑，
//     * 接收會員的帳號或信箱和密碼，並嘗試進行登入操作。
//     * 如果登入成功，將會員的訊息儲存到會話中，並重定向到原始請求的 URI。
//     * 如果會員狀態是被禁止使用（狀態為 2），則刪除該會員的無權限標記，並顯示相關訊息。
//     * 如果登入失敗，增加會員的登入嘗試次數。
//     * 如果會員的登入嘗試次數達到或超過五次，將停權該會員並返回相關提示消息。
//     * 如果剩餘嘗試次數少於等於三次，顯示剩餘的嘗試次數。
//     * 根據請求的不同路徑（'/frontend/member/loginPage' 或 '/frontend/member/loginPageByMemAccount'），
//     * 對應返回不同的登入頁面名稱。
//     *
//     * @param identifier 會員的帳號或信箱。
//     * @param memPwd 會員的密碼。
//     * @param modelMap 包含模型屬性的 'ModelMap'。
//     * @param session 用於存儲和訪問會員訊息的會話對象。
//     * @param response 用於處理重定向的回應對象。
//     * @param request 用於確定請求路徑的請求對象。
//     * @return 登入成功時，返回 null 並重定向到原始請求的 URI；
//     *         登入失敗時，返回對應的登入頁面名稱並顯示錯誤消息。
//     */
//    @PostMapping({"/loginPage", "/loginPageByMemAccount"})
//    public String loginPage(@ModelAttribute("identifier") String identifier,
//                            @ModelAttribute("password") String memPwd,
//                            ModelMap modelMap,
//                            HttpSession session,
//                            HttpServletResponse response,
//                            HttpServletRequest request) {
//
//        // 獲取重定向的 URI 或設置默認值
//        String uri = session.getAttribute("URI") == null ? "/" : session.getAttribute("URI").toString();
//        String projectUri = session.getServletContext().getContextPath();
//
//        // 檢查帳號或信箱和密碼是否為空
//        if (identifier.isEmpty() || memPwd.isEmpty()) {
//            if (identifier.isEmpty()) {
//                if (request.getRequestURI().endsWith("/loginPage")) {
//                    modelMap.addAttribute("message", "信箱不能空白，請重新輸入!");
//                } else {
//                    modelMap.addAttribute("message", "帳號不能空白，請重新輸入!");
//                }
//            } else {
//                modelMap.addAttribute("message", "密碼不能空白，請重新輸入!");
//            }
//            return request.getRequestURI().endsWith("/loginPage")
//                    ? "frontend/member/loginMember"
//                    : "frontend/member/loginMemberByAccount";
//        }
//
//        // 從會話中獲取儲存的登錄失敗次數 Map 物件
//        Map<String, Integer> loginAttemptsMap = (Map<String, Integer>) session.getAttribute("loginAttemptsMap");
//        if (loginAttemptsMap == null) {
//            // 如果 Map 尚未初始化，創建一個新的 Map
//            loginAttemptsMap = new HashMap<>();
//            session.setAttribute("loginAttemptsMap", loginAttemptsMap);
//        }
//
//        // 根據請求路徑確定使用帳號還是信箱登入
//        Member loginData;
//        Member existingMember;
//        String memberKey;
//
//        if (request.getRequestURI().endsWith("/loginPage")) {
//            // 使用信箱進行登入
//            loginData = memberService.login(identifier, memPwd);
//            existingMember = memberService.findByMail(identifier);
//        } else {
//            // 使用帳號進行登入
//            loginData = memberService.loginByMemAcc(identifier, memPwd);
//            existingMember = memberService.findByMemAcc(identifier);
//        }
//
//        // 檢查會員是否存在於資料庫中
//        if (existingMember == null) {
//            // 根據請求路徑返回不同的錯誤消息
//            if (request.getRequestURI().endsWith("/loginPage")) {
//                // 信箱登入頁面
//                modelMap.addAttribute("message", "沒有該會員信箱，請確認您的信箱。");
//            } else {
//                // 帳號登入頁面
//                modelMap.addAttribute("message", "沒有該會員帳號，請確認您的帳號。");
//            }
//            return request.getRequestURI().endsWith("/loginPage")
//                    ? "frontend/member/loginMember"
//                    : "frontend/member/loginMemberByAccount";
//        }
//
//        // 使用會員ID作為 `loginAttemptsMap` 的鍵，以跟蹤同一會員的登入失敗次數
//        memberKey = existingMember.getMemNo().toString();
//        Integer attemptCount = loginAttemptsMap.getOrDefault(memberKey, 0);
//
//        // 如果會員狀態是 2，表示會員已經被禁止使用
//        if (existingMember.getMemStat() == 2) {
//            // 刪除無權限會員的標記
//            redisTemplate.delete("noFun" + existingMember.getMemNo().toString());
//            modelMap.addAttribute("noFun", "此會員已無權限，請洽詢相關的工作人員");
//            return request.getRequestURI().endsWith("/loginPage")
//                    ? "frontend/member/loginMember"
//                    : "frontend/member/loginMemberByAccount";
//        }
//
//        // 如果登錄失敗
//        if (loginData == null) {
//            // 增加該會員的登錄失敗次數
//            attemptCount++;
//            loginAttemptsMap.put(memberKey, attemptCount);
//
//            // 如果嘗試次數達到或超過五次，則停權該會員並返回提示訊息
//            if (attemptCount >= 5) {
//                modelMap.addAttribute("message", "此會員已遭停權，請聯繫官方客服尋求幫助");
//                // 停權會員
//                if (request.getRequestURI().endsWith("/loginPage")) {
//                    banMemberByMail(identifier, session, modelMap);
//                } else {
//                    banMemberByMemAcc(identifier, session, modelMap);
//                }
//                return request.getRequestURI().endsWith("/loginPage")
//                        ? "frontend/member/loginMember"
//                        : "frontend/member/loginMemberByAccount";
//            }
//
//
//            // 細分登入失敗的原因
//            if (existingMember != null) {
//                // 檢查密碼是否正確
//                if (!existingMember.getMemPwd().equals(memPwd)) {
//                    // 密碼錯誤
//                    modelMap.addAttribute("message", "密碼輸入錯誤，請重新輸入!");
//                } else {
//                    // 因為會員存在但密碼正確，所以可能是其他原因造成的錯誤
//                    modelMap.addAttribute("message", "未知的登入錯誤，請稍後再試。");
//                }
//            } else {
//                // 會員不存在，所以可能是帳號或信箱錯誤
//                if (request.getRequestURI().endsWith("/loginPage")) {
//                    modelMap.addAttribute("message", "信箱錯誤，請重新輸入!");
//                } else {
//                    modelMap.addAttribute("message", "帳號錯誤，請重新輸入!");
//                }
//            }
//
//            if (5 - attemptCount <= 3) {
//                modelMap.addAttribute("message", "您剩餘 " + (5 - attemptCount) + " 次嘗試次數");
//            }
//
//            return request.getRequestURI().endsWith("/loginPage")
//                    ? "frontend/member/loginMember"
//                    : "frontend/member/loginMemberByAccount";
//        }
//
//        // 登錄成功，清除該會員登錄失敗的次數
//        loginAttemptsMap.remove(memberKey);
//
//        // FIXME 修改
//        // 獲取會員的通知
//        Notice notice = noticeService.getOneByMember(loginData);
//
//        // 從會話中移除 "URI" 屬性，以避免重複重定向
//        session.removeAttribute("URI");
//
//        // 登入成功後，將會員訊息和通知訊息儲存到會話中
//        session.setAttribute("loginsuccess", loginData);
//        session.setAttribute("notice", notice);
//
//        // 重定向到原始請求的 URI
//        try {
//            response.sendRedirect(projectUri + uri);
//            return null;
//        } catch (IOException e) {
//            // 處理重定向的 IOException
//            e.printStackTrace();
//        }
//
//        return request.getRequestURI().endsWith("/loginPage")
//                ? "frontend/member/loginMember"
//                : "frontend/member/loginMemberByAccount";
//    }
/**
//      * 前往單個會員資料的頁面。
//      * 此方法處理 HTTP GET 請求到 '/frontend/member/memberData' URL 路徑，
//      * 從會話中獲取當前已登入的會員資料並將其添加到 'ModelMap' 中。
//      *
//      * @param modelMap 包含模型屬性的 'ModelMap'。
//      * @param session session HTTP 會話物件，用來儲存和訪問當前已經登入的會員。
//      * @return 要呈現的視圖名稱 "oneMember.html"。
//      */
//    @GetMapping("/memberData")
//    public String memberData(ModelMap modelMap, HttpSession session) {
//
//        // 從 HTTP 會話中獲取當前已登入的會員資料
//        Member myData = (Member) loginStateMember;
//        // Member myData = (Member) session.getAttribute("loginsuccess");
//
////        // 如果會員未登錄，重定向到登錄頁面
////        if (myData == null) {
////            return "redirect:/frontend/member/loginMember";
////        }
//
//        // 將會員資料添加到模型中
//        modelMap.addAttribute("myData", myData);
//        // 以下魔改
////        Base64.Encoder encoder = Base64.getEncoder();
////        String chaPic = encoder.encodeToString(myData.getMemPic());
////        modelMap.addAttribute("chaPic", chaPic);
//
//        // 返回要呈現的視圖名稱
//        return "frontend/member/oneMember";
//    }
    //    /**
//     * 控制器方法，用於處理彈出式登錄。
//     *
//     * @param identifier         使用者輸入的識別符（電子郵件或帳戶）。
//     * @param memPwd             使用者輸入的密碼。
//     * @param autoLoginMember    標誌，指示是否要求自動登錄。
//     * @param modelMap           ModelMap 物件，用於為視圖添加屬性。
//     * @param session            HttpSession 物件，用於管理會話數據。
//     * @param response           HttpServletResponse 物件，用於發送響應。
//     * @param request            HttpServletRequest 物件，用於處理請求。
//     * @return                   視圖名稱或如果重定向則為 null。
//     */
//    @PostMapping("/loginPageByPopup")
//    public String loginPageByPopup(@ModelAttribute("identifier") String identifier,
//                                   @ModelAttribute("password") String memPwd,
//                                   @RequestParam(value = "autoLoginMember", defaultValue = "0") Byte autoLoginMember,
//                                   ModelMap modelMap,
//                                   HttpSession session,
//                                   HttpServletResponse response,
//                                   HttpServletRequest request) {
//
//        // 獲取重定向的 URI 或設置默認值
//        String uri = session.getAttribute("URI") == null ? "/" : session.getAttribute("URI").toString();
//        String projectUri = session.getServletContext().getContextPath();
//
//        // 檢查帳號或信箱和密碼是否為空
//        if (identifier.isEmpty() || memPwd.isEmpty()) {
//            if (identifier.isEmpty()) {
//                modelMap.addAttribute("message", "帳號或信箱不能空白，請重新輸入!");
//            } else {
//                modelMap.addAttribute("message", "密碼不能空白，請重新輸入!");
//            }
//            return "frontend/component/loginpopup";
//        }
//
//        // 從會話中獲取儲存的登錄失敗次數 Map 物件
//        Map<String, Integer> loginAttemptsMap = (Map<String, Integer>) session.getAttribute("loginAttemptsMap");
//        if (loginAttemptsMap == null) {
//            // 如果 Map 尚未初始化，創建一個新的 Map
//            loginAttemptsMap = new HashMap<>();
//            session.setAttribute("loginAttemptsMap", loginAttemptsMap);
//        }
//
//        // 根據請求路徑確定使用帳號還是信箱登入
//        // 宣告會員跟會員編號，於後續參數驗證後賦值
//        Member loginData = null;
//        Integer memNo = null;
//        Member existingMember = null;
//        String memberKey = null;
//
//        if (validateEmail(identifier)) {
//            // 使用信箱進行登入
//            loginData = memberService.login(identifier, memPwd);
//            existingMember = memberService.findByMail(identifier);
//        } else if (validateAccout(identifier)){
//            // 使用帳號進行登入
//            loginData = memberService.loginByMemAcc(identifier, memPwd);
//            existingMember = memberService.findByMemAcc(identifier);
//        }
//
//        // 檢查會員是否存在於資料庫中
//        if (existingMember == null) {
//            // 根據不同的正則表達式返回不同的錯誤消息
//            if (validateEmail(identifier)) {
//                // 信箱登入
//                modelMap.addAttribute("message", "沒有該會員信箱，請確認您的信箱。");
//            } else if (validateAccout(identifier)){
//                // 帳號登入
//                modelMap.addAttribute("message", "沒有該會員帳號，請確認您的帳號。");
//            }
//            return "frontend/component/loginpopup";
//        }
//
//        // 使用會員ID作為 `loginAttemptsMap` 的鍵，以跟蹤同一會員的登入失敗次數
//        memberKey = existingMember.getMemNo().toString();
//        Integer attemptCount = loginAttemptsMap.getOrDefault(memberKey, 0);
//
//        // 如果會員狀態是 2，表示會員已經被禁止使用
//        if (existingMember.getMemStat() == 2) {
//            // 刪除無權限會員的標記
//            redisTemplate.delete("noFun" + existingMember.getMemNo().toString());
//            modelMap.addAttribute("noFun", "此會員已無權限，請洽詢相關的工作人員");
//            return "frontend/component/loginpopup";
//        }
//
//        // 如果登錄失敗
//        if (loginData == null) {
//            // 增加該會員的登錄失敗次數
//            attemptCount++;
//            loginAttemptsMap.put(memberKey, attemptCount);
//
//            // 如果嘗試次數達到或超過五次，則停權該會員並返回提示訊息
//            if (attemptCount >= 5) {
//                modelMap.addAttribute("message", "此會員已遭停權，請聯繫官方客服尋求幫助");
//                // 停權會員
//                if (validateEmail(identifier)) {
//                    banMemberByMail(identifier, session, modelMap);
//                } else if (validateAccout(identifier)){
//                    banMemberByMemAcc(identifier, session, modelMap);
//                }
//                return "frontend/component/loginpopup";
//            }
//
//            // 細分登入失敗的原因
//            if (existingMember != null) {
//                // 檢查密碼是否正確
//                if (!existingMember.getMemPwd().equals(memPwd)) {
//                    // 密碼錯誤
//                    modelMap.addAttribute("message", "密碼輸入錯誤，請重新輸入!");
//                } else {
//                    // 因為會員存在但密碼正確，所以可能是其他原因造成的錯誤
//                    modelMap.addAttribute("message", "未知的登入錯誤，請稍後再試。");
//                }
//            } else {
//                // 會員不存在，所以可能是帳號或信箱錯誤
//                if (validateEmail(identifier)) {
//                    modelMap.addAttribute("message", "信箱錯誤，請重新輸入!");
//                } else if (validateAccout(identifier)){
//                    modelMap.addAttribute("message", "帳號錯誤，請重新輸入!");
//                }
//            }
//
//            if (5 - attemptCount <= 3) {
//                modelMap.addAttribute("message", "您剩餘 " + (5 - attemptCount) + " 次嘗試次數");
//            }
//
//            return "frontend/component/loginpopup";
//        }
//
//        // 登錄成功，清除該會員登錄失敗的次數
//        System.out.println("登入成功");
//        loginAttemptsMap.remove(memberKey);
//
//        // FIXME 修改
//        // 獲取會員的通知
//        Notice notice = noticeService.getOneByMember(loginData);
//
//        // 從會話中移除 "URI" 屬性，以避免重複重定向
//        session.removeAttribute("URI");
//
//        // 登入成功後，將會員訊息和通知訊息儲存到會話中
//        session.setAttribute("loginsuccess", loginData);
//        session.setAttribute("notice", notice);
//
//        System.out.println("autoLoginMember: " + autoLoginMember);
//
//        memNo = existingMember.getMemNo();
//        // 確認會員是否要自動登入
//        if (autoLoginMember != null && autoLoginMember == YES) {
//            // 如果 autoLoginMember 不為空且值為 1，表示用戶要自動登入
//            // 添加處理自動登入的邏輯，例如生成一個 token，存入 cookie 和 redis
//            // 這裡的範例代碼是將自動登入的信息存入 redis 和 cookie 中
//            // 生成名為 autoLoginMember 的 cookie，其值設置為一個亂數生成的字符串，分別存入給會員與 redis 資料庫，做身分核對
//            String random = generateRandomString(40);
//            Cookie cookie = new Cookie("autoLoginMember", random);
//
//            // 設置存活 7 天
//            cookie.setMaxAge(604800);
//
//            // 設置 cookie 的路徑為 / frontend，當訪問所有的前台網頁都可以獲取這個 cookie
//            cookie.setPath(request.getContextPath() + "/frontend");
//            response.addCookie(cookie);
//            memStrIntRedisTemplate.opsForValue().set(random, memNo);
//            System.out.println("cookie 存入");
//            System.out.println("自動登入信息已存入");
//        }
//
//        // 重定向到原始請求的 URI
//        try {
//            response.sendRedirect(projectUri + uri);
//            return null;
//        } catch (IOException e) {
//            // 處理重定向的 IOException
//            e.printStackTrace();
//        }
//
//        return "frontend/component/loginpopup";
//    }
    //    /**
//     * 用於處理彈出式登錄。
//     *
//     * @param identifier         使用者輸入的識別符（電子郵件或帳戶）。
//     * @param memPwd             使用者輸入的密碼。
//     * @param autoLoginMember    標誌，指示是否要求自動登錄。
//     * @param session            HttpSession 物件，用於管理會話數據。
//     * @param response           HttpServletResponse 物件，用於發送響應。
//     * @param request            HttpServletRequest 物件，用於處理請求。
//     * @return                   ResponseEntity 包含 JSON 格式的響應。
//     */
//    @PostMapping("/loginPageByPopup")
//    @ResponseBody
//    public ResponseEntity<?> loginPageByPopup(@RequestParam("identifier") String identifier,
//                                              @RequestParam("password") String memPwd,
//                                              @RequestParam(value = "autoLoginMember", defaultValue = "0") Byte autoLoginMember,
//                                              HttpSession session,
//                                              HttpServletResponse response,
//                                              HttpServletRequest request) {
//
//        // 獲取重定向的 URI 或設置默認值
//        String uri = session.getAttribute("URI") == null ? "/" : session.getAttribute("URI").toString();
//        String projectUri = session.getServletContext().getContextPath();
//
//        // 檢查帳號或信箱和密碼是否為空
//        if (identifier.isEmpty() || memPwd.isEmpty()) {
//            if (identifier.isEmpty()) {
//                return ResponseEntity.badRequest().body("帳號或信箱不能空白，請重新輸入!");
//            } else {
//                return ResponseEntity.badRequest().body("密碼不能空白，請重新輸入!");
//            }
//        }
//
//        // 從會話中獲取儲存的登錄失敗次數 Map 物件
//        Map<String, Integer> loginAttemptsMap = (Map<String, Integer>) session.getAttribute("loginAttemptsMap");
//        if (loginAttemptsMap == null) {
//            // 如果 Map 尚未初始化，創建一個新的 Map
//            loginAttemptsMap = new HashMap<>();
//            session.setAttribute("loginAttemptsMap", loginAttemptsMap);
//        }
//
//        // 根據請求路徑確定使用帳號還是信箱登入
//        // 宣告會員跟會員編號，於後續參數驗證後賦值
//        Member loginData = null;
//        Integer memNo = null;
//        Member existingMember = null;
//        String memberKey = null;
//
//        if (validateEmail(identifier)) {
//            // 使用信箱進行登入
//            loginData = memberService.login(identifier, memPwd);
//            existingMember = memberService.findByMail(identifier);
//        } else if (validateAccout(identifier)){
//            // 使用帳號進行登入
//            loginData = memberService.loginByMemAcc(identifier, memPwd);
//            existingMember = memberService.findByMemAcc(identifier);
//        }
//
//        // 檢查會員是否存在於資料庫中
//        if (existingMember == null) {
//            // 根據不同的正則表達式返回不同的錯誤消息
//            if (validateEmail(identifier)) {
//                // 信箱登入
//                return ResponseEntity.badRequest().body("沒有該會員信箱，請確認您的信箱。");
//            } else if (validateAccout(identifier)){
//                // 帳號登入
//                return ResponseEntity.badRequest().body("沒有該會員帳號，請確認您的帳號。");
//            }
//        }
//
//        // 使用會員ID作為 `loginAttemptsMap` 的鍵，以跟蹤同一會員的登入失敗次數
//        memberKey = existingMember.getMemNo().toString();
//        Integer attemptCount = loginAttemptsMap.getOrDefault(memberKey, 0);
//
//        // 如果會員狀態是 2，表示會員已經被禁止使用
//        if (existingMember.getMemStat() == 2) {
//            // 刪除無權限會員的標記
//            redisTemplate.delete("noFun" + existingMember.getMemNo().toString());
//            return ResponseEntity.badRequest().body("此會員已無權限，請洽詢相關的工作人員");
//        }
//
//        // 如果登錄失敗
//        if (loginData == null) {
//            // 增加該會員的登錄失敗次數
//            attemptCount++;
//            loginAttemptsMap.put(memberKey, attemptCount);
//
//            // 返回錯誤訊息
//            if (existingMember != null) {
//                if (!existingMember.getMemPwd().equals(memPwd)) {
//                    return ResponseEntity.badRequest().body("密碼輸入錯誤，請重新輸入!");
//                } else {
//                    return ResponseEntity.badRequest().body("未知的登入錯誤，請稍後再試。");
//                }
//            } else {
//                if (validateEmail(identifier)) {
//                    return ResponseEntity.badRequest().body("信箱錯誤，請重新輸入!");
//                } else if (validateAccout(identifier)){
//                    return ResponseEntity.badRequest().body("帳號錯誤，請重新輸入!");
//                }
//            }
//
//            if (5 - attemptCount <= 3) {
//                return ResponseEntity.badRequest().body("您剩餘 " + (5 - attemptCount) + " 次嘗試次數");
//            }
//        }
//
//        // 登錄成功，清除該會員登錄失敗的次數
//        System.out.println("登入成功");
//        loginAttemptsMap.remove(memberKey);
//
//        // FIXME 修改
//        // 獲取會員的通知
//        Notice notice = noticeService.getOneByMember(loginData);
//
//        // 從會話中移除 "URI" 屬性，以避免重複重定向
//        session.removeAttribute("URI");
//
//        // 登入成功後，將會員訊息和通知訊息儲存到會話中
//        session.setAttribute("loginsuccess", loginData);
//        session.setAttribute("notice", notice);
//
//        System.out.println("autoLoginMember: " + autoLoginMember);
//
//        memNo = existingMember.getMemNo();
//        // 確認會員是否要自動登入
//        if (autoLoginMember != null && autoLoginMember == YES) {
//            // 如果 autoLoginMember 不為空且值為 1，表示用戶要自動登入
//            // 添加處理自動登入的邏輯，例如生成一個 token，存入 cookie 和 redis
//            // 這裡的範例代碼是將自動登入的信息存入 redis 和 cookie 中
//            // 生成名為 autoLoginMember 的 cookie，其值設置為一個亂數生成的字符串，分別存入給會員與 redis 資料庫，做身分核對
//            String random = generateRandomString(40);
//            Cookie cookie = new Cookie("autoLoginMember", random);
//
//            // 設置存活 7 天
//            cookie.setMaxAge(604800);
//
//            // 設置 cookie 的路徑為 / frontend，當訪問所有的前台網頁都可以獲取這個 cookie
//            cookie.setPath(request.getContextPath() + "/frontend");
//            response.addCookie(cookie);
//            memStrIntRedisTemplate.opsForValue().set(random, memNo);
//            System.out.println("cookie 存入");
//            System.out.println("自動登入信息已存入");
//        }
//
//        System.out.println("登入成功");
//
//        // 登錄成功後，將用戶重定向到前台首頁
//        String redirectUrl = "index";
//        // 返回成功的 JSON 響應
//        return ResponseEntity.ok().header("Location", redirectUrl).build();
//    }

}
