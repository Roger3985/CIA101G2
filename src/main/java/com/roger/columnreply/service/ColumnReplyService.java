package com.roger.columnreply.service;

import com.roger.columnreply.entity.ColumnReply;

import java.util.List;

public interface ColumnReplyService {

    /**
     * 查找所有專欄文章回覆。
     *
     * @return 包含所有專欄文章回覆的 List<ColumnReply> 列表。
     */
    public List<ColumnReply> findAll();
}
