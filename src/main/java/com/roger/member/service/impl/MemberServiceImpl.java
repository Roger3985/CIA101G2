package com.roger.member.service.impl;

import com.roger.member.dto.LoginStateMember;
import com.roger.member.repository.MemberRepository;
import com.roger.member.entity.Member;
import com.roger.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

@Service
public class MemberServiceImpl implements MemberService {

    /**
     * 自動裝配的 MemberRepository，用於執行會員相關的資料庫操作。
     */
    @Autowired
    private MemberRepository memberRepository;

    /**
     * 自動裝配的 StringRedisTemplate，用於執行與 Redis 資料庫操作，特別是針對字串類型的資料。
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 註冊新會員
     */
    @Override
    public Member register(Member member) {
        // 將會員的密碼進行加密
        String encryptedPassword = hashPassword(member.getMemPwd());
        member.setMemPwd(encryptedPassword);

        member.setMemBd(member.getMemBd());

        // 設定會員加入時間
        if (member.getMemberJoinTime() == null) {
            member.setMemberJoinTime(new Timestamp(System.currentTimeMillis()));
        }

        // 0為信箱未驗證
        member.setMemStat((byte) 0);

        // 保存新會員資料到資料庫中
        Member newData = memberRepository.save(member);

        // 返回新的會員資料
        return newData;
    }

    /**
     * 登入會員並驗證其信箱和密碼。
     */
    @Override
    public Member login(String memMail, String memPwd) {
        // 根據會員的電子郵件尋找會員資料
        Member getMemMail = memberRepository.findByMemMail(memMail);

        // 如果找不到會員資料，則返回該會員資料，即 null
        if (getMemMail == null) {
            return getMemMail;
        }

        // 核對密碼(比較輸入的密碼經哈希後的結果與會員資料中的密碼是否匹配)
        getMemMail = (hashPassword(memPwd).equals(getMemMail.getMemPwd())) ? getMemMail : null;

        // 返回會員資料(如果密碼匹配則返回會員資料；否則返回null)
        return getMemMail;
    }

    /**
     * 登入會員並驗證其帳號和密碼。
     */
    @Override
    public Member loginByMemAcc(String memAcc, String memPwd) {
        // 根據會員的帳號尋找會員資料
        Member getMemAcc = memberRepository.findByMemAcc(memAcc);

        // 如果找不到會員資料，則返回該會員資料，即 null
        if (getMemAcc == null) {
            return getMemAcc;
        }

        // 核對密碼(比較輸入的密碼經哈希後的結果與會員資料中的密碼是否匹配)
        getMemAcc = (hashPassword(memPwd).equals(getMemAcc.getMemPwd())) ? getMemAcc : null;

        // 返回會員資料(如果密碼匹配則返回會員資料；否則返回null)
        return getMemAcc;
    }

    /**
     * 編輯會員資料
     */
    @Override
    public Member edit(Member newData) {
        return memberRepository.save(newData);
    }

    /**
     * 變更會員大頭貼
     */
    @Override
    public void changePicture(Member member, byte[] memPic) {
        memberRepository.updateMemPicById(member.getMemNo(), memPic);
    }

    /**
     * 會員編號查詢
     */
    @Override
    public Member findByNo(Integer memNo) {
        return memberRepository.findById(memNo).orElse(null);
    }

    /**
     * 根據電子郵件查找會員。
     */
    @Override
    public Member findByMail(String memMail) {
        return memberRepository.findByMemMail(memMail);
    }

    /**
     * 根據會員帳號查找會員。
     */
    @Override
    public Member findByMemAcc(String memAcc) {
        return memberRepository.findByMemAcc(memAcc);
    }

