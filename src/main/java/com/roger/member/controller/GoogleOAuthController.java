package com.roger.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roger.columnarticle.entity.ColumnArticle;
import com.roger.columnarticle.service.ColumnArticleService;
import com.roger.member.dto.AccessToken;
import com.roger.member.dto.ExchangeTokenRequest;
import com.roger.member.dto.RefreshTokenRequest;
import com.roger.member.entity.Member;
import com.roger.member.main.PhoneNumberGenerator;
import com.roger.member.service.MemberService;
import com.roger.notice.entity.Notice;
import com.roger.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;

//@RestController
@Controller
@ResponseBody
public class GoogleOAuthController {

    /**
     * 生成以 "09" 開頭的隨機電話號碼
     * @return 隨機生成的以 09 開頭的電話號碼
     */
    public static String generatePhoneNumber() {

        Random random = new Random();
        StringBuilder phoneNumber = new StringBuilder("09");

        // 生成後面的的 8 位數字
        for (int i = 0; i < 8; i++) {
            phoneNumber.append(random.nextInt(10));
        }

        return phoneNumber.toString();
    }

    @Autowired
    private MemberService memberService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private ColumnArticleService columnArticleService;

    private String GOOGLE_CLIENT_ID = "XXX";

    private String GOOGLE_CLIENT_SECRET = "YYY";

    private String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";

    private String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";

    // 步驟 2. 拼接跳轉的 url，將使用者跳轉到 Google 認證中心頁面
    @GetMapping("/google/buildAuthUrl")
    public String buildAuthUrl() {
        // 使用 Google 所提供的 auth url，使用者點擊此 url 之後，可以開始 OAuth 2.0 的授權流程
        String authUrl = GOOGLE_AUTH_URL;

        // 拼接 auth url 的請求參數
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(authUrl)
                .queryParam("response_type", "code")
                .queryParam("client_id", GOOGLE_CLIENT_ID)
                .queryParam("scope", "profile+email+openid")
                .queryParam("redirect_uri", "http://localhost:8080/frontend/member/loginMember")
                .queryParam("state", generateRandomState())
                .queryParam("access_type", "offline");

        return uriBuilder.toUriString();
    }

    // 步驟 5. 傳遞 code、client_id、client_secret 的值給 Google 認證中心，換取 access_token 的值
    @PostMapping(value = "/google/exchangeToken")
    public String exchangeToken(@RequestBody ExchangeTokenRequest exchangeTokenRequest,
                                Member member,
                                HttpSession session) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // 使用 Google 所提供的 token url，傳遞 code 的值過去，即可取得使用者的 access_token
        String tokenUrl = GOOGLE_TOKEN_URL;

        // 填寫 request body 中的請求參數
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", GOOGLE_CLIENT_ID);
        body.add("client_secret", GOOGLE_CLIENT_SECRET);
        body.add("code", exchangeTokenRequest.getCode());
        body.add("redirect_uri", "http://localhost:8080/frontend/member/loginMember");

        // 發送請求
        String result;
        try {
            result = restTemplate.postForObject(
                    tokenUrl,
                    new HttpEntity<>(body, headers),
                    String.class
            );
        } catch (Exception e) {
            result = e.toString();
        }

        session.setAttribute("loginsuccess", member);
        System.out.println("第三方登入，成功放入session");

