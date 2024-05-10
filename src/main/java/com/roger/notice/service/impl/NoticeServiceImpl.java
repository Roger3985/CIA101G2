package com.roger.notice.service.impl;

import com.roger.member.entity.Member;
import com.roger.notice.repository.NoticeRepository;
import com.roger.notice.entity.Notice;
import com.roger.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
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
     * 從資料庫中查找所有 Notice 實例。
     */
    @Override
    public List<Notice> getAll() {
        return noticeRepository.findAll();
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