    /**
     * 查找所有會員
     */
    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    /**
     * 信箱驗證
     */
    @Override
    public boolean verifyMail(String mail, String subject, String text, String verifyID) {
        verifyID = (verifyID == null) ? getAuthCode() : verifyID;

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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));

            // 設定郵件的主旨
            message.setSubject(subject);

            // 設定郵件的內容，包括驗證ID
            message.setText(text + verifyID);

            // 發送郵件
            Transport.send(message);
            System.out.println("傳送成功");

            // 將驗證 ID 設置到 Redis 中，並設置過期時間為20秒
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set("templateID" + mail, verifyID);
            redisTemplate.expire("templateID" + mail, 20, TimeUnit.SECONDS);
            System.out.println("yes");

            // 郵件發送成功，返回true
            return true;
        } catch (MessagingException e) {
            System.out.println("傳送失敗!");
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 哈希(MD5)密碼加密
     * 參考網站https://jax-work-archive.blogspot.com/2015/02/java.html
     */
    @Override
    public String hashPassword(String memPwd) {

        // 將 memPwd 加上前綴 "Fall" 和後綴 "Love" 進行簡易密碼加密
        memPwd = "Fall" + memPwd + "Love";

        // 根據 MD5 演算法生成 MessageDigest 物件
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            // 將字符串 memPwd 轉換為字節陣列
            byte[] srcBytes = memPwd.getBytes();
            // 使用 srcBytes 更新摘要
            md5.update(srcBytes);
            // 完成哈希演算法計算，得到 result
            byte[] resultBytes = md5.digest();

            // 將字節陣列轉換為十六進制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : resultBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // 返回十六進制字符串
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 處理忘記密碼的功能
     */
    @Override
    public boolean forgetPassword(String memMail) {

        if (memberRepository.existsByMemMail(memMail)) {
            String authCode = getAuthCode();
            Member member = memberRepository.findByMemMail(memMail);
            member.setMemPwd(hashPassword(authCode));
            edit(member);
            return verifyMail(memMail, "Fall衣Love發送新密碼", "你的新密碼為:", authCode);
        }
        // 發送隨機密碼簡訊
        return false;
    }

    /**
     * 停權會員。
     */
    @Override
    public void banMem(Integer memNo) {
        Member data = memberRepository.findById(memNo).orElse(null);
        memberRepository.updateMemStatById(memNo, Byte.valueOf("2"));
    }

    /**
     * 禁用指定會員並更新其狀態。
     */
    @Override
    public void banMember(Member member) {
        // 設置會員狀態為 2，表示被禁用
        member.setMemStat((byte) 2);
        // 保存修改後的會員信息
        memberRepository.save(member);
    }


    /**
     * 檢查會員帳號是否在系統中存在。
     */
    @Override
    public boolean existMemAccount(String memAcc) {
        return memberRepository.existsByMemAcc(memAcc);
    }

    /**
     * 檢查會員手機號碼是否在系統中存在。
     */
    @Override
    public boolean existMemMobile(String memMob) {
        return memberRepository.existsByMemMob(memMob);
    }

    /**
     * 檢查會員信箱是否在系統中存在。
     */
    @Override
    public boolean existMemMail(String memMail) {
        return memberRepository.existsByMemMail(memMail);
    }

    /**
     * 根據會員編號（memNo）查找會員。
     */
    @Override
    public Member getMemberByMemNo(Integer memNo) {
        Optional<Member> member = memberRepository.findMemberByMemNo(memNo);
        return member.orElse(null);
    }

    /**
     * U:
     * 登入成功，修改會員登入狀態
     */
    @Override
    public LoginStateMember loginState(Member member, HttpSession session) {
        // Login 1:登入 0:登出 , Logout 1:登出 0:登入
        // 四種情況 1.已登入未登出(帳號使用中) 2.已登入已登出(異常) 3.未登入已登出(帳號未登入) 4.未登入未登出(異常)
        if (member.getMemLogin() == 0 || member.getMemLogout() == 1) {
            member.setMemLogin(Byte.valueOf("1"));
            member.setMemLogout(Byte.valueOf("0"));

            // 返回更新過後的 member
            member = memberRepository.save(member);
        }

        LoginStateMember loginStateMember = new LoginStateMember();
        loginStateMember.setMemNo(member.getMemNo());
        loginStateMember.setJessionid(session.getId());
        loginStateMember.setMemLogin(member.getMemLogin());
        loginStateMember.setMemLogout(member.getMemLogout());
        loginStateMember.setMemActiveTime(member.getMemActiveTime());

        return loginStateMember;
    }

    /**
     * 產生一個隨機的密碼。
     *
     * 此方法會產生一個長度為 8 的隨機密碼。
     * 密碼可能包含大小寫英文字母、小寫英文字母和數字。
     *
     * @return 長度為 8 的隨機密碼。
     */
    private String getAuthCode() {
        // A~Z unicode 65~90
        // a~z unicode 97~122
        String random = "";
        for (int i = 1; i <= 8; i++) {
            int getChose = (int)(Math.random() * 3);
            switch (getChose) {
                case 0:
                    int getNumber = getRandom(0, 9);
                    random += String.valueOf(getNumber);
                    break;
                case 1:
                    char getEnUp = (char) getRandom(65, 90);
                    random += String.valueOf(getEnUp);
                    break;
                case 2:
                    char getEnDown = (char) getRandom(97, 122);
                    random += String.valueOf(getEnDown);
                    break;
            }
        }
        return random;
    }

    private int getRandom(int min, int max) {
        return (int)(Math.random() * (max - min + 1) + min);
    }


}
