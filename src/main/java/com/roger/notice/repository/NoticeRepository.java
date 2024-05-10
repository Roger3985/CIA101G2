package com.roger.notice.repository;

import com.roger.member.entity.Member;
import com.roger.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    @Transactional
    public Notice getOneByMember(Member member);

    /**
     * 根據會員編號查詢所有通知。
     *
     * @param memNo 會員編號
     * @return 該會員所有通知的列表
     */
    @Transactional
    List<Notice> findNoticesByMemberMemNo(Integer memNo);

    @Transactional
    @Query("SELECT n FROM Notice n WHERE " +
            "(:memNo IS NULL OR n.member.memNo = :memNo) AND " +
            "(:notContent IS NULL OR n.notContent = :notContent) AND " +
            "(:notTime IS NULL OR n.notTime = :notTime) AND " +
            "(:notStat IS NULL OR n.notStat = :notStat)")
    List<Notice> findByAttribute(@Param("memNo") Integer memNo,
                                 @Param("notContent") String notContent,
                                 @Param("notTime") Timestamp notTime,
                                 @Param("notStat") Byte notStat);
}
