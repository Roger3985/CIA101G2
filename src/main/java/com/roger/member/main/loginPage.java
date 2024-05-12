package com.roger.member.main;

public class loginPage {

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

}
