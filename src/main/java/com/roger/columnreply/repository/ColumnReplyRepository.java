package com.roger.columnreply.repository;

import com.roger.columnreply.entity.ColumnReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnReplyRepository extends JpaRepository<ColumnReply, Integer> {

    /**
     * 根據給定的文章編號查找相關的 {@link ColumnReply} 實體列表。
     *
     * @param artNo 要查詢回復的文章編號。
     * @return 關聯到指定文章編號的 {@link ColumnReply} 實體列表。
     */
    @Query("SELECT cr FROM ColumnReply cr WHERE cr.columnArticle.artNo = :artNo")
    List<ColumnReply> findColumnRepliesByArtNo(@Param("artNo") Integer artNo);

    /**
     * 根據會員編號（memNo）查詢該會員提交的所有文章回覆。
     *
     * @param memNo 要查詢的會員編號。
     * @return 包含指定會員提交的所有文章回覆的列表。
     */
    List<ColumnReply> findByMemberMemNo(Integer memNo);

    /**
     * 根據列回覆編號查詢列回覆。
     *
     * @param columnReplyNo 列回覆編號
     * @return 對應於給定編號的列回覆對象，如果未找到則返回 null
     */
    ColumnReply findByColumnReplyNo(Integer columnReplyNo);

    /**
     * 根據 columnReplyNo 刪除相應的 ColumnReply 實體。
     *
     * 此方法從資料庫中刪除具有給定 columnReplyNo 的 ColumnReply 實體。
     * 如果不存在具有給定 columnReplyNo 的實體，則該方法不執行任何操作。
     *
     * @param columnReplyNo 要刪除的 ColumnReply 的唯一標識符。
     *                      不能為空。
     * @throws IllegalArgumentException 如果給定的 columnReplyNo 為空。
     */
    void deleteByColumnReplyNo(Integer columnReplyNo);

}
