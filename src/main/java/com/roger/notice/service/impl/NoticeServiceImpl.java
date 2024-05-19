package com.roger.notice.service.impl;

import com.roger.member.entity.Member;
import com.roger.notice.repository.NoticeRepository;
import com.roger.notice.entity.Notice;
import com.roger.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    /**
     * 將新的通知添加到資料庫中。
     */
    @Override
    public Notice addNotice(Notice notice) {
        notice.setNotTime(Timestamp.valueOf(String.valueOf(notice.getNotTime())));
        return noticeRepository.save(notice);
    }

    /**
     * 更新現有的通知。
     */
    @Override
    public Notice updateNotice(Notice notice) {
        return noticeRepository.save(notice);
    }

    /**
     * 根據會員編號取得通知。
    */
    @Override
    public Notice getOneByMemNo(Integer memNo) {
        return noticeRepository.getNoticeByMember_MemNo(memNo);
    }


    /**
     * 從資料庫中查找所有 Notice 實例。
     */
    @Override
    public List<Notice> getAll() {
        return noticeRepository.findAll();
    }

    /**
     * 根據會員編號查詢所有通知。
     */
    @Override
    public List<Notice> findNoticesByMemberMemNo(Integer memNo) {
        return noticeRepository.findNoticesByMemberMemNo(memNo);
    }

    /**
     * 根據提供的條件查詢通知。
     */
    @Override
    public List<Notice> getByAttributes(Map<String, Object> map) {

        if (map.isEmpty()) {
            return noticeRepository.findAll();
        }

        Integer memNo = null;
        String notContent = null;
        Timestamp notTime = null;
        Byte notStat = null;

        if (map.containsKey("memNo")) {
            memNo = (Integer) map.get("memNo");
        } else if (map.containsKey("notContent")) {
            notContent = (String) map.get("notContent");
        } else if (map.containsKey("notTime")) {
            notTime = (Timestamp) map.get("notTime");
        } else if (map.containsKey("notStat")) {
            notStat = (Byte) map.get("notStat");
        }

        return noticeRepository.findByAttribute(memNo, notContent, notTime, notStat);
    }

    /**
     * 獲取未讀通知的數量。
     */
    @Override
    public int getUnreadNoticeCount(Member member) {
        byte unreadState = 0; // 0 表示為未讀狀態
        return noticeRepository.countByMemberAndNotStat(member, unreadState);
    }

    /**
     * 獲取所有未讀通知。
     */
    @Override
    public List<Notice> getUnreadNotices() {
        byte unreadStat = 0; // 0 表示為未讀狀態
        return noticeRepository.findNoticesByNotStat(unreadStat);
    }

    /**
     * @param member 傳入的 Member 物件，用於查找相關的 Notice。
     */
    @Override
    public Notice getOneByMember(Member member) {
        return noticeRepository.getOneByMember(member);
    }

    /**
     * 根據給定的 motNo 查找對應的 Notice 實例。
     */
    @Override
    public Notice getOneNotice(Integer motNo) {
        Optional<Notice> optional = noticeRepository.findById(motNo);
        return optional.orElse(null);
    }

}
