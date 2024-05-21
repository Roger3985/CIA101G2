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

    /**
     * 提交留言到專欄文章的方法。
     *
     * @param memNo      會員編號。
     * @param artNo      專欄文章編號。
     * @param comContent 留言內容。
     */
    public void submitComment(Integer memNo, Integer artNo, String comContent);

    /**
     * 根據文章編號查找所有相關的 {@link ColumnReply} 實體。
     *
     * @param artNo 要查詢回復的文章編號。
     * @return 關聯到指定文章編號的 {@link ColumnReply} 實體列表。
     */
    public List<ColumnReply> getRepliesByArticleId(Integer artNo);
}
