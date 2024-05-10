package com.roger.columnreply.repository;

import com.roger.columnreply.entity.ColumnReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnReplyRepository extends JpaRepository<ColumnReply, Integer> {
}
