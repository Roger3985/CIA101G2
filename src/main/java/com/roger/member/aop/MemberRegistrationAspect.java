//package com.roger.member.aop;
//
//import com.roger.member.entity.Member;
//import com.roger.member.service.MemberService;
//import com.roger.notice.entity.Notice;
//import com.roger.notice.service.NoticeService;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Aspect
//@Component
//public class MemberRegistrationAspect {
//
//    @Autowired
//    private MemberService memberService;
//
//    @Autowired
//    private NoticeService noticeService;
//
//    @AfterReturning(pointcut = "execution(* com.roger.member.service.MemberService.register(..))",
//                    returning = "newData")
//    public void afterMemberRegistration(Member newData) {
//        // 添加新增訊息的邏輯
//        String messageContent = "您的會員註冊已成功，歡迎加入我們fallElove的大家庭";
//
//        // 創建 SimpleDateFormat 物件，指定日期格式
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        // 格式化當前時間為字符串
//        String currentTimeString = sdf.format(new Date());
//
//        // 將字符串轉換為 Timestamp 物件
//        Timestamp timestamp = Timestamp.valueOf(currentTimeString);
//
//        Notice newNotice = noticeService.getOneByMember(newData);
//
//        newNotice.setMember(newData);
//        newNotice.setNotContent(messageContent);
//        newNotice.setNotStat((byte) 0);
//
//        // 設置 newNotice 的時間屬性為轉換後的時間戳
//        newNotice.setNotTime(timestamp);
//
//        // 保存通知物件到資料庫中
//        noticeService.addNotice(newNotice);
//
//    }
//}