        return result;
    }

    // 步驟 7. 使用 access_token，取得使用者在 Google 中的數據
    @PostMapping("/google/getGoogleUser")
    public String getGoogleUser(@RequestBody AccessToken accessToken,
                                @RequestParam(value = "memNo", required = false) String memNo,
                                ModelMap modelMap,
                                Member member,
                                HttpSession session,
                                HttpServletRequest request,
                                HttpServletResponse response) throws IOException {

        String token = accessToken.getAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";

        String result;
        try {
            result = restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<>(headers),
                            String.class
                    )
                    .getBody();
        } catch (Exception e) {
            result = e.toString();
        }

        System.out.println("獲取到token");
        System.out.println(result);

        // 在方法中的適當位置進行 JSON 解析
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(result);

        // 提取會員電子郵件
        String email = node.get("email").asText();
        System.out.println(email);
        // 提取第三方平台會員 ID
        String userId = node.get("id").asText();

        // 提取第三方平台會員的名子
        String name = node.get("name").asText();

        // 取得當前要設置的會員
        member = memberService.findByMail(email);

        if (member == null) {
            System.out.println("我有進來");
            Random random = new Random();
            int randomNumber = random.nextInt(10);
            Member newMember = new Member();

            newMember.setMemName(name);

            String newAcc;
            do {
                newAcc = (String) name + randomNumber;
            } while (memberService.existMemAccount(newAcc));
            newMember.setMemAcc(newAcc);

            newMember.setMemPwd(memberService.setThreeLoginPassword(email));

            String newPhoneNumber;
            do {
                newPhoneNumber = generatePhoneNumber();
            } while (memberService.existMemMobile(newPhoneNumber));
            newMember.setMemMob(newPhoneNumber);

            newMember.setMemGender((byte) 1);
            newMember.setMemMail(email);
            newMember.setMemAdd("台北市大安區忠孝東路四段100號");
            newMember.setMemBd(new Date(System.currentTimeMillis()));
            newMember.setProvider((byte) 1);
            newMember.setClientID(userId);
            newMember.setDisplayName(name);
            newMember.setAccessToken(token);
            newMember.setCreationTime(new Timestamp(System.currentTimeMillis()));
            newMember.setMemberJoinTime(new Timestamp(System.currentTimeMillis()));
            newMember.setMemStat((byte) 0);

            memberService.register(newMember);
            session.setAttribute("loginsuccess", newMember);

            // 設置加入成功的消息
            Notice newNotice = new Notice();
            newNotice.setMember(newMember);
            newNotice.setNotContent("您的會員註冊已成功，歡迎加入我們Fall衣Love的大家庭");
            newNotice.setNotTime(new Timestamp(System.currentTimeMillis()));
            newNotice.setNotStat((byte) 0);

            noticeService.addNotice(newNotice);

            // 獲取未讀取通知的數量
            int unreadNoticeCount = noticeService.getUnreadNoticeCount(newMember);
            // 獲取會員的通知
            List<Notice> noticeList = noticeService.findNoticesByMemberMemNo(newMember.getMemNo());

            session.setAttribute("noticeList", noticeList);
            session.setAttribute("unreadNoticeCount", unreadNoticeCount);

            // 獲取上架中的文章列表
            List<ColumnArticle> publishedArticles = columnArticleService.getPublishedArticles();

            if (!publishedArticles.isEmpty()) {
                ColumnArticle firstArticle = publishedArticles.get(0);
                // 現在您可以使用 firstArticle 來訪問第一個文章的屬性和方法
                // 例如：firstArticle.getTitle()，firstArticle.getContent()，等等
                session.setAttribute("onePublishedArticles", firstArticle.getArtNo());
            }

            List<ColumnArticle> columnArticles = columnArticleService.findAll();
            modelMap.addAttribute("columnArticles", columnArticles);

            // 設置重定向目標為 `/frontend/cart/addcartsuccess`
//            session.setAttribute("location", "/frontend/cart/addcartsuccess");

        } else {
            // 將提供的第三方: 1(Google) 設置進去資料庫
            member.setProvider((byte) 1);

            member.setClientID(userId);

            // 將會員名子設置給 member 的 displayName
            member.setDisplayName(name);

            // 儲存 token，將 token 放到資料庫
            member.setAccessToken(token);

            // 設置創建時間為當前時間
            member.setCreationTime(new Timestamp(System.currentTimeMillis()));

            // 更新會員
            memberService.edit(member);

            // 設置 session
            session.setAttribute("loginsuccess", member);

            // 獲取會員的通知
            List<Notice> noticeList = noticeService.findNoticesByMemberMemNo(member.getMemNo());

            // 獲取上架中的文章列表
            List<ColumnArticle> publishedArticles = columnArticleService.getPublishedArticles();

            if (!publishedArticles.isEmpty()) {
                ColumnArticle firstArticle = publishedArticles.get(0);
                // 現在您可以使用 firstArticle 來訪問第一個文章的屬性和方法
                // 例如：firstArticle.getTitle()，firstArticle.getContent()，等等
                session.setAttribute("onePublishedArticles", firstArticle.getArtNo());
            }

            List<ColumnArticle> columnArticles = columnArticleService.findAll();
            modelMap.addAttribute("columnArticles", columnArticles);

            // 獲取未讀取通知的數量
            int unreadNoticeCount = noticeService.getUnreadNoticeCount(member);

            // 登入成功後，將通知訊息儲存到會話中
            session.setAttribute("noticeList", noticeList);
            session.setAttribute("unreadNoticeCount", unreadNoticeCount);

            // 設置重定向目標為 `/frontend/cart/addcartsuccess`
//            session.setAttribute("location", "/frontend/cart/addcartsuccess");
        }
        return result;
    }




    // 步驟 7. 使用 access_token，取得使用者在 Google 中的數據
//    @PostMapping("/google/getGoogleUser")
//    public String getGoogleUser(@RequestParam String accessToken) {
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + accessToken);
////        headers.setBearerAuth(accessToken);
//
//        // call Google 的 api，取得使用者在 Google 中的基本資料
//        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
//
//        // 發送請求
//        String result;
//        try {
//            result = restTemplate.exchange(
//                        url,
//                        HttpMethod.GET,
//                        new HttpEntity<>(headers),
//                        String.class
//                    )
//                    .getBody();
//
//        } catch (Exception e) {
//            result = e.toString();
//        }
//
//        System.out.println("獲取到token");
//        return result;
//    }

    // 使用 refresh_token，去和 Google 換發一個新的 access_token
    @PostMapping("/google/refreshToken")
    public String refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // 使用 Google 所提供的 token url，傳遞 refresh_token 的值過去，即可取得到新的 access token
        String tokenUrl = GOOGLE_TOKEN_URL;

        // 填寫 request body 中的請求參數
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", GOOGLE_CLIENT_ID);
        body.add("client_secret", GOOGLE_CLIENT_SECRET);
        body.add("refresh_token", refreshTokenRequest.getRefreshToken());

        // 發送請求
        String result;
        try {
            result = restTemplate.postForObject(
                    tokenUrl,
                    new HttpEntity<>(body, headers),
                    String.class
            );
        } catch (Exception e) {
            result = e.toString();
        }

        return result;
    }

    private String generateRandomState() {
        SecureRandom sr = new SecureRandom();
        byte[] data = new byte[6];
        sr.nextBytes(data);
        return Base64.getUrlEncoder().encodeToString(data);
    }
}
