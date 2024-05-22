package com.config;

import com.howard.rentalmytrack.dao.RentalMyTrackRepository;
import com.howard.rentalmytrack.entity.RentalMyTrack;
import com.howard.rentalorder.entity.RentalOrder;
import com.howard.rentalorder.dao.RentalOrderRepository;
import com.roger.columnarticle.service.ColumnArticleService;
import com.roger.member.service.MemberService;
import com.roger.notice.entity.Notice;
import com.roger.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    RentalOrderRepository orderRepository;

    @Autowired
    RentalMyTrackRepository trackRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    NoticeService noticeService;

    @Autowired
    ColumnArticleService columnArticleService;

    /**
     * 每天 0 時 0 分 0 秒檢查所有租借訂單有沒有距離歸還日 =< 3天的，若有，存入通知
     */
    @Scheduled(cron = "0 0 0 * * ?") /* 0 秒 0 分 0 時 每天 每月 不管星期幾 (沒指定年分=每一年) */
    public void checkAllOrdersBackDate() {

        List<RentalOrder> orders = orderRepository.findAll();
        for (RentalOrder order : orders) {
            String returnRemind = null;
            Integer memNo = null;
            System.out.println("檢查歸還日期排程器啟動了");
            // 如果 以歸還、已完成 則不新增通知
            if (order.getrentalOrdStat() == (byte)50 || order.getrentalOrdStat() == (byte)45) {
                continue;
            }
            LocalDateTime now = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
            LocalDateTime rentalBackDate = order.getrentalBackDate().toLocalDateTime();
            long daysRemain = Duration.between(now, rentalBackDate).toDays();
            /*-------------------- 依照剩餘天數存入通知 --------------------*/
            if (daysRemain > 3) { // > 3 天，什麼也不做
                continue;
            }
            if(daysRemain < 0 || daysRemain == 0) { // 過這個 if => 0 < 剩餘天數 < 3
                returnRemind = "溫馨提醒 : 有筆訂單已到期限需歸還喔!快去查看訂單吧!";
                memNo = order.getMember().getMemNo();
            } else {
                returnRemind = "溫馨提醒 : 有筆訂單在" + daysRemain + "天內需歸還喔!快去查看訂單吧!";
                memNo = order.getMember().getMemNo();
            }
            // 新增點讚的通知消息
            Notice newNotice = new Notice();
            newNotice.setMember(memberService.findByNo(memNo));
            newNotice.setNotContent(returnRemind);
            newNotice.setNotTime(new Timestamp(System.currentTimeMillis()));
            newNotice.setNotStat((byte) 0);
            noticeService.addNotice(newNotice);
        }

    }

    /**
     * 每天 18 時 0 分 0 秒檢查所有 租借品追蹤 有沒有商品狀態已經變為 0 (上架)，若有，存入通知
     */
    @Scheduled(cron = "0 0 18 * * ?")
    public void checkRentalStat() {

        List<RentalMyTrack> tracks = trackRepository.findAll();
        for (RentalMyTrack track : tracks) {
            if (track.getRental().getRentalStat() == 0) {
                Notice newNotice = new Notice();
                newNotice.setMember(memberService.findByNo(track.getCompositeTrack().getMemNo()));
                newNotice.setNotContent( track.getRental().getRentalName() + " 已重新上架囉!快去購物車看看吧!" );
                newNotice.setNotTime(new Timestamp(System.currentTimeMillis()));
                newNotice.setNotStat((byte) 0);
                noticeService.addNotice(newNotice);
            }
        }

    }

}
