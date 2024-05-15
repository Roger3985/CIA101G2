package com.roger.member.service;

import com.roger.member.dto.LoginStateMember;
import com.roger.member.entity.Member;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface MemberService {

    /**
     * 註冊新會員。
     *
     * @param member 新會員的資料，封裝在 MemberVO 物件中。
     * @return 註冊成功後的 MemberVO 物件，其中可能包含更新的會員資訊 (例如:自動生成的會員編號)
     */
    public Member register(Member member);

    /**
     * 登入會員並驗證其信箱和密碼。
     *
     * @param memMail 會員的註冊信箱。
     * @param memPwd 會員的註冊密碼。
     * @return 登入成功後的 Member 物件，其中包含該會員的資料。
     */
    public Member login(String memMail, String memPwd);

    /**
     * 登入會員並驗證其帳號和密碼。
     *
     * @param memAcc 會員的註冊帳號。
     * @param memPwd 會員的註冊密碼。
     * @return 登入成功後的 Member 物件，其中包含該會員的資料。
     */
    public Member loginByMemAcc(String memAcc, String memPwd);

    /**
     * 登入會員並驗證其電話和密碼。
     *
     * @param memMob 會員的註冊電話。
     * @param memPwd 會員的註冊密碼。
     * @return 登入成功後的 Member 物件，其中包含該會員的資料。
     */
    public Member loginByMemMob(String memMob, String memPwd);

    /**
     * 編輯會員資料
     * 此方法接收一個 'Member' 類型的物件，表示需要更新的會員資料。
     * 方法執行後，將會根據提供的會員資料更新相應的會員訊息。
     *
     * @param newData 要編輯的會員物件，包含了需要更新的會員訊息。
     * @return 更新後的會員物件。
     */
    public Member edit(Member newData);

    /**
     * 變更會員大頭貼。
     * 此方法接收一個 'Member' 物件和一個 'byte[]' 類型的陣列 'memPic'，用於更改會員的大頭貼圖片。
     *
     * @param member 需要更新大頭貼的 `Member` 物件。
     * @param memPic 新的會員大頭貼圖片，作為一個 `byte[]` 類型的數組。
     * @throws IllegalArgumentException 如果 `member` 或 `memPic` 為 `null`。
     */
    public void changePicture(Member member, byte[] memPic);

    /**
     * 根據會員編號查詢會員資料
     *
     * @param memNo 要查詢的會員編號
     * @return 找到相對應的會員，則返回 MemberVO 物件。
     */
    public Member findByNo(Integer memNo);

    /**
     * 根據電子郵件查找會員。
     *
     * @param memMail 要查找的會員的電子郵件。
     * @return 與該電子郵件相匹配的會員，如果找不到則返回 null。
     */
    public Member findByMail(String memMail);

    /**
     * 根據會員帳號查找會員。
     *
     * @param memAcc 要查找的會員的帳號。
     * @return 與該帳號相匹配的會員，如果找不到則返回 null。
     */
    public Member findByMemAcc(String memAcc);

    /**
     * 根據會員電話查找會員。
     *
     * @param memMob 要查找的會員的電話。
     * @return 與該電話相匹配的會員，如果找不到則返回 null。
     */
    public Member findByMemMob(String memMob);

    /**
     * 查找所有會員。
     *
     * @return 包含所有會員的 List<Member> 列表。
     */
    public List<Member> findAll();

    /**
     * 信箱驗證。
     * 此方法負責發送信箱驗證的郵件。
     * 方法接受驗證碼、主題、內容和驗證ID作為參數，並使用這些信息向用戶發送驗證郵件。
     *
     * @param mail 驗證碼，用於郵件中進行身份驗證。
     * @param subject 郵件主題，描述郵件中的內容。
     * @param text 郵件內容，包含驗證相關信息。
     * @param verifyID 驗證ID，用於識別驗證操作。
     * @return 郵件發送結果，通常為狀態描述或識別驗證狀態的字符串。
     */
    public boolean verifyMail(String mail, String subject, String text, String verifyID);

    /**
     * 使用哈希演算法對密碼進行加密。
     * 該方法接收一個明文密碼，並使用安全的哈希算法（例如:MD5）對其進行哈希加密。
     * 哈希後的密碼可以安全地存儲在資料庫中。
     *
     * @param memPwd 被哈希加密後的密碼。
     * @return 哈希加密後的密碼，這個密碼是安全的，適合在資料庫中存儲。
     */
    public String hashPassword(String memPwd);

    /**
     * 處理忘記密碼的功能。
     * 此方法接受會員的註冊信箱，並對會員的密碼進行重置或發送重置密碼的相關訊息。
     *
     * @param memMail 會員的註冊信箱。
     * @return 如果重置密碼的請求成功，則返回 true;否則返回 false
     */
    public boolean forgetPassword(String memMail);

    /**
     * 處理第三方登入賦予隨機密碼的功能。
     * 此方法接受會員的第三方信箱，並對會員的密碼賦予隨機密碼且發送賦予密碼的相關訊息。
     *
     * @param memMail 會員的第三方註冊信箱。
     * @return 如果賦予密碼的請求成功，則返回 true;否則返回 false
     */
    public String setThreeLoginPassword(String memMail);

    /**
     * 停權會員。
     * 此方法根據會員編號停權會員的帳號。
     *
     * @param memNo 會員編號。
     */
    public void banMem(Integer memNo);

    /**
     * 禁用指定會員並更新其狀態。
     * @param member 要禁用的會員。
     */
    public void banMember(Member member);

    /**
     * 檢查會員帳號是否在系統中存在。
     * 此方法接受會員帳號的標識，並返回該會員帳號是否存在。
     *
     * @param memAcc 會員帳號的標識。
     * @return 如果會員帳號存在，返回 `true`；否則返回 `false`。
     */
    public boolean existMemAccount(String memAcc);

    /**
     * 檢查會員手機號碼是否在系統中存在。
     * 此方法接受會員手機號碼的標識，並返回該手機號碼是否存在於系統中。
     *
     * @param memMob 會員手機號碼的標識。
     * @return 如果會員手機號碼存在，返回 `true`；否則返回 `false`。
     */
    public boolean existMemMobile(String memMob);

    /**
     * 檢查會員信箱是否在系統中存在。
     * 此方法接受會員信箱的標識，並返回該信箱是否存在於系統中。
     *
     * @param memMail 會員信箱的標識。
     * @return 如果會員信箱存在，返回 `true`；否則返回 `false`。
     */
    public boolean existMemMail(String memMail);


    /**
     * 根據會員編號（memNo）查找會員。
     * 此方法接受會員編號作為參數，並返回與該編號匹配的會員對象。
     * 如果找不到匹配的會員，則返回 null。
     *
     * @param memNo 會員編號。
     * @return 與給定會員編號匹配的會員對象；如果找不到匹配的會員，則返回 null。
     */
    public Member getMemberByMemNo(Integer memNo);


    /**
     * U:
     * 登入成功，修改會員登入狀態
     *
     * @param member 使用查詢方法取得 Entity，身分核對後將 Entity 傳入方法內修改登入狀態
     * @return 返回帳號登入狀態 DTO
     */
    public LoginStateMember loginState(Member member, HttpSession session);

}
