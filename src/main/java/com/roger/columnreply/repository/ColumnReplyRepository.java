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
}
