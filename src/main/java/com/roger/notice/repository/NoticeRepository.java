package com.roger.notice.repository;

import com.roger.member.entity.Member;
import com.roger.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    @Transactional
    public Notice getOneByMember(Member member);
}
