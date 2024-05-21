package com.roger.columnreply.service.impl;

import com.roger.columnarticle.entity.ColumnArticle;
import com.roger.columnarticle.service.ColumnArticleService;
import com.roger.columnreply.entity.ColumnReply;
import com.roger.columnreply.repository.ColumnReplyRepository;
import com.roger.columnreply.service.ColumnReplyService;
import com.roger.member.entity.Member;
import com.roger.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ColumnReplyServiceImpl implements ColumnReplyService {

    /**
     * 自動裝配的 ColumnReplyService，用於執行專欄文章回覆相關的資料庫操作。
     */
    @Autowired
    private ColumnReplyRepository columnReplyRepository;

    /**
     * 自動裝配的 StringRedisTemplate，用於執行與 Redis 資料庫操作，特別是針對字串類型的資料。
     */
    @Autowired
    @Qualifier("colStrStr")
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ColumnArticleService columnArticleService;


    /**
     * 查找所有專欄文章回覆。
     */
    @Override
    public List<ColumnReply> findAll() {
        return columnReplyRepository.findAll();
    }

    /**
     * 提交留言到專欄文章的方法。
     */
    @Override
    public void submitComment(Integer memNo, Integer artNo, String comContent) {
        // 創建一個新的 ColumnReply 物件
        ColumnReply columnReply = new ColumnReply();

        // 設置留言相關的資料
        columnReply.setMember(memberService.findByNo(memNo));
        columnReply.setColumnArticle(columnArticleService.getOneColumnArticle(artNo));
        columnReply.setComContent(comContent);
        columnReply.setComTime(new Timestamp(System.currentTimeMillis())); // 設置留言時間，可以根據實際需求進行調整
        columnReply.setComStat((byte) 1); // 假設默認狀態為顯示

        // 調用 Repository 保存到資料庫
        columnReplyRepository.save(columnReply);
    }

    /**
     * 根據文章編號查找所有相關的 {@link ColumnReply} 實體。
     */
    @Override
    public List<ColumnReply> getRepliesByArticleId(Integer artNo) {
        return columnReplyRepository.findColumnRepliesByArtNo(artNo);
    }

}
