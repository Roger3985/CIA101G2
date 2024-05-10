package com.roger.columnreply.service.impl;

import com.roger.columnreply.entity.ColumnReply;
import com.roger.columnreply.repository.ColumnReplyRepository;
import com.roger.columnreply.service.ColumnReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
    private StringRedisTemplate redisTemplate;


    /**
     * 查找所有專欄文章回覆。
     */
    @Override
    public List<ColumnReply> findAll() {
        return columnReplyRepository.findAll();
    }
}
